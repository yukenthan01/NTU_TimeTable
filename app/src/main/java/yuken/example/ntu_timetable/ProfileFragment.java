package yuken.example.ntu_timetable;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    View view;
    String TAG = "Profile";
    boolean isEmptyCheckedFirstname,isEmptyCheckedLastname,isEmptyCheckedEmail,
            isEmptyCheckedBatchId,isEmptyCheckedUniversityId;
    Validations validations;
    private String [] degrees = {"UG","PG","PhD"};
    TextInputLayout degreeText,courseText,batchText;
    private TextInputEditText firstname,lastname,email,batchId,universityId;
    private AutoCompleteTextView userRole,degree,course;
    Button btnSubmit;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    List<String> courses;
    DataSeeds dataSeeds = new DataSeeds();
    String uid ;
    private ArrayAdapter<String> userRoleAdapter,degreeAdapter,courseAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        view =  inflater.inflate(R.layout.fragment_profile, container, false);
        uid = firebaseAuth.getCurrentUser().getUid();

        batchId = view.findViewById(R.id.batchId);
        email = view.findViewById(R.id.email);
        firstname = view.findViewById(R.id.firstname);
        lastname = view.findViewById(R.id.lastname);
        universityId = view.findViewById(R.id.universityId);
        userRole = view.findViewById(R.id.userRole);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        degree = view.findViewById(R.id.degreeLevel);
        course = view.findViewById(R.id.course);
        degreeText = view.findViewById(R.id.degreeLevelText);
        courseText = view.findViewById(R.id.courseText);
        batchText = view.findViewById(R.id.batchIdText);
        getUserDetails();


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
        return view;
    }
    private void updateProfile(){
        validations = new Validations();
        isEmptyCheckedFirstname = validations.isEmpty(firstname);
        isEmptyCheckedLastname = validations.isEmpty(lastname);
        isEmptyCheckedEmail = validations.isEmpty(email);
        isEmptyCheckedBatchId = validations.isEmpty(batchId);
        isEmptyCheckedUniversityId = validations.isEmpty(universityId);

        firebaseFirestore.collection("users").document(uid).update(
                        "firstname",firstname.getText().toString(),
                        "lastname",lastname.getText().toString()
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(),"Updated successful",Toast.LENGTH_SHORT).show();
                        reloadFragment(new ProfileFragment());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getUserDetails() {

        DocumentReference documentReference = firebaseFirestore.collection("users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.getString("userRole").toString().equals("student"))
                {
                    degree.setText("");
                    degreeText.setVisibility(View.VISIBLE);
                    course.setText("");
                    courseText.setVisibility(View.VISIBLE);
                    batchId.setText("");
                    batchText.setVisibility(View.VISIBLE);

                    batchId.setText(documentSnapshot.getString("batchId").toString());
                    email.setText(documentSnapshot.getString("email").toString());
                    firstname.setText(documentSnapshot.getString("firstname").toString());
                    lastname.setText(documentSnapshot.getString("lastname").toString());
                    universityId.setText(documentSnapshot.getString("universityId").toString());
                    degree.setText(documentSnapshot.getString("degreeLevel").toString());
                    dataSeeds.getValueByField(new DataSeeds.getValueCallback() {
                        @Override
                        public void onCallback(String fieldValues) {
                            course.setText(fieldValues);
                        }
                    },"course","course",documentSnapshot.getString("courseId").toString());


                }
                else if(documentSnapshot.getString("userRole").toString().equals("lecturer"))
                {
                    degree.setText("-");
                    degreeText.setVisibility(View.GONE);
                    course.setText("-");
                    courseText.setVisibility(View.GONE);
                    batchId.setText("-");
                    batchText.setVisibility(View.GONE);

                    email.setText(documentSnapshot.getString("email").toString());
                    firstname.setText(documentSnapshot.getString("firstname").toString());
                    lastname.setText(documentSnapshot.getString("lastname").toString());
                    universityId.setText(documentSnapshot.getString("universityId").toString());

                }




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAGfaill", "onFailure: "+e.getMessage().toString());
            }
        });
    }
    private void reloadFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

}