package com.example.trabalhofinal.ui.home;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.trabalhofinal.MainActivity;
import com.example.trabalhofinal.R;
import com.example.trabalhofinal.controller.MatchController;
import com.example.trabalhofinal.controller.TableMatch;
import com.example.trabalhofinal.controller.TeamController;
import com.example.trabalhofinal.data.model.DBHelper;
import com.example.trabalhofinal.data.model.Match;
import com.example.trabalhofinal.data.model.Team;
import com.example.trabalhofinal.data.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private DBHelper db;
    private View root;
    private Dialog newMatchDialog, penaltiesDialog;
    private DatePickerDialog dpd;
    private EditText edtDate, edtGame, edtGameMode, edtRival, edtGoalsFor, edtGoalsAgainst;
    private User loggedUser;
    private TextView tvNoMatch;
    private Spinner spHomeTeam, spAwayTeam;
    private ImageView imgAwayTeam, imgHomeTeam, imgNoMatch;
    private final String POSITIVE = "Positive";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        loggedUser = ((MainActivity)getActivity()).getLoggedUser();

        init();

        newMatchDialog = new Dialog(getContext());
        penaltiesDialog = new Dialog(getContext());

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        db = DBHelper.getInstance(getContext());

        List<Match> matches = db.getAllMatches(loggedUser);
        // TODO: otimizar o código dessa condição
        if (matches.isEmpty()) {
            tvNoMatch = root.findViewById(R.id.tvNoMatch);
            imgNoMatch = root.findViewById(R.id.imgNoMatch);
            imgNoMatch.setImageDrawable(getDrawable("ic_no_match"));
            tvNoMatch.setText(getResources().getString(R.string.label_no_found_match));
        } else {
            TableLayout tableLayout = root.findViewById(R.id.table_matches);
            TableMatch tableMatch = new TableMatch(getActivity(), tableLayout, loggedUser);
            tableMatch.initTable(matches);
        }

        db.close();
    }

    private void openPenaltiesDialog(final Match match) {
        final Match penMatch = match;
        penaltiesDialog.setContentView(R.layout.dialog_penalties);
        Button btnNoPenalties = (Button) penaltiesDialog.findViewById(R.id.btnNoPenalties);
        Button btnPenalties = (Button) penaltiesDialog.findViewById(R.id.btnPenalties);
        final EditText edtGoalsForPen = (EditText) penaltiesDialog.findViewById(R.id.edtGoalsForPen);
        final EditText edtGoalsAgainstPen = (EditText) penaltiesDialog.findViewById(R.id.edtGoalsAgainstPen);
        ImageView imgHomeTeam = (ImageView) penaltiesDialog.findViewById(R.id.imgHomeTeam);
        ImageView imgAwayTeam = (ImageView) penaltiesDialog.findViewById(R.id.imgAwayTeam);

        Team homeTeam = new Team();
        TeamController tc = new TeamController(penaltiesDialog.getContext());
        homeTeam.setName(match.getMyTeam());
        homeTeam.setBadgePath();
        imgHomeTeam.setImageDrawable(tc.getTeamBadge(homeTeam.getBadgePath()));

        // TODO
        Team awayTeam = new Team();
        awayTeam.setName(match.getRivalTeam());
        awayTeam.setBadgePath();
        imgAwayTeam.setImageDrawable(tc.getTeamBadge(awayTeam.getBadgePath()));

        final String MATCH_ADDED = getResources().getString(R.string.message_match_added);
        btnPenalties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer penGF = Integer.parseInt(edtGoalsForPen.getText().toString());
                Integer penGA = Integer.parseInt(edtGoalsAgainstPen.getText().toString());
                if ((penGF != 0 || penGF != null) && (penGA != 0 || penGA != null)) {
                    penMatch.setPenaltiesGF(Integer.parseInt(edtGoalsForPen.getText().toString()));
                    penMatch.setPenaltiesGA(Integer.parseInt(edtGoalsAgainstPen.getText().toString()));
                    System.out.println(penMatch.getPenaltiesGF());
                }
                MatchController mc = new MatchController(penaltiesDialog.getContext());
                mc.addMatch(penMatch, loggedUser);
                createToast(MATCH_ADDED, POSITIVE);
                penaltiesDialog.dismiss();
                getActivity().recreate();
            }
        });

        btnNoPenalties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchController mc = new MatchController(penaltiesDialog.getContext());
                mc.addMatch(penMatch, loggedUser);
                createToast(MATCH_ADDED, POSITIVE);
                penaltiesDialog.dismiss();
                getActivity().recreate();
            }
        });

        penaltiesDialog.show();
    }

    private void openDialog() {
        newMatchDialog.setContentView(R.layout.dialog_add_match);
        Button btnClose;
        Button btnAddMatch;
        edtDate = (EditText) newMatchDialog.findViewById(R.id.edtDate);
        Button btnDatePicker = (Button) newMatchDialog.findViewById(R.id.btnDatePicker);
        edtGame = (EditText) newMatchDialog.findViewById(R.id.edtGame);
        edtGameMode = (EditText) newMatchDialog.findViewById(R.id.edtGameMode);
        edtRival = (EditText) newMatchDialog.findViewById(R.id.edtRival);
        spHomeTeam = (Spinner) newMatchDialog.findViewById(R.id.spHomeTeam);
        spAwayTeam = (Spinner) newMatchDialog.findViewById(R.id.spAwayTeam);
        edtGoalsFor = (EditText) newMatchDialog.findViewById(R.id.edtGoalsFor);
        edtGoalsAgainst = (EditText) newMatchDialog.findViewById(R.id.edtGoalsAgainst);
        imgHomeTeam = (ImageView) newMatchDialog.findViewById(R.id.imgHomeTeam);
        imgAwayTeam = (ImageView) newMatchDialog.findViewById(R.id.imgAwayTeam);
        btnClose = (Button) newMatchDialog.findViewById(R.id.btnClose);
        btnAddMatch = (Button) newMatchDialog.findViewById(R.id.btnAddMatch);
        final TextView tvWarning = newMatchDialog.findViewById(R.id.tvWarning);

        final ArrayAdapter<String> adapter = new ArrayAdapter(
                newMatchDialog.getContext(),
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.teams_array));

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);

        spHomeTeam.setAdapter(adapter);
        spAwayTeam.setAdapter(adapter);

        edtDate.setText(currentDate());
        edtDate.setFocusable(false);

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvWarning.setText(getResources().getString(R.string.message_warning));
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edtDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);

                tvWarning.setText("");
                dpd.show();
            }
        });

        spHomeTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Team team = new Team();
                TeamController tc = new TeamController(newMatchDialog.getContext());
                team.setName(spHomeTeam.getSelectedItem().toString());
                team.setBadgePath();
                imgHomeTeam.setImageDrawable(tc.getTeamBadge(team.getBadgePath()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAwayTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Team team = new Team();
                TeamController tc = new TeamController(newMatchDialog.getContext());
                team.setName(spAwayTeam.getSelectedItem().toString());
                team.setBadgePath();
                imgAwayTeam.setImageDrawable(tc.getTeamBadge(team.getBadgePath()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMatchDialog.dismiss();
            }
        });

        btnAddMatch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                final String MATCH_ADDED = getResources().getString(R.string.message_match_added);
                Match addMatch = assembleMatch();
                Integer goalsFor = Integer.parseInt(edtGoalsFor.getText().toString());
                Integer goalsAgainst = Integer.parseInt(edtGoalsAgainst.getText().toString());
                if (goalsFor == goalsAgainst)
                    openPenaltiesDialog(addMatch);
                else {
                    MatchController mc = new MatchController(penaltiesDialog.getContext());
                    mc.addMatch(addMatch, loggedUser);
                    createToast(MATCH_ADDED, POSITIVE);
                    getActivity().recreate();
                }

                newMatchDialog.dismiss();
            }
        });

        newMatchDialog.show();
    }

    private Match assembleMatch() {
        String homeTeam = spHomeTeam.getSelectedItem().toString();
        String awayTeam = spAwayTeam.getSelectedItem().toString();

        Match match = new Match();

        match.setMatchDate(Date.valueOf(edtDate.getText().toString()));
        match.setGame(edtGame.getText().toString());
        match.setGameMode(edtGameMode.getText().toString());
        match.setRival(edtRival.getText().toString());
        match.setMyTeam(homeTeam);
        match.setRivalTeam(awayTeam);
        match.setGoalsFor(Integer.parseInt(edtGoalsFor.getText().toString()));
        match.setGoalsAgainst(Integer.parseInt(edtGoalsAgainst.getText().toString()));

        return match;
    }

    public void createToast(String text, String toastType) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = null;
        if (toastType.equals("Negative")) {
            layout = inflater.inflate(R.layout.custom_negative_toast,
                    (ViewGroup) root.findViewById(R.id.custom_negative_toast_container));
            TextView textMsg = layout.findViewById(R.id.tvNegativeToast);
            textMsg.setText(text);

        } else if (toastType.equals("Positive")) {
            layout = inflater.inflate(R.layout.custom_positive_toast,
                    (ViewGroup) root.findViewById(R.id.custom_positive_toast_container));
            TextView textMsg = layout.findViewById(R.id.tvPositiveToast);
            textMsg.setText(text);
        } else {
            layout = inflater.inflate(R.layout.custom_info_toast,
                    (ViewGroup) root.findViewById(R.id.custom_info_toast_container));
            TextView textMsg = layout.findViewById(R.id.tvInfoToast);
            textMsg.setText(text);
        }

        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 10);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private String currentDate() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(calendar.getTime());
    }

    private Drawable getDrawable(String drawableName) {
        Resources res = getResources();
        int resId = res.getIdentifier(drawableName, "drawable", getContext().getPackageName());
        Drawable drawable = res.getDrawable(resId);
        return drawable;
    }

}