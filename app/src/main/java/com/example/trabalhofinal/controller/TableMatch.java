package com.example.trabalhofinal.controller;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.trabalhofinal.R;
import com.example.trabalhofinal.data.model.Match;

import java.util.ArrayList;
import java.util.List;

public class TableMatch extends Table<Match> {

    private List<String> labels = new ArrayList<String>(){
        {
            add("Data");
            add("Adversário");
            add("T. Casa");
            add("Res.");
            add("T.Fora");
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TableMatch(Context context, TableLayout tl) {
        super(context, tl, 5);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initTable(List<Match> matches) {
        header(labels);
        dataRows(matches, super.getNumberOfColumns());
    }

    @Override
    protected void dataRows(List<Match> matches, int numberOfColumns) {
        List<TableRow> rows = new ArrayList<>();

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
        }

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


}
