package com.example.trabalhofinal.controller;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.trabalhofinal.R;
import com.example.trabalhofinal.data.model.Match;

import java.util.ArrayList;
import java.util.List;

public abstract class Table<E> {

    private TableLayout tl;
    private Context context;
    private Resources resources;
    private int numberOfColumns;
    private TableRow.LayoutParams lp;

    public Table(Context context, TableLayout tl, int numberOfColumns) {
        this.context = context;
        this.resources = context.getResources();
        this.lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        this.tl = tl;
        this.numberOfColumns = numberOfColumns;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void header(List<String> labels) {
        TableRow headers = new TableRow(context);
        headers.setLayoutParams(lp);
        headers.setBackground(resources.getDrawable(R.drawable.table_style));
        headers.setElevation(2);
        List<TextView> columnLabels = new ArrayList<>();
        for (int i=0; i < numberOfColumns; i++) {
            columnLabels.add(new TextView(context));
            TextView thisLabel = columnLabels.get(i);
            columnLabels.get(i).setText(labels.get(i));
            columnLabels.get(i).setTextColor(resources.getColor(R.color.white));
            columnLabels.get(i).setGravity(Gravity.CENTER);
            columnLabels.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            columnLabels.get(i).setPadding(10, 10,10,10);

            headers.addView(columnLabels.get(i));
        }
        tl.addView(headers);
    }

    abstract public void initTable(List<E> e);

    abstract protected void dataRows(List<E> e, int numberOfColumns);

    abstract protected void setValues(TableRow row, E e, int numberOfColumns);

    protected String[] getValues(String values) {
        return values.split(",");
    }

    public TableLayout getTl() {
        return tl;
    }

    public void setTl(TableLayout tl) {
        this.tl = tl;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public TableRow.LayoutParams getLp() {
        return lp;
    }

    public void setLp(TableRow.LayoutParams lp) {
        this.lp = lp;
    }
}
