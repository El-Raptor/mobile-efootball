package com.example.trabalhofinal.ui.players;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.trabalhofinal.controller.TablePlayer;
import com.example.trabalhofinal.controller.TableTeam;
import com.example.trabalhofinal.data.model.DBHelper;
import com.example.trabalhofinal.data.model.Player;
import com.example.trabalhofinal.data.model.User;

import java.util.List;

public class PlayersFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private View root;
    private DBHelper db;
    private User loggedUser;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);

        loggedUser = ((MainActivity)getActivity()).getLoggedUser();

        System.out.println("Players " + loggedUser.getEmail());
        root = inflater.inflate(R.layout.fragment_players, container, false);

        init();
        return root;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        db = DBHelper.getInstance(getContext());
        List<Player> players = db.getAllPlayers(loggedUser);

        if (players.isEmpty()) {
            ConstraintLayout constraintLayout = root.findViewById(R.id.clPlayer);
            ConstraintSet set = new ConstraintSet();

            TextView emptyList = new TextView(getContext());
            emptyList.setText("Nenhum jogador encontrado!");
            emptyList.setId(View.generateViewId());
            emptyList.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            constraintLayout.addView(emptyList, 0);

            set.clone(constraintLayout);
            // Adicionando as constraints layouts para o novo TextView.
            set.centerHorizontally(emptyList.getId(), constraintLayout.getId());
            set.centerVertically(emptyList.getId(), constraintLayout.getId());

            set.applyTo(constraintLayout);
        } else {
            TableLayout tableLayout = root.findViewById(R.id.table_players);
            TablePlayer tablePlayer = new TablePlayer(getActivity(), tableLayout);
            tablePlayer.initTable(players);
        }
    }

}