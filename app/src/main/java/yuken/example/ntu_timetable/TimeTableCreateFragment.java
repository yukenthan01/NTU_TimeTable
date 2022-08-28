package yuken.example.ntu_timetable;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeTableCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableCreateFragment extends Fragment {
    String mo_lecturerId,mo_moduleId,mo_date,mo_startTime,mo_endTime,mo_classType,mo_location,mo_batchNo,mo_status,
            mo_termStartDate,mo_termEndDate,mo_documentID;
    private TextInputEditText txtdate,txtStartTime,txtEndTimePicker,location,termStartDate,
            termEndDate;
    private Button btnsubmit;
    private CheckBox checkRepeat;
    private LinearLayout linearLayout;
    View view;
    private int year, month, dayDate, hour, minute;
    Calendar calendar;
    private ArrayAdapter<String> arrayAdapter,batchArrayAdapter,moduleArrayAdapter;
    private AutoCompleteTextView batchNumber, moduleId, lecturerId, type;


    List<String> batches ;
    List<String> batchModuleIds ;
    List<String> lecturesIds ;
    private String [] types = {"Online","InPerson"};
    public String lastInsertId ;
    public String lectureDocumentId ;
    private ArrayList<String> tweetList = new ArrayList<String>();
    private SimpleDateFormat simpleDateFormat;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Map<String,Object> documentIdCallback = new HashMap<>();
    private DataSeeds dataSeeds = new DataSeeds();
    public TimeTableCreateFragment() {
        // Required empty public constructor
    }
    public TimeTableCreateFragment(TimeTableModel timeTableModel,String documentId) {
        mo_lecturerId = timeTableModel.getLecturerId();
        mo_moduleId = timeTableModel.getModuleId();
        mo_date = timeTableModel.getDate();
        mo_startTime = timeTableModel.getStartTime();
        mo_endTime = timeTableModel.getEndTime();
        mo_classType = timeTableModel.getClassType();
        mo_location = timeTableModel.getLocation();
        mo_batchNo = timeTableModel.getBatchNo();
        mo_status = timeTableModel.getStatus();
        mo_termStartDate = timeTableModel.getTermStartDate();
        mo_termEndDate = timeTableModel.getTermEndDate();

        mo_documentID = documentId;

    }


    // TODO: Rename and change types and number of parameters
    public static TimeTableCreateFragment newInstance(String param1, String param2) {
        TimeTableCreateFragment fragment = new TimeTableCreateFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_time_table_create, container, false);
        dataSeeds = new DataSeeds();

        txtdate = view.findViewById(R.id.datePicker);
        txtStartTime =  view.findViewById(R.id.startTimePicker);
        txtEndTimePicker = view.findViewById(R.id.endTimePicker);
        batchNumber = view.findViewById(R.id.batchId);
        moduleId = view.findViewById(R.id.module);
        lecturerId = view.findViewById(R.id.lecturerId);
        type = view.findViewById(R.id.type);
        location = view.findViewById(R.id.location);
        termStartDate = view.findViewById(R.id.termStartDate);
        termEndDate = view.findViewById(R.id.termEndDate);
        checkRepeat = view.findViewById(R.id.repeat);
        linearLayout = view.findViewById(R.id.repeatBorder);
        btnsubmit = view.findViewById(R.id.btnSubmit);



        if(mo_moduleId != null)
        {
            txtdate.setText(mo_date);
            txtStartTime.setText(mo_startTime);
            txtEndTimePicker.setText(mo_endTime);;
            batchNumber.setText(mo_batchNo);
            //get module name by documnet ID
            dataSeeds.getValueByField(new DataSeeds.getLectureIdCallback() {
                @Override
                public void onCallback(String fieldValues) {
                    moduleId.setText(fieldValues);
                    //batchNumber.setAdapter(batchArrayAdapter);
                }
            },"module","module",mo_moduleId);

            //get Lecture name by documnet ID
            dataSeeds.getValueByField(new DataSeeds.getLectureIdCallback() {
                @Override
                public void onCallback(String fieldValues) {
                    lecturerId.setText(fieldValues);
                }
            },"users","firstname",mo_lecturerId);

            type.setText(mo_classType);
            location.setText(mo_location);
            termStartDate.setText(mo_termStartDate);
            termEndDate.setText(mo_termEndDate);
            if(!mo_termStartDate.isEmpty())
            {
                linearLayout.setVisibility(View.VISIBLE);
                checkRepeat.setVisibility(View.GONE);
            }
            else
            {
                linearLayout.setVisibility(View.GONE);
                termEndDate.setText("");
                termStartDate.setText("");
            }
            moduleId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    getModuleName(batchNumber.getText().toString());
                    arrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,batchModuleIds);
                    moduleId.setAdapter(arrayAdapter);
                }
            });
            lecturerId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    getLectures(moduleId.getText().toString());
                    arrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,lecturesIds);
                    lecturerId.setAdapter(arrayAdapter);
                }
            });
        }

        checkRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkRepeat.isChecked())
                {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    linearLayout.setVisibility(View.GONE);
                    termEndDate.setText("");
                    termStartDate.setText("");
                }
            }
        });
        selectedDataBatches();

        batchArrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,batches);
        batchNumber.setAdapter(batchArrayAdapter);

        // Set the type in to the adapter
        arrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,types);
        type.setAdapter(arrayAdapter);
        // Module id get
        batchNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                moduleId.setText("");
                getModuleName(batchNumber.getText().toString());
                arrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,batchModuleIds);
                moduleId.setAdapter(arrayAdapter);
            }
        });
        type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(type.getText().toString().equals("Online")){
                    Log.d("type", "onItemClick: "+"true");
                    location.setEnabled(false);
                }
                else{
                    location.setEnabled(true);
                    Log.d("type", "onItemClick: "+"false");

                }
            }
        });
        moduleId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //lecturerId.setText("");
                getLectures(moduleId.getText().toString());
                arrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,lecturesIds);
                lecturerId.setAdapter(arrayAdapter);
            }
        });

        txtdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                datePicker(txtdate,b);
            }
        });

        txtStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    startTimePicker();
                }
            }
        });
        txtEndTimePicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                {
                    endTimePicker();
                }
            }
        });
        termStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                datePicker(termStartDate,b);
            }
        });
        termEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                datePicker(termEndDate,b);
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mo_documentID == ""){
                    getDocumentIdByName(new getDocumentIdByNameCallBack() {
                        @Override
                        public void onCallback(String documentId) {
                            documentIdCallback.put("lectureId",documentId);
                            ////////////module ID
                            getDocumentIdByName(new getDocumentIdByNameCallBack() {
                                @Override
                                public void onCallback(String documentId) {
                                    documentIdCallback.put("moduleId",documentId);
                                    Map<String,Object> timetable = new HashMap<>();
                                    timetable.put("lecturerId", documentIdCallback.get("lectureId").toString());
                                    timetable.put("moduleId",documentIdCallback.get("moduleId").toString());
                                    timetable.put("date",txtdate.getText().toString());
                                    timetable.put("startTime",txtStartTime.getText().toString());
                                    timetable.put("endTime",txtEndTimePicker.getText().toString());
                                    timetable.put("classType",type.getText().toString());
                                    timetable.put("location",location.getText().toString());
                                    timetable.put("batchNo",batchNumber.getText().toString());
                                    timetable.put("status","active");
                                    timetable.put("termStartDate",termStartDate.getText().toString());
                                    timetable.put("termEndDate",termEndDate.getText().toString());
                                    insertTablesValues(timetable);
                                }
                            },"module","module",moduleId.getText().toString());
                        }
                    },"users","firstname",lecturerId.getText().toString());
                }
                else
                {
                    getDocumentIdByName(new getDocumentIdByNameCallBack() {
                        @Override
                        public void onCallback(String documentId) {
                            documentIdCallback.put("lectureId",documentId);
                            ////////////module ID
                            getDocumentIdByName(new getDocumentIdByNameCallBack() {
                                @Override
                                public void onCallback(String documentId) {
                                    documentIdCallback.put("moduleId",documentId);
                                    Map<String,Object> timetable = new HashMap<>();
                                    timetable.put("lecturerId", documentIdCallback.get("lectureId").toString());
                                    timetable.put("moduleId",documentIdCallback.get("moduleId").toString());
                                    timetable.put("date",txtdate.getText().toString());
                                    timetable.put("startTime",txtStartTime.getText().toString());
                                    timetable.put("endTime",txtEndTimePicker.getText().toString());
                                    timetable.put("classType",type.getText().toString());
                                    timetable.put("location",location.getText().toString());
                                    timetable.put("batchNo",batchNumber.getText().toString());
                                    timetable.put("status","active");
                                    timetable.put("termStartDate",termStartDate.getText().toString());
                                    timetable.put("termEndDate",termEndDate.getText().toString());
                                    updateTimeTable(timetable);
                                }
                            },"module","module",moduleId.getText().toString());
                        }
                    },"users","firstname",lecturerId.getText().toString());
                }
            }
        });
        return view;
    }

    //update time table
    public void updateTimeTable(Map<String,Object> timetable)
    {
        firebaseFirestore.collection("timetable").document(mo_documentID).update(timetable).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(),"Updated successful",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface getDocumentIdByNameCallBack {
        void onCallback(String documentId);
    }

    public void getDocumentIdByName(getDocumentIdByNameCallBack getDocumentIdByNameCallBack,
                                    String table,String field,String value){
        firebaseFirestore.collection(table)
            .whereEqualTo(field,value)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if(task.isSuccessful()){
                       for (QueryDocumentSnapshot document : task.getResult()) {
                           getDocumentIdByNameCallBack.onCallback(document.getId());
                       }
                   }
               }
           }
        );
    }
    public interface getValueCallBack {
        void onCallback(List<String> fieldValues);
    }

    public void getValueByField(getValueCallBack getValueCallBack,
                                    String table,String field,String value,String selectField){
        firebaseFirestore.collection(table)
            .whereEqualTo(field,value)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if(task.isSuccessful()){
                       List<String> results = new ArrayList<>();
                       for (QueryDocumentSnapshot document : task.getResult()) {
                           results.add(document.getString(selectField));
                       }
                       getValueCallBack.onCallback(results);
                   }
               }
           }
        );
    }
    public void getModuleName(String batchName)
    {
        batchModuleIds = new ArrayList<String>();
        firebaseFirestore.collection("batch").whereEqualTo("name",batchName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String batchDocumentId = "" ;
                    List<String> modulsIds = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        batchDocumentId = document.getId();
                    }

                    firebaseFirestore.collection("batchModule")
                        .whereEqualTo("batchId",batchDocumentId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                if(task2.isSuccessful()){

                                    for (QueryDocumentSnapshot document2 : task2.getResult()) {
                                        modulsIds.add(document2.getString("moduleId").toString());
                                    }
                                    firebaseFirestore.collection("module").whereIn(FieldPath.documentId(),modulsIds)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task3) {
                                               if(task3.isSuccessful()){
                                                   for (QueryDocumentSnapshot document2 :
                                                           task3.getResult()) {
                                                       batchModuleIds.add(document2.getString("module").toString());
                                                   }
                                               }
                                           }
                                       }
                                    );
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
    public void getLectures(String module){
        lecturesIds = new ArrayList<String>();
        firebaseFirestore.collection("module").whereEqualTo("module",module).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String moduleDocumentId = "" ;
                    List<String> lectIds = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        moduleDocumentId = document.getId();
                    }

                    firebaseFirestore.collection("lecturerModule")
                        .whereEqualTo("moduleId",moduleDocumentId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                               if(task2.isSuccessful()){

                                   for (QueryDocumentSnapshot document2 : task2.getResult()) {
                                       lectIds.add(document2.getString("lecturerId").toString());
                                   }
                                   firebaseFirestore.collection("users").whereIn(FieldPath.documentId(),lectIds)
                                       .get()
                                       .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                          @Override
                                          public void onComplete(@NonNull Task<QuerySnapshot> task3) {
                                              if(task3.isSuccessful()){
                                                  for (QueryDocumentSnapshot document2 :
                                                          task3.getResult()) {
                                                      lecturesIds.add(document2.getString(
                                                              "firstname").toString());
                                                  }
                                              }
                                          }
                                      }
                                   );
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
    public void selectedDataBatches(){
        batches = new ArrayList<String>();
        firebaseFirestore.collection("batch").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        batches.add(document.getString("name").toString());
                    }
                } else {
                    Log.d("spein", "Error getting documents: ", task.getException());
                }
            }
        });

    }
    public void endTimePicker() {
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
                        txtEndTimePicker.setText(newHoursOfDay + ":" + newMinute);

                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }
    public void datePicker(EditText textBox,boolean b){
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
    public void startTimePicker() {
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
                        txtStartTime.setText(newHoursOfDay + ":" + newMinute);

                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }

    // all date insert
    public void insertTablesValues(Map<String,Object> timetable)
    {
        if(timetable.get("termStartDate").toString().isEmpty()){

            firebaseFirestore.collection("timetable").add(timetable).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    lastInsertId = documentReference.getId();
                    getValueByField(new getValueCallBack() {
                        @Override
                        public void onCallback(List<String> fieldValues) {
                            Map<String,Object> studentIdModuleId = new HashMap<>();

                            for(int i = 0; i < fieldValues.size(); i++){

                                Log.d("stuids", "onCallback: "+fieldValues.get(i));
                                studentIdModuleId.put("timetableId",documentReference.getId());
                                studentIdModuleId.put("studentId",fieldValues.get(i));
                                manyToManyInsert(studentIdModuleId,"studentTimetableId");
                                Toast.makeText(getContext(),"Timetable added successful",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    },"studentModule","moduleId",timetable.get("moduleId").toString(),"studentId");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG", "onSuccess: "+e.getMessage());
                }
            });
        }
        //term dates insert
        else{
            //////////////////////////
            Date startDateTerm = null, endDateTerm = null, currentDate = null;
            simpleDateFormat = new SimpleDateFormat("d-M-yyyy");
            String previousDate = null, lastDate = null;
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(simpleDateFormat.parse(timetable.get("termStartDate").toString()));

                cal.add(Calendar.DATE, -1);
                previousDate = simpleDateFormat.format(cal.getTime());
                startDateTerm = simpleDateFormat.parse(previousDate);

                cal = Calendar.getInstance();
                cal.setTime(simpleDateFormat.parse(timetable.get("termEndDate").toString()));
                cal.add(Calendar.DATE, 1);
                lastDate = simpleDateFormat.format(cal.getTime());
                endDateTerm = simpleDateFormat.parse(lastDate);

            } catch (ParseException e) {

            }
            long differenceWeek = endDateTerm.getTime() - startDateTerm.getTime();
            int weeks = (int)(TimeUnit.DAYS.convert(differenceWeek, TimeUnit.MILLISECONDS)/7);

            String date = txtdate.getText().toString();
            //term dates Countiing loop
            for(int i = 0 ; i<= weeks+1 ; i++) {
                Calendar c = Calendar.getInstance();
                try {
                    currentDate = simpleDateFormat.parse(date);
                    Format f = new SimpleDateFormat("EEEE");
                    String day = f.format(currentDate);
                    Log.d("asIF", "empty true"+timetable);
                    if (simpleDateFormat.parse(date).after(startDateTerm) && simpleDateFormat.parse(date).before(endDateTerm) && !day.equals("Saturday")&& !day.equals("Sunday")) {
                        // Insert term data with loop
                        timetable.put("date",date);
                        firebaseFirestore.collection("timetable").add(timetable).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                lastInsertId = documentReference.getId();
                                getValueByField(new getValueCallBack() {
                                    @Override
                                    public void onCallback(List<String> fieldValues) {
                                        Map<String,Object> studentIdModuleId = new HashMap<>();
                                        for(int i = 0; i < fieldValues.size(); i++){

                                            studentIdModuleId.put("timetableId",documentReference.getId());
                                            studentIdModuleId.put("studentId",fieldValues.get(i));
                                            manyToManyInsert(studentIdModuleId,"studentTimetableId");
                                        }
                                    }
                                },"studentModule","moduleId",timetable.get("moduleId").toString(),"studentId");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "onSuccess: "+e.getMessage());
                            }
                        });
                    }
                    try {
                        c.setTime(simpleDateFormat.parse(date));
                    } catch (ParseException e) {
                        Log.d("asCatcg1", "empty true"+e.getMessage());
                    }

                    c.add(Calendar.DATE, 7);
                    date = simpleDateFormat.format(c.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("asCatcg2", "empty true"+e.getMessage());
                }
            }
        }
    }
    public void manyToManyInsert(Map<String,Object> datas,String table)
    {
        firebaseFirestore.collection(table).add(datas).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                lastInsertId = documentReference.getId();
                Log.d("dummy", "onSuccess: User Added Successfully"+documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onSuccess: "+e.getMessage());
            }
        });
    }
    // Dummy Insert values
    public void dummyInsertTablesValues()
    {
        Map<String,Object> dummy = new HashMap<>();
        dummy.put("moduleId","JLVsrUNVwLT8n556MDKy");
        dummy.put("studentId","jWMRaFYxpVYyzp7acICm");
        firebaseFirestore.collection("studentModule").add(dummy).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                lastInsertId = documentReference.getId();
                Log.d("dummy", "onSuccess: User Added Successfully"+documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onSuccess: "+e.getMessage());
            }
        });
    }
}