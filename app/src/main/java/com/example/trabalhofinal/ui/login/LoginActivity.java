package com.example.trabalhofinal.ui.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalhofinal.MainActivity;
import com.example.trabalhofinal.R;
import com.example.trabalhofinal.data.model.DBHelper;
import com.example.trabalhofinal.data.model.User;
import com.example.trabalhofinal.ui.home.HomeFragment;

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
                String noEmail = "Email incorreto.";
                if (!haveEmail(user)) {
                    System.out.println(edtUsername.getText().toString());
                    System.out.println(edtPassword.getText().toString());
                    System.out.println(user.getEmail());
                    Toast.makeText(getApplicationContext(), noEmail,
                            Toast.LENGTH_SHORT).show();
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

    /*private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }*/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openDialog() {
        myDialog.setContentView(R.layout.fragment_register);

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

                String differentPass = "Senhas não combinam.";
                String emailAlreadyRegistered = "Email já cadastrado.";
                String registerSuccess = "Conta cadastrada com sucesso!";
                String nullValues = "Preencha todos os campos.";

                String password = edtPasswordReg.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();

                System.out.println(password);
                System.out.println(confirmPassword);
                System.out.println(edtEmailReg.getText().toString());
                System.out.println(edtPasswordReg.getText().toString());
                System.out.println("new user " + newUser.getEmail());


                if (checkForEmptyValues(edtPasswordReg.getText().toString()) ||
                        checkForEmptyValues(edtEmailReg.getText().toString()) ||
                        checkForEmptyValues(edtUsernameReg.getText().toString()))
                Toast.makeText(
                        getApplicationContext(), nullValues, Toast.LENGTH_SHORT).show();
                else if (!password.equals(confirmPassword)) {
                    Toast.makeText(
                            getApplicationContext(), differentPass, Toast.LENGTH_SHORT).show();
                }
                else if (haveEmail(newUser))
                    Toast.makeText(getApplicationContext(), emailAlreadyRegistered,
                            Toast.LENGTH_SHORT).show();
                else {
                    addUser(newUser);
                    Toast.makeText(
                            getApplicationContext(), registerSuccess, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(
                    getApplicationContext(), "Bem-vindo " + verifyingUser.getUsername(),
                    Toast.LENGTH_SHORT).show();
            Intent it = new Intent(this, MainActivity.class);
            it.putExtra("user", (Serializable) verifyingUser);
            startActivity(it);
            finish();

        } else
            Toast.makeText(
                    getApplicationContext(), "Senha incorreta.",
                    Toast.LENGTH_SHORT).show();

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
    /*private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }*/

}