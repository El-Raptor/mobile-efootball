package com.example.trabalhofinal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalhofinal.data.model.DBHelper;
import com.example.trabalhofinal.data.model.User;
import com.example.trabalhofinal.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private User loggedUser;
    private DBHelper db;
    private Dialog editUserDialog;
    private EditText edtNewPass, edtConfirmNewPass, edtNewUsername;
    private final String NEGATIVE = "Negative";
    private final String POSITIVE = "Positive";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editUserDialog = new Dialog(this);

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

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_user:
                openDialog();
                return true;
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        loggedUser = null;
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /*private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        TextView emailHeader = findViewById(R.id.emailHeader);
        TextView usernameHeader = findViewById(R.id.usernameHeader);
        emailHeader.setText(loggedUser.getEmail());
        usernameHeader.setText(loggedUser.getUsername());
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public User getLoggedUser() {
        return loggedUser;
    }


    private void openDialog() {
        editUserDialog.setContentView(R.layout.dialog_edit_user);

        edtNewPass = editUserDialog.findViewById(R.id.edtNewPass);
        edtConfirmNewPass = editUserDialog.findViewById(R.id.edtConfirmNewPass);
        edtNewUsername = editUserDialog.findViewById(R.id.edtNewUsername);
        Button btnConfirmNewPassword = editUserDialog.findViewById(R.id.btnConfirmNewPassword);
        Button btnBack = editUserDialog.findViewById(R.id.btnBack);

        btnConfirmNewPassword.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String username = edtNewUsername.getText().toString();
                String pass = edtNewPass.getText().toString();
                String confirmNewPass = edtConfirmNewPass.getText().toString();

                final String PASSWORD_NOT_MATCHING = getResources()
                        .getString(R.string.message_passwords_not_matching);
                final String NULL_VALUES = getResources().getString(R.string.message_null_values);
                final String USERNAME_CHANGED = getResources()
                        .getString(R.string.message_username_changed);
                final String INVALID_PASSWORD = getResources().getString(R.string.message_invalid_password);

                User user = new User(username, pass, loggedUser.getEmail());

                if (checkForEmptyValues(pass) || checkForEmptyValues(username) ||
                        checkForEmptyValues(pass))
                    createToast(NULL_VALUES, NEGATIVE);

                else if (!pass.equals(confirmNewPass))
                    createToast(PASSWORD_NOT_MATCHING, NEGATIVE);
                else if (!checkUser(user))
                    createToast(INVALID_PASSWORD, NEGATIVE);
                else {
                    changeUsername(user);
                    loggedUser = user;
                    createToast(USERNAME_CHANGED, POSITIVE);
                    editUserDialog.dismiss();
                }
            }

        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserDialog.dismiss();
            }
        });
        editUserDialog.show();
    }

    private boolean checkUser(User user) {
        DBHelper db = DBHelper.getInstance(this);
        User userDB = db.getUser(user).get(0);
        db.close();

        return userDB.getPassword().equals(user.getPassword());
    }

    private boolean checkForEmptyValues(String field) {
        return field.equals("");
    }

    public void createToast(String text, String toastType) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = null;
        if (toastType.equals("Negative")) {
            layout = inflater.inflate(R.layout.custom_negative_toast,
                    (ViewGroup) findViewById(R.id.custom_negative_toast_container));
            TextView textMsg = layout.findViewById(R.id.tvNegativeToast);
            textMsg.setText(text);

        } else if (toastType.equals("Positive")) {
            layout = inflater.inflate(R.layout.custom_positive_toast,
                    (ViewGroup) findViewById(R.id.custom_positive_toast_container));
            TextView textMsg = layout.findViewById(R.id.tvPositiveToast);
            textMsg.setText(text);
        } else {
            layout = inflater.inflate(R.layout.custom_info_toast,
                    (ViewGroup) findViewById(R.id.custom_info_toast_container));
            TextView textMsg = layout.findViewById(R.id.tvInfoToast);
            textMsg.setText(text);
        }

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 10);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private void changeUsername(User user) {
        db = DBHelper.getInstance(getApplicationContext());
        db.updateUsername(user);
        db.close();
    }
}