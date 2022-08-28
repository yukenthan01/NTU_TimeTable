package yuken.example.ntu_timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginActivity extends AppCompatActivity {
    Boolean isEmptyChecked,isPasswordChecked; // for valida email adn password
    EditText username,password;
    TextView errorTextView;
    Button btnLogin;
    Validations validations;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        validations = new Validations();

        btnLogin = findViewById(R.id.btnLogin);
        username = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        errorTextView = findViewById(R.id.invalidEmailPassword);

//        Login CLick event start for the login process
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEmptyChecked = validations.isEmpty(password);
                if(isEmptyChecked){
                    firebaseAuth.signInWithEmailAndPassword(username.getText().toString(),
                            password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            checkUserAccessLevel(authResult.getUser().getUid());
//                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this,"dsfsdfd",Toast.LENGTH_SHORT);
                            errorTextView.setText(e.getMessage().toString());
                            errorTextView.setVisibility(View.VISIBLE);
                        }
                    });
                }
               // Toast.makeText(LoginActivity.this,username.getText().toString(),
                        //Toast.LENGTH_SHORT);
            }
        });
    }
    public void checkUserAccessLevel(String uid) {
        Log.d("TAG123", "asd | " + uid);
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.getString("userRole").toString().equals("admin")){

                    Toast.makeText(LoginActivity.this,"Login Successfull",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AdminDashboard.class));
                    finish();
                }
                else if(documentSnapshot.getString("userRole") .equals("student")){
                    Toast.makeText(LoginActivity.this,"Login SuccessfullStudent",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),StudentDashboardActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}