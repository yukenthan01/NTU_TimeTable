package yuken.example.ntu_timetable;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class UserRegistrationFragment extends Fragment {
    View view;
    private TextInputEditText firstname,lastname,email,batchId,universityId;
    private AutoCompleteTextView userRole,degree,course;
    private String [] userRoles = {"Lecturer","Student"};
    private String [] degrees = {"UG","PG","PhD"};
    private Button btnSubmit;
    private ArrayAdapter<String> userRoleAdapter,degreeAdapter,courseAdapter;
    Validations validations;
    TextInputLayout degreeText,courseText,batchText;
    boolean isEmptyCheckedFirstname,isEmptyCheckedLastname,isEmptyCheckedEmail,
            isEmptyCheckedBatchId,isEmptyCheckedUniversityId;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    List<String> courses;
    DataSeeds dataSeeds = new DataSeeds();
    UserModel userModel;
    String userId;
    private Random random;
    final String username = "yukenthan93@gmail.com";
    final String mailPassword = "wrfobhexrjrqdoyd";

    public UserRegistrationFragment() {
        // Required empty public constructor

    }
    // TODO: Rename and change types and number of parameters
    public static UserRegistrationFragment newInstance(String param1, String param2) {
        UserRegistrationFragment fragment = new UserRegistrationFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        view =  inflater.inflate(R.layout.fragment_user_registration, container, false);
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


        // Set the user roles in to the adapter
        userRoleAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,userRoles);
        userRole.setAdapter(userRoleAdapter);

        degreeAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,degrees);
        degree.setAdapter(degreeAdapter);

        userRole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(userRole.getText().toString().equals("Lecturer"))
                {
                    degree.setText("-");
                    degreeText.setVisibility(View.GONE);
                    course.setText("-");
                    courseText.setVisibility(View.GONE);
                    batchId.setText("-");
                    batchText.setVisibility(View.GONE);

                }
                else{
                    degree.setText("");
                    degreeText.setVisibility(View.VISIBLE);
                    course.setText("");
                    courseText.setVisibility(View.VISIBLE);
                    batchId.setText("");
                    batchText.setVisibility(View.VISIBLE);
                }

            }
        });
        degree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selecteCourses(degree.getText().toString());
                degreeAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,courses);
                course.setAdapter(degreeAdapter);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUsers();
                //dummyInsertTablesValues();
            }
        });
        return view;
    }
    private void saveUsers() {
        validations = new Validations();
        isEmptyCheckedFirstname = validations.isEmpty(firstname);
        isEmptyCheckedLastname = validations.isEmpty(lastname);
        isEmptyCheckedEmail = validations.isEmpty(email);
        isEmptyCheckedBatchId = validations.isEmpty(batchId);
        isEmptyCheckedUniversityId = validations.isEmpty(universityId);


        if (isEmptyCheckedFirstname && isEmptyCheckedLastname && isEmptyCheckedEmail && isEmptyCheckedBatchId && isEmptyCheckedUniversityId) {
            if(userRole.getText().toString().equals("Lecturer")) {

                userModel = new UserModel(
                        "",
                        email.getText().toString(),
                        firstname.getText().toString(),
                        lastname.getText().toString(),
                        "active",
                        universityId.getText().toString(),
                        userRole.getText().toString().toLowerCase(),
                        ""
                );
                random = new Random();
                String randomPassword = String.format("%07d", random.nextInt(10000));
                //firebase store the details
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                        randomPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        userId = firebaseAuth.getCurrentUser().getUid();

                        DocumentReference documentReference = firebaseFirestore.collection(
                                "users").document(userId);
                        documentReference.set(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                passwordSentToMail(email.getText().toString(), randomPassword.toString());
                                Toast.makeText(getActivity(), "User Added Successfully",
                                        Toast.LENGTH_SHORT).show();
                                reloadFragment(new UserRegistrationFragment());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "onSuccess: " + e.getMessage());
                                Toast.makeText(getActivity(), e.getMessage().toString(),
                                        Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage().toString(),
                                Toast.LENGTH_LONG).show();

                    }
                });

            }
            else {
                dataSeeds.getFiledValue(new DataSeeds.getTheValueByFieldCallBack() {
                    @Override
                    public void onCallback(String fieldValues) {

                        userModel = new UserModel(
                                batchId.getText().toString(),
                                email.getText().toString(),
                                firstname.getText().toString(),
                                lastname.getText().toString(),
                                "active",
                                universityId.getText().toString(),
                                userRole.getText().toString().toLowerCase(),
                                fieldValues.toString()

                        );
                        random = new Random();
                        String randomPassword = String.format("%07d", random.nextInt(10000));
                        //firebase store the details
                        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                                randomPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                userId = firebaseAuth.getCurrentUser().getUid();

                                DocumentReference documentReference = firebaseFirestore.collection(
                                        "users").document(userId);
                                documentReference.set(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        passwordSentToMail(email.getText().toString(), randomPassword.toString());
                                        Toast.makeText(getActivity(), "User Added Successfully",
                                                Toast.LENGTH_SHORT).show();
                                        reloadFragment(new UserRegistrationFragment());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "onSuccess: " + e.getMessage());
                                        Toast.makeText(getActivity(), e.getMessage().toString(),
                                                Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage().toString(),
                                        Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                }, "course", "course", course.getText().toString());
            }


        } else {
//            errorView.setText("Please Fill in the blanks!!");
//            errorView.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Please Fill in the blanks!!", Toast.LENGTH_LONG).show();
        }
    }
    public void selecteCourses(String degreeLevel){
        courses = new ArrayList<String>();
        firebaseFirestore.collection("course").whereEqualTo("degreeLevel",degreeLevel).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        courses.add(document.getString("course").toString());
                    }
                } else {
                    Log.d("spein", "Error getting documents: ", task.getException());
                }
            }
        });

    }
    private void reloadFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
    // Dummy Insert values
    public void dummyInsertTablesValues()
    {
        Map<String,Object> dummy = new HashMap<>();
        dummy.put("batchId","-");
        dummy.put("email","student@gmail.com");
        dummy.put("firstname","Student");
        dummy.put("lastname","Student");
        dummy.put("status","active");
        dummy.put("universityId","N000000");
        dummy.put("student@gmail.com","student");


        firebaseFirestore.collection("courseModule").add(dummy).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Log.d("dummy", "onSuccess: User Added Successfully"+documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onSuccess: "+e.getMessage());
            }
        });
    }
    public void passwordSentToMail(String email,String password)
    {
        Properties props = new Properties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port","587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, mailPassword);
            }
        });
        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
            message.setSubject("New Password");

            message.setText("New Password - " + password);
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Transport.send(message);
        }
        catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
    }
}