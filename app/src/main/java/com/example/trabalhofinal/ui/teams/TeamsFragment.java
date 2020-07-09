package com.example.trabalhofinal.ui.teams;

import android.content.res.Resources;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.trabalhofinal.MainActivity;
import com.example.trabalhofinal.R;
import com.example.trabalhofinal.controller.TableTeam;
import com.example.trabalhofinal.data.model.DBHelper;
import com.example.trabalhofinal.data.model.Team;
import com.example.trabalhofinal.data.model.User;

import java.util.List;

public class TeamsFragment extends Fragment {

    private View root;
    private DBHelper db;
    private User loggedUser;
    private TextView tvNoTeam;
    private ImageView imgNoTeam;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_teams, container, false);
        loggedUser = ((MainActivity)getActivity()).getLoggedUser();

        System.out.println("Teams " + loggedUser.getEmail());
        tvNoTeam = root.findViewById(R.id.tvNoTeam);

        init();

        return root;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        db = DBHelper.getInstance(getContext());
        List<Team> teams = db.getAllTeams(loggedUser);

        if (teams.isEmpty()) {
            tvNoTeam = root.findViewById(R.id.tvNoTeam);
            imgNoTeam = root.findViewById(R.id.imgNoTeam);
            imgNoTeam.setImageDrawable(getDrawable("ic_no_team"));
            tvNoTeam.setText(getResources().getString(R.string.label_no_found_team));
        } else {
            TableLayout tableLayout = root.findViewById(R.id.table_teams);
            TableTeam tableTeam = new TableTeam(getActivity(), tableLayout);
            tableTeam.initTable(teams);
        }
    }

    private Drawable getDrawable(String drawableName) {
        Resources res = getResources();
        int resId = res.getIdentifier(drawableName, "drawable", getContext().getPackageName());
        Drawable drawable = res.getDrawable(resId);
        return drawable;
    }

}