package com.example.trabalhofinal.ui.players;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import com.example.trabalhofinal.controller.TablePlayer;
import com.example.trabalhofinal.controller.TableTeam;
import com.example.trabalhofinal.data.model.DBHelper;
import com.example.trabalhofinal.data.model.Player;
import com.example.trabalhofinal.data.model.User;

import org.w3c.dom.Text;

import java.util.List;

public class PlayersFragment extends Fragment {

    private View root;
    private DBHelper db;
    private User loggedUser;
    private TextView tvNoPlayer;
    private ImageView imgNoPlayer;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        loggedUser = ((MainActivity)getActivity()).getLoggedUser();

        System.out.println("Players " + loggedUser.getEmail());
        root = inflater.inflate(R.layout.fragment_players, container, false);
        tvNoPlayer = root.findViewById(R.id.tvNoPlayer);

        init();
        return root;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        db = DBHelper.getInstance(getContext());
        List<Player> players = db.getAllPlayers(loggedUser);

        if (players.isEmpty()) {
            tvNoPlayer = root.findViewById(R.id.tvNoPlayer);
            imgNoPlayer = root.findViewById(R.id.imgNoPlayer);
            imgNoPlayer.setImageDrawable(getDrawable("ic_no_player"));
            tvNoPlayer.setText(getResources().getString(R.string.label_no_found_player));
        } else {
            TableLayout tableLayout = root.findViewById(R.id.table_players);
            TablePlayer tablePlayer = new TablePlayer(getActivity(), tableLayout);
            tablePlayer.initTable(players);
        }
    }

    private Drawable getDrawable(String drawableName) {
        Resources res = getResources();
        int resId = res.getIdentifier(drawableName, "drawable", getContext().getPackageName());
        Drawable drawable = res.getDrawable(resId);
        return drawable;
    }

}