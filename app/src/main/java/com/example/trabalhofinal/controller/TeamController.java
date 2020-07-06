package com.example.trabalhofinal.controller;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.example.trabalhofinal.data.model.Team;

public class TeamController {

    private Team team;
    private Context context;

    public TeamController(Context context, Team team) {
        this.team = team;
        this.context = context;
    }

    public Drawable getTeamBadge(String badgePath) {
        Resources res = context.getResources();
        int resId = res.getIdentifier(badgePath, "drawable", context.getPackageName());
        Drawable drawable = res.getDrawable(resId);
        return drawable;
    }
}
