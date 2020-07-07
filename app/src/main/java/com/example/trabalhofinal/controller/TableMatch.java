package com.example.trabalhofinal.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.trabalhofinal.R;
import com.example.trabalhofinal.data.model.DBHelper;
import com.example.trabalhofinal.data.model.Match;
import com.example.trabalhofinal.data.model.Team;
import com.example.trabalhofinal.data.model.User;

import org.w3c.dom.Text;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TableMatch extends Table<Match> {

    private Dialog editDialog;
    private Dialog newMatchDialog;
    private Dialog penaltiesDialog;
    private List<String> labels = new ArrayList<String>(){
        {
            add("Data");
            add("Adversário");
            add("T. Casa");
            add("Res.");
            add("T.Fora");
        }
    };
    private EditText edtGame, edtDate;
    private EditText edtGameMode;
    private EditText edtRival, edtGoalsFor, edtGoalsAgainst;
    private Spinner spHomeTeam, spAwayTeam;
    private ImageView imgHomeTeam, imgAwayTeam;
    private DatePickerDialog dpd;
    private User loggedUser;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TableMatch(Context context, TableLayout tl, User user) {
        super(context, tl, 5);
        loggedUser = user;
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initTable(List<Match> matches) {
        header(labels);
        dataRows(matches, super.getNumberOfColumns());
    }

    @Override
    protected void dataRows(final List<Match> matches, int numberOfColumns) {
        final List<TableRow> rows = new ArrayList<>();

        for (int i = 0; i < matches.size(); i++) {
            rows.add(new TableRow(super.getContext()));
            rows.get(i).setLayoutParams(super.getLp());
            rows.get(i).setPadding(5, 15, 5, 15);
            if (i % 2 == 0)
                rows.get(i).setBackgroundColor(super.getResources().getColor(R.color.whisper_gray));
            else
                rows.get(i).setBackgroundColor(super.getResources().getColor(R.color.gray));
            rows.get(i).setGravity(Gravity.CENTER);
            setValues(rows.get(i), matches.get(i), numberOfColumns);
            getTl().addView(rows.get(i));

            final int id = i;
            final TableRow currentRow = rows.get(i);
            currentRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int matchId = matches.get(id).getId();
                    editDialog = new Dialog(getContext());
                    openDialog(matchId);
                    return true;
                }
            });

        }

    }

    private void openDialog(final int id) {
        editDialog.setContentView(R.layout.fragment_edit_row);
        Button btnEdit = editDialog.findViewById(R.id.btnEdit);
        Button btnDelete = editDialog.findViewById(R.id.btnEdit);
        ImageView imgClose = (ImageView) editDialog.findViewById(R.id.imgClose);


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMatchDialog = new Dialog(getContext());
                openDialog(loggedUser, id);
                editDialog.dismiss();
            }
        });

        /*btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });*/

        /*imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });*/

        editDialog.show();
    }

    @Override
    protected void setValues(TableRow row, Match match, int numberOfColumns) {
        List<TextView> values = new ArrayList<>();

        for (int i = 0; i < numberOfColumns-1; i++) {
            values.add(new TextView(super.getContext()));
            values.get(i).setGravity(Gravity.CENTER);
            values.get(i).setText(getValues(match.toString())[i]);

            if (i == 3) {
                String goalsFor = getValues(match.toString())[i];
                String goalsAgainst = getValues(match.toString())[i+1];
                String result = goalsFor + " x " + goalsAgainst;
                values.get(i).setText(result);

                int penGf = Integer.parseInt(getValues(match.toString())[i+3]);
                int penGa = Integer.parseInt(getValues(match.toString())[i+4]);
                if (penGf != 0 && penGa != 0) {
                    String penaltiesResult = penGf + " x " + penGa;
                    String matchScore = values.get(i).getText().toString();
                    values.get(i).setText(matchScore + "\n(" + penaltiesResult + ")");
                }
            }

            row.addView(values.get(i));
        }

        values.add(new TextView(super.getContext()));
        values.get(4).setGravity(Gravity.CENTER);
        values.get(4).setText(getValues(match.toString())[5]);

        row.addView(values.get(4));

    }

    private void openDialog(final User loggedUser, final int id) {
        newMatchDialog.setContentView(R.layout.fragment_add_match);
        Button btnClose = (Button) newMatchDialog.findViewById(R.id.btnClose);
        Button btnAddMatch = (Button) newMatchDialog.findViewById(R.id.btnAddMatch);
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

        ArrayAdapter<String> adapter = new ArrayAdapter(
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
                DBHelper db = DBHelper.getInstance(getContext());
                Match oldMatch = db.getMatch(loggedUser, id).get(0);
                Match newMatch = assembleMatch();

                Integer goalsFor = Integer.parseInt(edtGoalsFor.getText().toString());
                Integer goalsAgainst = Integer.parseInt(edtGoalsAgainst.getText().toString());
                if (goalsFor == goalsAgainst) {
                    penaltiesDialog = new Dialog(getContext());
                    openPenaltiesDialog(newMatch, oldMatch);
                } else {
                    MatchController mc = new MatchController(newMatchDialog.getContext());
                    mc.updateMatch(oldMatch, newMatch, loggedUser);
                }
                Activity activity = (Activity) getContext();
                activity.recreate();
                newMatchDialog.dismiss();
            }
        });

        newMatchDialog.show();
    }

    private void openPenaltiesDialog(final Match match, Match oldMatch) {
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
            }
        });

        btnNoPenalties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchController mc = new MatchController(penaltiesDialog.getContext());
                mc.addMatch(penMatch, loggedUser);
                penaltiesDialog.dismiss();
            }
        });

        penaltiesDialog.show();
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
