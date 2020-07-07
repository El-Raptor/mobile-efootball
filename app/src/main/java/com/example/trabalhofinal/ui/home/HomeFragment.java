package com.example.trabalhofinal.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
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
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.trabalhofinal.MainActivity;
import com.example.trabalhofinal.R;
import com.example.trabalhofinal.controller.MatchController;
import com.example.trabalhofinal.controller.Table;
import com.example.trabalhofinal.controller.TableMatch;
import com.example.trabalhofinal.controller.TeamController;
import com.example.trabalhofinal.data.model.DBHelper;
import com.example.trabalhofinal.data.model.Match;
import com.example.trabalhofinal.data.model.Player;
import com.example.trabalhofinal.data.model.Stats;
import com.example.trabalhofinal.data.model.Team;
import com.example.trabalhofinal.data.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private DBHelper db;
    private View root;
    private Dialog newMatchDialog, penaltiesDialog;
    private DatePickerDialog dpd;
    private EditText edtDate, edtGame, edtGameMode, edtRival, edtGoalsFor, edtGoalsAgainst;
    private User loggedUser;
    private Spinner spHomeTeam, spAwayTeam;
    private ImageView imgAwayTeam, imgHomeTeam;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
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
            /*ConstraintLayout constraintLayout = root.findViewById(R.id.clHome);
            ConstraintSet set = new ConstraintSet();

            TextView emptyList = new TextView(getContext());
            emptyList.setText("Nenhuma partida encontrada!");
            emptyList.setId(View.generateViewId());
            emptyList.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            constraintLayout.addView(emptyList, 0);

            set.clone(constraintLayout);
            // Adicionando as constraints layouts para o novo TextView.
            set.centerHorizontally(emptyList.getId(), constraintLayout.getId());
            set.centerVertically(emptyList.getId(), constraintLayout.getId());

            set.applyTo(constraintLayout);*/
        } else {
            TableLayout tableLayout = root.findViewById(R.id.table_matches);
            TableMatch tableMatch = new TableMatch(getActivity(), tableLayout, loggedUser);
            tableMatch.initTable(matches);
        }

        db.close();
    }

    private void openPenaltiesDialog(final Match match) {
        final Match penMatch = match;
        penaltiesDialog.setContentView(R.layout.fragment_penalties);
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
                penaltiesDialog.dismiss();
                getActivity().recreate();
            }
        });

        btnNoPenalties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchController mc = new MatchController(penaltiesDialog.getContext());
                mc.addMatch(penMatch, loggedUser);
                penaltiesDialog.dismiss();
                getActivity().recreate();
            }
        });

        penaltiesDialog.show();
    }

    private void openDialog() {
        newMatchDialog.setContentView(R.layout.fragment_add_match);
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

        final ArrayAdapter<String> adapter = new ArrayAdapter(
                newMatchDialog.getContext(),
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.teams_array));

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);

        spHomeTeam.setAdapter(adapter);
        spAwayTeam.setAdapter(adapter);

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
                }, day, month, year);
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
                Match addMatch = assembleMatch();
                System.out.println("On click " + addMatch.getMyTeam());
                Integer goalsFor = Integer.parseInt(edtGoalsFor.getText().toString());
                Integer goalsAgainst = Integer.parseInt(edtGoalsAgainst.getText().toString());
                if (goalsFor == goalsAgainst)
                    openPenaltiesDialog(addMatch);
                else {
                    MatchController mc = new MatchController(penaltiesDialog.getContext());
                    mc.addMatch(addMatch, loggedUser);
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

        System.out.println("Assemble " + match.getGoalsFor());

        return match;
    }


}