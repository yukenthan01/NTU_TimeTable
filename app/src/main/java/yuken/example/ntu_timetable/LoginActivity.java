package yuken.example.ntu_timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.GeneralSecurityException;

public class LoginActivity extends AppCompatActivity {
    Boolean isEmptyChecked,isPasswordChecked; // for valida email adn password
    EditText username,password;
    TextView errorTextView;
    Button btnLogin;
    Validations validations;
    private LoginDataLayer loginDataLayer;
    public static DatabaseConnection databaseConnection;
    private String userRole;
    String loginName, loginRole = null,result = "false";
    private SharedPreferences sharedPreferences; // login validation daily use
    private static final String SHARED_PRE_NAME = "myPreference";
    private static final String KEY_NAME = "username";
    private static final String KEY_ROLE = "userType";

    private DataSeeds dataSeeds ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        validations = new Validations();
        dataSeeds = new DataSeeds();
        loginDataLayer = new LoginDataLayer();

        btnLogin = findViewById(R.id.btnLogin);
        username = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        errorTextView = findViewById(R.id.invalidEmailPassword);
        // first time data enter by manual
        try {
            dataSeeds.adminAccountCreation(this);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
//        Login CLick event start for the login process
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEmptyChecked = validations.isEmpty(password);

                if(isEmptyChecked){
//                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                    finish();
                    String result = "false";
                    try {
                        result = loginDataLayer.login(username.getText().toString(),
                                password.getText().toString(),LoginActivity.this);

//                        SharedPreferences.Editor editor=sharedPreferences.edit();
//                        editor.putString(KEY_NAME,username.getEditText().getText().toString());
//                        editor.putString(KEY_ROLE,result);
//                        editor.apply();

//                        selectDashboard(result);
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                }
               // Toast.makeText(LoginActivity.this,username.getText().toString(),
                        //Toast.LENGTH_SHORT);
            }
        });
    }
}