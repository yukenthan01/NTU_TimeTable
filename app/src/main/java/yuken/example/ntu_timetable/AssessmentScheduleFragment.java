package yuken.example.ntu_timetable;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AssessmentScheduleFragment extends Fragment {
    View view;
    List<String> lecturerModuleId = new ArrayList<>() ;
    TextInputEditText credits,publishedDate,publishedTime,dueDate,dueTime;
    private ArrayAdapter<String> moduleArrayAdapter;
    private AutoCompleteTextView moduleId;
    private Button btnSubmit;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    List<TextInputEditText> isEmptyCheck;
    Validations validations =  new Validations();
    private int year, month, dayDate, hour, minute;
    Calendar calendar;
    DataSeeds dataSeeds = new DataSeeds();
    String mo_moduleId,mo_credits,mo_publishedDate,mo_publishedTime,mo_dueDate,mo_dueTime,
    mo_status,mo_lecturerId,mo_DocumentId;
    public AssessmentScheduleFragment() {
        // Required empty public constructor
    }
    public AssessmentScheduleFragment(AssessmentModule assessmentModule,String documentId) {
        mo_moduleId = assessmentModule.getModuleId();
        mo_credits = assessmentModule.getCredits();
        mo_publishedDate = assessmentModule.getPublishedDate();
        mo_publishedTime = assessmentModule.getPublishedTime();
        mo_dueDate = assessmentModule.getDueDate();
        mo_dueTime = assessmentModule.dueTime;
        mo_lecturerId = assessmentModule.getLecturerId();
        mo_status = assessmentModule.getStatus();
        mo_DocumentId = documentId;
    }
    // TODO: Rename and change types and number of parameters
    public static AssessmentScheduleFragment newInstance(String param1, String param2) {
        AssessmentScheduleFragment fragment = new AssessmentScheduleFragment();
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

        view =  inflater.inflate(R.layout.fragment_assessment_schedule, container, false);
        moduleId = view.findViewById(R.id.module);
        credits = view.findViewById(R.id.credit);
        publishedDate = view.findViewById(R.id.publishedDate);
        publishedTime = view.findViewById(R.id.publishTime);
        dueDate = view.findViewById(R.id.dueDate);
        dueTime = view.findViewById(R.id.dueTime);

        btnSubmit = view.findViewById(R.id.btnSubmit);
        getModuleName();

        moduleArrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,lecturerModuleId);
        moduleId.setAdapter(moduleArrayAdapter);
        if(mo_DocumentId !="" )
        {
            credits.setText(mo_credits);
            publishedDate.setText(mo_publishedTime);
            publishedTime.setText(mo_publishedTime);
            dueDate.setText(mo_dueDate);
            dueTime.setText(mo_dueTime);
            dataSeeds.getValueByField(new DataSeeds.getValueCallback() {
                @Override
                public void onCallback(String fieldValues) {
                    moduleId.setText(fieldValues);
                    //batchNumber.setAdapter(batchArrayAdapter);
                }
            },"module","module",mo_moduleId);

            moduleId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    moduleArrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,lecturerModuleId);
                    moduleId.setAdapter(moduleArrayAdapter);
                }
            });

        }




        publishedDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                datePicker(publishedDate,b);
            }
        });
        publishedTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                TimePicker(publishedTime,b);
            }
        });
        dueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                datePicker(dueDate,b);
            }
        });
        dueTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                TimePicker(dueTime,b);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mo_moduleId != ""){
                    updateAssessment(mo_DocumentId);
                }
                else {
                    saveAssessment();
                }
            }
        });
        return view;
    }
    public void updateAssessment(String documentId){
        Log.d("TAG", "updateAssessment: "+documentId);
        if(validations.isEmpty(credits))
        {
            dataSeeds.getFiledValue(new DataSeeds.getTheValueByFieldCallBack() {
                @Override
                public void onCallback(String fieldValues) {
                    AssessmentModule assessmentModule = new AssessmentModule
                            (
                                    fieldValues,
                                    credits.getText().toString(),
                                    publishedDate.getText().toString(),
                                    publishedTime.getText().toString(),
                                    dueDate.getText().toString(),
                                    dueTime.getText().toString(),
                                    firebaseAuth.getUid(),
                                    "active"
                            );
                    firebaseFirestore.collection("assessments").document(documentId).set(assessmentModule).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(),"Updated successful",Toast.LENGTH_SHORT).show();
                            reloadFragment(new AssessmentViewFragment());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            reloadFragment(new AssessmentScheduleFragment());
                        }
                    });
                }
            },"module","module",moduleId.getText().toString());

        }
    }
    public void saveAssessment()
    {
        if(validations.isEmpty(credits))
        {
            dataSeeds.getFiledValue(new DataSeeds.getTheValueByFieldCallBack() {
                @Override
                public void onCallback(String fieldValues) {
                    AssessmentModule assessmentModule = new AssessmentModule
                            (
                                    fieldValues,
                                    credits.getText().toString(),
                                    publishedDate.getText().toString(),
                                    publishedTime.getText().toString(),
                                    dueDate.getText().toString(),
                                    dueTime.getText().toString(),
                                    firebaseAuth.getUid(),
                                    "active"
                            );
                    firebaseFirestore.collection("assessments").add(assessmentModule).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getActivity(),"New Assessment Scheduled",Toast.LENGTH_LONG).show();
                            reloadFragment(new AssessmentScheduleFragment());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
//                    errorView.setText(e.getMessage());
//                    errorView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            },"module","module",moduleId.getText().toString());

        }
    }
    public void TimePicker(EditText textBox, boolean b) {
        calendar= Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(
                            TimePicker view, int hourOfDay,
                            int minute) {
                        String newHoursOfDay = String.valueOf(hourOfDay);
                        if(hourOfDay<10)
                        {
                            newHoursOfDay = "0"+hourOfDay;
                        }

                        String newMinute = String.valueOf(minute);
                        if(minute<10)
                        {
                            newMinute = "0"+minute;
                        }
                        textBox.setText(newHoursOfDay + ":" + newMinute);

                    }
                }, hour, minute, false);
        if(b){
            timePickerDialog.show();
        }
    }
    public void datePicker(EditText textBox, boolean b){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayDate = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(
                            DatePicker view, int year,
                            int monthOfYear, int dayOfMonth
                    ) {

                        int newMonth = monthOfYear + 1;
                        String twoDigitMonth = String.valueOf(newMonth);
                        String twoDigitDate = String.valueOf(dayOfMonth);
                        if (newMonth < 10) {
                            twoDigitMonth = "0" + newMonth;
                        }
                        if (dayOfMonth < 10) {
                            twoDigitDate = "0" + dayOfMonth;
                        }
                        textBox.setText(twoDigitDate + "-" + (twoDigitMonth) + "-" + year);
                    }
                }, year, month, dayDate);
        if(b){
            datePickerDialog.show();
        }
    }
    public void getModuleName()
    {
        firebaseFirestore.collection("lecturerModule").whereEqualTo("lecturerId",
                firebaseAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    
                    List<String> modulsIds = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        modulsIds.add(document.getString("moduleId").toString());
                    }

                    firebaseFirestore.collection("module")
                        .whereIn(FieldPath.documentId(),modulsIds)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                               if(task2.isSuccessful()){

                                   for (QueryDocumentSnapshot document2 : task2.getResult()) {
                                       lecturerModuleId.add(document2.getString("module").toString());

                                   }
                               }
                           }
                       }
                    );
                }
                else
                {
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
}