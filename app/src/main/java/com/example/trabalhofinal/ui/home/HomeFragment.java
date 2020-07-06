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
import com.example.trabalhofinal.controller.Table;
import com.example.trabalhofinal.controller.TableMatch;
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
                addMatch(penMatch);
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
                addMatch = assembleMatch();
                Integer goalsFor = Integer.parseInt(edtGoalsFor.getText().toString());
                Integer goalsAgainst = Integer.parseInt(edtGoalsAgainst.getText().toString());
                if (goalsFor == goalsAgainst)
                    openPenaltiesDialog(addMatch);
                else
                    addMatch(addMatch);
                newMatchDialog.dismiss();
            }
        });

        newMatchDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        db = DBHelper.getInstance(getContext());
        List<Player> players = db.getAllPlayers(loggedUser);

        List<Match> matches = db.getAllMatches(loggedUser);

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
            TableLayout tableLayout = root.findViewById(R.id.table_matches);
            TableMatch tableMatch = new TableMatch(getActivity(), tableLayout);
            tableMatch.initTable(matches);
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