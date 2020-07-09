package com.example.trabalhofinal.ui.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalhofinal.MainActivity;
import com.example.trabalhofinal.R;
import com.example.trabalhofinal.data.model.DBHelper;
import com.example.trabalhofinal.data.model.User;

import java.io.Serializable;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin, btnRegister;
    private EditText edtUsername, edtPassword;
    private EditText edtEmailReg, edtUsernameReg, edtPasswordReg, edtConfirmPassword;
    private DBHelper db;
    private Dialog myDialog;
    private TextView tvRegister, tvIsMember;
    private User newUser;
    private final String NEGATIVE = "Negative";
    private final String POSITIVE = "Positive";
    private final String INFO = "Info";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtUsername = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPasswordReg);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        myDialog = new Dialog(this);

        // Botão de Entrar.
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = createUser();
                final String NO_EMAIL = getResources().getString(R.string.message_invalid_email);
                if (!haveEmail(user)) {
                    createToast(NO_EMAIL, NEGATIVE);
                }
                else {
                    logIn(user);
                }
            }
        });


        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openDialog() {
        myDialog.setContentView(R.layout.dialog_register);

        edtUsernameReg = myDialog.findViewById(R.id.edtUsernameReg);
        edtEmailReg = myDialog.findViewById(R.id.edtEmailReg);
        edtPasswordReg = myDialog.findViewById(R.id.edtPasswordReg);
        edtConfirmPassword = myDialog.findViewById(R.id.edtConfirmPass);
        btnRegister = myDialog.findViewById(R.id.btnRegister);
        tvIsMember = myDialog.findViewById(R.id.tvIsMember);

        tvIsMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass = edtPasswordReg.getText().toString();
                String email = edtEmailReg.getText().toString();
                String username = edtUsernameReg.getText().toString();

                newUser = new User(username, pass, email);

                final String PASSWORD_NOT_MATCHING = getResources()
                        .getString(R.string.message_passwords_not_matching);
                final String EMAIL_ALREADY_REGISTERED = getResources()
                        .getString(R.string.message_email_already_registered);
                final String REGISTER_SUCCESS = getResources()
                        .getString(R.string.message_register_success);
                final String NULL_VALUES = getResources().getString(R.string.message_null_values);

                String password = edtPasswordReg.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();

                if (checkForEmptyValues(edtPasswordReg.getText().toString()) ||
                        checkForEmptyValues(edtEmailReg.getText().toString()) ||
                        checkForEmptyValues(edtUsernameReg.getText().toString()))
                    createToast(NULL_VALUES, NEGATIVE);

                else if (!password.equals(confirmPassword))
                    createToast(PASSWORD_NOT_MATCHING, NEGATIVE);

                else if (haveEmail(newUser))
                    createToast(EMAIL_ALREADY_REGISTERED, NEGATIVE);
                else {
                    addUser(newUser);
                    createToast(REGISTER_SUCCESS, POSITIVE);
                    myDialog.dismiss();
                }
            }
        });

        myDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void logIn(User user) {
        db = DBHelper.getInstance(getApplicationContext());

        User verifyingUser = db.getUser(user).get(0);

        if (checkUser(user)) {
            final String WELCOME = getResources().getString(R.string.message_welcome);
            createToast(WELCOME, INFO + user.getUsername() + "!");
            Intent it = new Intent(this, MainActivity.class);
            it.putExtra("user", (Serializable) verifyingUser);
            startActivity(it);
            finish();

        } else {
            final String INVALID_PASSWORD = getResources().getString(R.string.message_invalid_password);
            createToast(INVALID_PASSWORD, NEGATIVE);
        }
        db.close();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkUser(User user) {

        User userDB = db.getUser(user).get(0);

        System.out.println(userDB.getPassword());
        System.out.println(user.getPassword());

        return userDB.getPassword().equals(user.getPassword());
    }


    private boolean checkForEmptyValues(String field) {
        return field.equals("");
    }

    private boolean haveEmail(User user) {
        db = DBHelper.getInstance(getApplicationContext());
        List<User> users = db.getUser(user);
        db.close();

        for (User u : users)
            System.out.println("Have Email " + u.getEmail());

        return !users.isEmpty();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private User createUser() {
        String email = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        User user = new User(password, email);

        return user;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private User createUserReg() {
        String email = edtEmailReg.getText().toString();
        String username = edtUsernameReg.getText().toString();
        String password = edtPasswordReg.getText().toString();

        User user = new User(username, password, email);

        return user;
    }

    private void addUser(User user) {
        db = DBHelper.getInstance(getApplicationContext());
        db.addUser(user);
        System.out.println("Add " + user.getEmail());
        db.close();
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

}