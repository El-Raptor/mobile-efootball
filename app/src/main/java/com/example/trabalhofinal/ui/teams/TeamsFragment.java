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
import com.example.trabalhofinal.controller.TableMatch;
import com.example.trabalhofinal.controller.TableTeam;
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
            TableLayout tableLayout = root.findViewById(R.id.table_teams);
            TableTeam tableTeam = new TableTeam(getActivity(), tableLayout);
            tableTeam.initTable(teams);
        }
    }



}