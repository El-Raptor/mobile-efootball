package com.example.trabalhofinal.controller;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.trabalhofinal.R;
import com.example.trabalhofinal.data.model.Player;

import java.util.ArrayList;
import java.util.List;

public class TablePlayer extends Table<Player> {

    private List<String> labels = new ArrayList(){
        {
            add("Jogador");
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

    public TablePlayer(Context context, TableLayout tl) {
        super(context, tl, 9);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initTable(List<Player> player) {
        header(labels);
        dataRows(player, super.getNumberOfColumns());
    }

    @Override
    protected void dataRows(List<Player> players, int numberOfColumns) {
        List<TableRow> rows = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            rows.add(new TableRow(super.getContext()));
            rows.get(i).setLayoutParams(super.getLp());
            rows.get(i).setPadding(5, 15, 5, 15);
            if (i % 2 == 0)
                rows.get(i).setBackgroundColor(super.getResources().getColor(R.color.whisper_gray));
            else
                rows.get(i).setBackgroundColor(super.getResources().getColor(R.color.gray));
            rows.get(i).setGravity(Gravity.CENTER);
            setValues(rows.get(i), players.get(i), numberOfColumns);
            getTl().addView(rows.get(i));
        }
    }

    @Override
    protected void setValues(TableRow row, Player player, int numberOfColumns) {
        List<TextView> values = new ArrayList<>();

        for (int i = 0; i < numberOfColumns; i++) {
            values.add(new TextView(super.getContext()));
            values.get(i).setGravity(Gravity.CENTER);
            values.get(i).setText(getValues(player.toString())[i]);

            row.addView(values.get(i));
        }
    }
}
