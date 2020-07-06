package com.example.trabalhofinal.controller;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.trabalhofinal.R;
import com.example.trabalhofinal.data.model.Team;

import java.util.ArrayList;
import java.util.List;

public class TableTeam extends Table<Team> {

    private TeamController controller;
    private List<String> labels = new ArrayList<String>(){
        {
            add("");
            add("Time");
            add("J");
            add("V");
            add("E");
            add("D");
            add("GP");
            add("GC");
            add("SG");
            add("App%");
        }
    };

    public TableTeam (Context context, TableLayout tl) {
        super(context, tl, 10);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initTable(List<Team> teams) {
        header(labels);
        dataRows(teams, super.getNumberOfColumns());
    }

    @Override
    protected void dataRows(List<Team> teams, int numberOfColumns) {
        List<TableRow> rows = new ArrayList<>();

        for (int i = 0; i < teams.size(); i++) {
            rows.add(new TableRow(super.getContext()));
            rows.get(i).setLayoutParams(super.getLp());
            rows.get(i).setPadding(5, 15, 5, 15);
            if (i % 2 == 0)
                rows.get(i).setBackgroundColor(super.getResources().getColor(R.color.whisper_gray));
            else
                rows.get(i).setBackgroundColor(super.getResources().getColor(R.color.gray));
            rows.get(i).setGravity(Gravity.CENTER);
            setValues(rows.get(i), teams.get(i), numberOfColumns);
            getTl().addView(rows.get(i));
        }
    }

    @Override
    protected void setValues(TableRow row, Team team, int numberOfColumns) {

        controller = new TeamController(getContext(), team);
        ImageView column0 = new ImageView(getContext());
        column0.setImageDrawable(controller.getTeamBadge(team.getBadgePath()));
        column0.setScaleType(ImageView.ScaleType.CENTER);
        column0.setAdjustViewBounds(true);
        column0.setMaxWidth(48);
        column0.setMaxHeight(48);

        row.addView(column0);

        List<TextView> values = new ArrayList<>();

        for (int i = 1; i < numberOfColumns; i++) {
            values.add(new TextView(super.getContext()));
            values.get(i).setGravity(Gravity.CENTER);
            values.get(i).setText(getValues(team.toString())[i]);

            row.addView(values.get(i));
        }
    }

}
