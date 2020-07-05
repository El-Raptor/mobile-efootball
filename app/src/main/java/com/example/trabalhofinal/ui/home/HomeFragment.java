package com.example.trabalhofinal.ui.home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
    private Match newMatch;
    private User loggedUser;
    private Spinner spHomeTeam, spAwayTeam;
    private ImageView imgAwayTeam, imgHomeTeam;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
        homeTeam.setName(match.getMyTeam());
        homeTeam.setBadgePath();
        imgHomeTeam.setImageDrawable(getTeamBadge(homeTeam.getBadgePath()));

        // TODO
        Team awayTeam = new Team();
        awayTeam.setName(match.getRivalTeam());
        awayTeam.setBadgePath();
        imgAwayTeam.setImageDrawable(getTeamBadge(awayTeam.getBadgePath()));

        btnPenalties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer penGF = Integer.parseInt(edtGoalsForPen.getText().toString());
                Integer penGA = Integer.parseInt(edtGoalsAgainstPen.getText().toString());
                if ((penGF != 0 || penGF != null) && (penGA != 0 || penGA != null)) {
                    System.out.println("EdtGF");
                    System.out.println(Integer.parseInt(edtGoalsForPen.getText().toString()));
                    penMatch.setPenaltiesGF(Integer.parseInt(edtGoalsForPen.getText().toString()));
                    penMatch.setPenaltiesGA(Integer.parseInt(edtGoalsAgainstPen.getText().toString()));
                    System.out.println(penMatch.getPenaltiesGF());
                }
                addMatch(penMatch);
                penaltiesDialog.dismiss();
            }
        });

        btnNoPenalties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                penaltiesDialog.dismiss();
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
                Match addMatch = new Match();
                Integer goalsFor = Integer.parseInt(edtGoalsFor.getText().toString());
                Integer goalsAgainst = Integer.parseInt(edtGoalsAgainst.getText().toString());
                System.out.println("Pen dialog: " + goalsFor);
                if (goalsFor == goalsAgainst)
                    openPenaltiesDialog(assembleMatch());
                // TODO Refatorar (assemble match está sendo chamado demais)
                else
                    addMatch(addMatch);
                newMatchDialog.dismiss();
            }
        });

        spHomeTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Team team = new Team();
                team.setName(spHomeTeam.getSelectedItem().toString());
                team.setBadgePath();
                imgHomeTeam.setImageDrawable(getTeamBadge(team.getBadgePath()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAwayTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Team team = new Team();
                team.setName(spAwayTeam.getSelectedItem().toString());
                team.setBadgePath();
                imgAwayTeam.setImageDrawable(getTeamBadge(team.getBadgePath()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        newMatchDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void init() {
        db = DBHelper.getInstance(getContext());
        List<Player> players = db.getAllPlayers(loggedUser);

        List<Match> matches = db.getAllMatches(loggedUser);
        for (Player p : players) {
            System.out.println(p.getName());
            System.out.println(p.getStats().getGamesPlayed());
        }

        if (matches.isEmpty()) {
            ConstraintLayout constraintLayout = root.findViewById(R.id.clHome);
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

            set.applyTo(constraintLayout);
        } else {
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            initTable(matches, lp);
        }

        db.close();
    }

    private Match assembleMatch() {
        String homeTeam = spHomeTeam.getSelectedItem().toString();
        String awayTeam = spAwayTeam.getSelectedItem().toString();

        newMatch = new Match();

        newMatch.setMatchDate(Date.valueOf(edtDate.getText().toString()));
        newMatch.setGame(edtGame.getText().toString());
        newMatch.setGameMode(edtGameMode.getText().toString());
        newMatch.setRival(edtRival.getText().toString());
        newMatch.setMyTeam(homeTeam);
        newMatch.setRivalTeam(awayTeam);
        newMatch.setGoalsFor(Integer.parseInt(edtGoalsFor.getText().toString()));
        newMatch.setGoalsAgainst(Integer.parseInt(edtGoalsAgainst.getText().toString()));

        return newMatch;
    }

    private Drawable getTeamBadge(String badgePath) {
        Resources res = getResources();
        int resId = res.getIdentifier(badgePath, "drawable", getActivity().getPackageName());
        Drawable drawable = res.getDrawable(resId);
        return drawable;
    }

    @SuppressLint("NewApi")
    private void initTable(List<Match> matches, TableRow.LayoutParams lp) {
        TableLayout tableLayout = root.findViewById(R.id.table_matches);
        Resources res = getResources();

        // Cabeçalho da tabela.
        TableRow headers = new TableRow(getContext());
        headers.setLayoutParams(lp);
        headers.setBackground(res.getDrawable(R.drawable.table_style));
        headers.setElevation(2);

        TextView label1 = new TextView(getContext());
        label1.setText("Data");
        label1.setTextColor(Color.WHITE);
        label1.setGravity(Gravity.CENTER);
        label1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        label1.setPadding(10, 10,10,10);

        /*TextView label2 = new TextView(getContext());
        label2.setText("Jogo");
        label2.setTextColor(Color.WHITE);
        label2.setBackgroundColor(Color.rgb(20, 20, 20));
        label2.setGravity(Gravity.CENTER);

        TextView label3 = new TextView(getContext());
        label3.setText("Modo de Jogo");
        label3.setTextColor(Color.WHITE);
        label3.setBackgroundColor(Color.rgb(20, 20, 20));
        label3.setGravity(Gravity.CENTER);*/

        TextView label4 = new TextView(getContext());
        label4.setText("Adversário");
        label4.setTextColor(Color.WHITE);
        label4.setGravity(Gravity.CENTER);
        label4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        label4.setPadding(10, 10,10,10);

        TextView label5 = new TextView(getContext());
        label5.setText("T. Casa");
        label5.setTextColor(Color.WHITE);
        label5.setGravity(Gravity.CENTER);
        label5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        label5.setPadding(10, 10,10,10);

        TextView label6 = new TextView(getContext());
        label6.setText("Res.");
        label6.setTextColor(Color.WHITE);
        label6.setGravity(Gravity.CENTER);
        label6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        label6.setPadding(10, 10,10,10);

        TextView label7 = new TextView(getContext());
        label7.setText("T. Fora");
        label7.setTextColor(Color.WHITE);
        label7.setGravity(Gravity.CENTER);
        label7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        label7.setPadding(10, 10,10,10);

        /*TextView label8 = new TextView(getContext());
        label8.setText("Pênaltis");
        label8.setTextColor(Color.WHITE);
        label8.setBackgroundColor(Color.rgb(20, 20, 20));
        label8.setGravity(Gravity.CENTER);
        label8.setPadding(5, 5,5,5);*/

        headers.addView(label1);
        //headers.addView(label2);
        //headers.addView(label3);
        headers.addView(label4);
        headers.addView(label5);
        headers.addView(label6);
        headers.addView(label7);
        //headers.addView(label8);

        tableLayout.addView(headers);

        tableSetValues(matches, tableLayout, lp);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void tableSetValues(List<Match> matches, TableLayout tableLayout, TableRow.LayoutParams lp) {
        for (Match match : matches) {
            TableRow tableRow = new TableRow(getActivity());
            tableRow.setLayoutParams(lp);
            tableRow.setPadding(5, 0, 5, 0);
            tableRow.setBackgroundColor(Color.rgb(231, 231, 231));
            tableRow.setGravity(Gravity.CENTER);

            TextView column1 = new TextView(getActivity());
            column1.setText(match.getMatchDate().toString());
            column1.setBackgroundColor(Color.rgb(231, 231, 231));
            column1.setGravity(Gravity.CENTER);

            /*TextView column2 = new TextView(getContext());
            column2.setText(match.getGame());
            column2.setBackgroundColor(Color.rgb(231, 231, 231));
            column2.setGravity(Gravity.CENTER);

            TextView column3 = new TextView(getContext());
            column3.setText(match.getGameMode());
            column3.setBackgroundColor(Color.rgb(231, 231, 231));
            column3.setGravity(Gravity.CENTER);*/

            TextView column4 = new TextView(getContext());
            column4.setText(match.getRival());
            column4.setBackgroundColor(Color.rgb(231, 231, 231));
            column4.setGravity(Gravity.CENTER);

            TextView column5 = new TextView(getContext());
            column5.setText(match.getMyTeam());
            column5.setBackgroundColor(Color.rgb(231, 231, 231));
            column5.setGravity(Gravity.CENTER);

            TextView column6 = new TextView(getContext());
            String result = match.getGoalsFor() + " x " + match.getGoalsAgainst();
            column6.setText(result);
            column6.setBackgroundColor(Color.rgb(231, 231, 231));
            column6.setGravity(Gravity.CENTER);

            TextView column7 = new TextView(getContext());
            column7.setText(match.getRivalTeam());
            column7.setBackgroundColor(Color.rgb(231, 231, 231));
            column7.setGravity(Gravity.CENTER);
            System.out.println("Pen HF: " + match.getPenaltiesGF());
            if (!(match.getPenaltiesGF() == 0 && match.getPenaltiesGA() == 0)) {
                String penaltiesResult = match.getPenaltiesGF() + " x " + match.getPenaltiesGA();
                String matchScore = column7.getText().toString();
                column7.setText(matchScore + "\n(" + penaltiesResult + ")");
            }

            /*TextView column8 = new TextView(getContext());
            if (match.getPenaltiesGF() == 0 && match.getPenaltiesGA() == 0)
                column8.setText(" ");
            else {
                String penaltiesResult = match.getPenaltiesGF() + " x " + match.getPenaltiesGA();
                column8.setText(penaltiesResult);
            }
            column8.setBackgroundColor(Color.rgb(231, 231, 231));
            column8.setGravity(Gravity.CENTER);*/

            tableRow.addView(column1);
            //tableRow.addView(column2);
            //tableRow.addView(column3);
            tableRow.addView(column4);
            tableRow.addView(column5);
            tableRow.addView(column6);
            tableRow.addView(column7);
            //tableRow.addView(column8);

            tableLayout.addView(tableRow);
        }
    }

    // TODO: Refatorar (?).

    public void addMatch(Match newMatch) {

        db = DBHelper.getInstance(getActivity());
        checkTeam(assembleTeam(newMatch, 1));
        checkTeam(assembleTeam(newMatch, 2));

        checkPlayer(assemblePlayer(newMatch));

        db.addMatch(newMatch, loggedUser);

        db.close();

    }


    private void checkTeam(Team team) {
        List<Team> teamResult = db.getTeam(team.getName(), loggedUser);
        if (teamResult.isEmpty())
            db.addTeam(team, loggedUser);

        else
            updateTeam(teamResult.get(0));

    }

    private void checkPlayer(Player player) {
        List<Player> playerResult = db.getPlayer(player.getName(), loggedUser);
        if (playerResult.isEmpty())
            db.addPlayer(player, loggedUser);
        else
            updatePlayer(playerResult.get(0));

    }

    private void updateTeam(Team team) {
        Stats newStats = new Stats();
        int gamesPlayedUp = team.getStats().getGamesPlayed();

        if (team.getName() == newMatch.getMyTeam()) {
            int goalsForUp = team.getStats().getGoalsFor() + newMatch.getGoalsFor();
            int goalsAgainstUp = team.getStats().getGoalsAgainst() + newMatch.getGoalsAgainst();
            int winsUp = team.getStats().getWins();
            int drawsUp = team.getStats().getDraws();
            int defeatsUp = team.getStats().getDefeats();

            if (newMatch.getGoalsFor() > newMatch.getGoalsAgainst())
                winsUp++;
            else if (newMatch.getGoalsFor() < newMatch.getGoalsAgainst())
                defeatsUp++;
            else
                drawsUp++;

            newStats.setGamesPlayed(gamesPlayedUp + 1);
            newStats.setWins(winsUp);
            newStats.setDraws(drawsUp);
            newStats.setDefeats(defeatsUp);
            newStats.setGoalsFor(goalsForUp);
            newStats.setGoalsAgainst(goalsAgainstUp);

        } else if (team.getName() == newMatch.getRivalTeam()) {
            int goalsForUp = team.getStats().getGoalsFor() + newMatch.getGoalsAgainst();
            int goalsAgainstUp = team.getStats().getGoalsAgainst() + newMatch.getGoalsFor();
            int winsUp = team.getStats().getWins();
            int drawsUp = team.getStats().getDraws();
            int defeatsUp = team.getStats().getDefeats();

            if (newMatch.getGoalsFor() < newMatch.getGoalsAgainst())
                winsUp++;
            else if (newMatch.getGoalsFor() > newMatch.getGoalsAgainst())
                defeatsUp++;
            else
                drawsUp++;

            newStats.setGamesPlayed(gamesPlayedUp + 1);
            newStats.setWins(winsUp);
            newStats.setDraws(drawsUp);
            newStats.setDefeats(defeatsUp);
            newStats.setGoalsFor(goalsForUp);
            newStats.setGoalsAgainst(goalsAgainstUp);
        } // TODO: Verificar para se foi para os pênaltis.

        team.setStats(newStats);
        db.updateTeamStats(team, loggedUser);
    }

    public void updatePlayer(Player player) {
        Stats newStats = new Stats();

        int gamesPlayedUp = player.getStats().getGamesPlayed();
        int winsUp = player.getStats().getWins();
        int drawsUp = player.getStats().getDraws();
        int defeatsUp = player.getStats().getDefeats();
        int goalsForUp = player.getStats().getGoalsFor() + newMatch.getGoalsAgainst();
        int goalsAgainstUp = player.getStats().getGoalsAgainst() + newMatch.getGoalsFor();

        if (newMatch.getGoalsAgainst() > newMatch.getGoalsFor())
            winsUp++;
        else if (newMatch.getGoalsAgainst() < newMatch.getGoalsFor())
            defeatsUp++;
        else
            drawsUp++;
        // TODO: Verificar para se foi para os pênaltis.

        newStats.setGamesPlayed(gamesPlayedUp + 1);
        newStats.setWins(winsUp);
        newStats.setDraws(drawsUp);
        newStats.setDefeats(defeatsUp);
        newStats.setGoalsFor(goalsForUp);
        newStats.setGoalsAgainst(goalsAgainstUp);

        player.setStats(newStats);
        db.updatePlayerStats(player, loggedUser);
    }

    private Player assemblePlayer(Match match) {
        Player player = new Player();
        player.setName(match.getRival());
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        stats.setWins(0);
        stats.setDraws(0);
        stats.setDefeats(0);
        stats.setGoalsFor(match.getGoalsAgainst());
        stats.setGoalsAgainst(match.getGoalsFor());

        if (match.getGoalsAgainst() > match.getGoalsFor())
            stats.setWins(1);
        else if (match.getGoalsAgainst() < match.getGoalsFor())
            stats.setDefeats(1);
        else
            stats.setDraws(1);
        // TODO: Verificar se foi para os pênaltis.

        player.setStats(stats);
        return player;
    }

    private Team assembleTeam(Match match, int homerOrAway) {
        Team team = new Team();
        if (homerOrAway == 1) {
            team.setName(match.getMyTeam());
            team.setBadgePath();
            Stats stats = new Stats();
            stats.setGamesPlayed(1);
            stats.setWins(0);
            stats.setDefeats(0);
            stats.setDraws(0);
            stats.setGoalsFor(match.getGoalsFor());
            stats.setGoalsAgainst(match.getGoalsAgainst());

            if (match.getGoalsFor() > match.getGoalsAgainst())
                stats.setWins(1);
            else if (match.getGoalsFor() < match.getGoalsAgainst())
                stats.setDefeats(1);
            else
                stats.setDraws(1);
            // TODO: Verificar se foi para os pênaltis.
            team.setStats(stats);
            return team;
        }

        team.setName(match.getRivalTeam());
        team.setBadgePath();
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        stats.setWins(0);
        stats.setDraws(0);
        stats.setDefeats(0);
        stats.setGoalsFor(match.getGoalsAgainst());
        stats.setGoalsAgainst(match.getGoalsFor());

        if (match.getGoalsAgainst() > match.getGoalsFor())
            stats.setWins(1);
        else if (match.getGoalsAgainst() < match.getGoalsFor())
            stats.setDefeats(1);
        else
            stats.setDraws(1);

        team.setStats(stats);
        return team;
    }

}