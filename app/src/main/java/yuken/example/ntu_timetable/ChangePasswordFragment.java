package yuken.example.ntu_timetable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {
    View view;
    Boolean isNewPasswordChecked,isCurrentPasswordChecked;
    TextView errorTextView;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private TextInputEditText newPassword,currentPassword;
    Button btnUpdate;
    Validations validations = new Validations();
    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
      
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_password, container, false);

        newPassword = view.findViewById(R.id.newPassword);
        currentPassword = view.findViewById(R.id.currentPassword);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        errorTextView = view.findViewById(R.id.errorView);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
        return view;
    }

    private void changePassword() {
        String email  = firebaseAuth.getCurrentUser().getEmail();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        isCurrentPasswordChecked = validations.isEmptyGoogle(currentPassword);
        isNewPasswordChecked = validations.isEmptyGoogle(newPassword);

        if(isCurrentPasswordChecked && isCurrentPasswordChecked){

            firebaseAuth.signInWithEmailAndPassword(email,
                    currentPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Do you confirm change the password?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    user.updatePassword(newPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(),
                                                    "Password Rest Successfully".toString(),
                                                    Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().signOut();
                                            Intent intent = new Intent(getContext(),
                                                    LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    errorTextView.setText("Current Password NOT matched");
                    errorTextView.setVisibility(View.VISIBLE);
                }
            });
        }
        else {
            errorTextView.setText("Please Fill the data");
            errorTextView.setVisibility(View.VISIBLE);
        }
    }
}