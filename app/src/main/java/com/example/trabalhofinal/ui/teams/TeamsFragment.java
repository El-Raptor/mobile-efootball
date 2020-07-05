package com.example.trabalhofinal.ui.teams;

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
import android.widget.ImageView;
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
import com.example.trabalhofinal.data.model.LoggedInUser;
import com.example.trabalhofinal.data.model.Team;
import com.example.trabalhofinal.data.model.User;

import java.util.List;

public class TeamsFragment extends Fragment {

    private View root;
    private TeamsViewModel teamsViewModel;
    private DBHelper db;
    private User loggedUser;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        teamsViewModel =
                ViewModelProviders.of(this).get(TeamsViewModel.class);
        root = inflater.inflate(R.layout.fragment_teams, container, false);
        loggedUser = ((MainActivity)getActivity()).getLoggedUser();

        System.out.println("Teams " + loggedUser.getEmail());

        init();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void init() {
        db = DBHelper.getInstance(getContext());
        List<Team> teams = db.getAllTeams(loggedUser);

        if (teams.isEmpty()) {
            ConstraintLayout constraintLayout = root.findViewById(R.id.clTeams);
            ConstraintSet set = new ConstraintSet();

            TextView emptyList = new TextView(getContext());
            emptyList.setText("Nenhum time encontrado!");
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
            initTable(teams, lp);
        }
    }

    // TODO: Refatorar (fazer classe responsável por criar tabela)
    private void initTable(List<Team> teams, TableRow.LayoutParams lp) {
        TableLayout tableLayout = root.findViewById(R.id.table_teams);

        // Cabeçalho da tabela.
        TableRow headers = new TableRow(getContext());
        headers.setLayoutParams(lp);
        headers.setBackgroundColor(Color.rgb(20,20,20));

        TextView label0 = new TextView(getContext());
        label0.setText("");
        label0.setBackgroundColor(Color.rgb(20, 20, 20));
        label0.setGravity(Gravity.CENTER);
        label0.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        label0.setPadding(10, 10,10,10);

        TextView label1 = new TextView(getContext());
        label1.setText("Time");
        label1.setTextColor(Color.WHITE);
        label1.setBackgroundColor(Color.rgb(20, 20, 20));
        label1.setGravity(Gravity.CENTER);
        label1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        label1.setPadding(10, 10,10,10);

        TextView label2 = new TextView(getContext());
        label2.setText("J");
        label2.setTextColor(Color.WHITE);
        label2.setBackgroundColor(Color.rgb(20, 20, 20));
        label2.setGravity(Gravity.CENTER);
        label2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        label2.setPadding(10, 10,10,10);

        TextView label3 = new TextView(getContext());
        label3.setText("V");
        label3.setTextColor(Color.WHITE);
        label3.setBackgroundColor(Color.rgb(20, 20, 20));
        label3.setGravity(Gravity.CENTER);
        label3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        label3.setPadding(10, 10,10,10);

        TextView label4 = new TextView(getContext());
        label4.setText("E");
        label4.setTextColor(Color.WHITE);
        label4.setBackgroundColor(Color.rgb(20, 20, 20));
        label4.setGravity(Gravity.CENTER);
        label4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        label4.setPadding(10, 10,10,10);

        TextView label5 = new TextView(getContext());
        label5.setText("D");
        label5.setTextColor(Color.WHITE);
        label5.setBackgroundColor(Color.rgb(20, 20, 20));
        label5.setGravity(Gravity.CENTER);
        label5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        label5.setPadding(10, 10,10,10);

        TextView label6 = new TextView(getContext());
        label6.setText("GP");
        label6.setTextColor(Color.WHITE);
        label6.setBackgroundColor(Color.rgb(20, 20, 20));
        label6.setGravity(Gravity.CENTER);
        label6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        label6.setPadding(10, 10,10,10);

        TextView label7 = new TextView(getContext());
        label7.setText("GC");
        label7.setTextColor(Color.WHITE);
        label7.setBackgroundColor(Color.rgb(20, 20, 20));
        label7.setGravity(Gravity.CENTER);
        label7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        label7.setPadding(10, 10,10,10);

        TextView label8 = new TextView(getContext());
        label8.setText("SG");
        label8.setTextColor(Color.WHITE);
        label8.setBackgroundColor(Color.rgb(20, 20, 20));
        label8.setGravity(Gravity.CENTER);
        label8.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        label8.setPadding(5, 5,5,5);

        TextView label9 = new TextView(getContext());
        label9.setText("App%");
        label9.setTextColor(Color.WHITE);
        label9.setBackgroundColor(Color.rgb(20, 20, 20));
        label9.setGravity(Gravity.CENTER);
        label9.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        label9.setPadding(5, 5,5,5);

        headers.addView(label0);
        headers.addView(label1);
        headers.addView(label2);
        headers.addView(label3);
        headers.addView(label4);
        headers.addView(label5);
        headers.addView(label6);
        headers.addView(label7);
        headers.addView(label8);
        headers.addView(label9);

        tableLayout.addView(headers);

        tableSetValues(teams, tableLayout, lp);
    }

    private Drawable getTeamBadge(String badgePath) {
        Resources res = getResources();
        System.out.println("team path " + badgePath);
        int resId = res.getIdentifier(badgePath, "drawable", getActivity().getPackageName());
        Drawable drawable = res.getDrawable(resId);
        return drawable;
    }

    private void tableSetValues(List<Team> teams, TableLayout tableLayout, TableRow.LayoutParams lp) {
        for (Team team : teams) {
            TableRow tableRow = new TableRow(getActivity());
            tableRow.setLayoutParams(lp);
            tableRow.setPadding(5, 5, 5, 5);
            tableRow.setBackgroundColor(Color.rgb(231, 231, 231));
            tableRow.setGravity(Gravity.CENTER);

            ImageView column0 = new ImageView(getActivity());
            String badgePath = team.getBadgePath();
            System.out.println(getTeamBadge(badgePath));
            System.out.println(badgePath);
            column0.setImageDrawable(getTeamBadge(badgePath));
            column0.setScaleType(ImageView.ScaleType.CENTER);
            column0.setAdjustViewBounds(true);
            column0.setMaxWidth(48);
            column0.setMaxHeight(48);
            //column0.setForegroundGravity(Gravity.CENTER);

            TextView column1 = new TextView(getActivity());
            column1.setText(team.getName());
            column1.setGravity(Gravity.CENTER);

            TextView column2 = new TextView(getContext());
            column2.setText(String.valueOf(team.getStats().getGamesPlayed()));
            column2.setGravity(Gravity.CENTER);

            TextView column3 = new TextView(getContext());
            column3.setText(String.valueOf(team.getStats().getWins()));
            column3.setGravity(Gravity.CENTER);

            TextView column4 = new TextView(getContext());
            column4.setText(String.valueOf(team.getStats().getDraws()));
            column4.setGravity(Gravity.CENTER);

            TextView column5 = new TextView(getContext());
            column5.setText(String.valueOf(team.getStats().getDefeats()));
            column5.setGravity(Gravity.CENTER);

            TextView column6 = new TextView(getContext());
            column6.setText(String.valueOf(team.getStats().getGoalsFor()));
            column6.setGravity(Gravity.CENTER);

            TextView column7 = new TextView(getContext());
            column7.setText(String.valueOf(team.getStats().getGoalsAgainst()));
            column7.setGravity(Gravity.CENTER);

            TextView column8 = new TextView(getContext());
            column8.setText(String.valueOf(team.getStats().goalsDifference()));
            column8.setGravity(Gravity.CENTER);

            TextView column9 = new TextView(getContext());
            column9.setText(String.valueOf(team.getStats().performanceRate()));
            column9.setGravity(Gravity.CENTER);

            tableRow.addView(column0);
            tableRow.addView(column1);
            tableRow.addView(column2);
            tableRow.addView(column3);
            tableRow.addView(column4);
            tableRow.addView(column5);
            tableRow.addView(column6);
            tableRow.addView(column7);
            tableRow.addView(column8);
            tableRow.addView(column9);

            tableLayout.addView(tableRow);
        }
    }


}