package com.example.trabalhofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.trabalhofinal.data.model.User;
import com.example.trabalhofinal.ui.home.HomeFragment;
import com.example.trabalhofinal.ui.login.LoginActivity;
import com.example.trabalhofinal.ui.players.PlayersFragment;
import com.example.trabalhofinal.ui.teams.TeamsFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private User loggedUser;
    private TextView emailHeader, usernameHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loggedUser = (User) getIntent().getSerializableExtra("user");

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_team, R.id.nav_players)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment newFragment;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                int id = item.getItemId();

                if (id == R.id.nav_logout)
                    logOut();
                else if (id == R.id.nav_home) {
                    newFragment = new HomeFragment();
                    transaction.replace(R.id.nav_host_fragment, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    drawer.closeDrawers();
                } else if (id == R.id.nav_players) {
                    newFragment = new PlayersFragment();
                    transaction.replace(R.id.nav_host_fragment, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    drawer.closeDrawers();
                } else if (id == R.id.nav_team) {
                    newFragment = new TeamsFragment();
                    transaction.replace(R.id.nav_host_fragment, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    drawer.closeDrawers();
                }

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        emailHeader = findViewById(R.id.emailHeader);
        usernameHeader = findViewById(R.id.usernameHeader);

        emailHeader.setText(loggedUser.getEmail());
        usernameHeader.setText(loggedUser.getUsername());
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void logOut() {
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
    }

    public User getLoggedUser() {
        return loggedUser;
    }
}