package yuken.example.ntu_timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    Boolean isEmailChecked,isPasswordChecked; // for valida email adn password
    EditText email,password;
    TextView errorTextView;
    Button btnLogin;
    Validations validations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        validations = new Validations();
        btnLogin = findViewById(R.id.btnLogin);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        errorTextView = findViewById(R.id.invalidEmailPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEmailChecked = validations.isValidEmail(email);
                //isPasswordChecked = validations.isValidPassword(password);
                if(isEmailChecked){

                }
            }
        });
    }
}