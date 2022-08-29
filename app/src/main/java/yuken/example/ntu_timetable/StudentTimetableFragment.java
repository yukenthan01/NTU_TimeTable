package yuken.example.ntu_timetable;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentTimetableFragment extends Fragment {
    View view;
    public GregorianCalendar cal_month, cal_month_copy;
    private HwAdapter hwAdapter;
    private TextView tv_month,txtdata;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    List<String> timetableIds;
    Map<String,Object> documentIdCallback = new HashMap<>();
    private static ArrayList<TimeTableModel> mArrayList = new ArrayList<>();;
    List<TimeTableModel> timetableTypes;
    private SharedPreferences sharedPreferences;
    DataSeeds dataSeeds = new DataSeeds();
    String lectureName,moduleName ;


    public StudentTimetableFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StudentTimetableFragment newInstance(String param1, String param2) {
        StudentTimetableFragment fragment = new StudentTimetableFragment();
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
        view = inflater.inflate(R.layout.fragment_student_timetable, container, false);
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();

        getTimeTable();

        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        hwAdapter = new HwAdapter(getActivity(), cal_month,HomeCollection.date_collection_arr);

        tv_month = (TextView) view.findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
        txtdata = view.findViewById(R.id.txtdata);

        ImageButton previous = (ImageButton) view.findViewById(R.id.ib_prev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 4&&cal_month.get(GregorianCalendar.YEAR)==2017) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(getActivity(), "Event Detail is available for current session only.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    setPreviousMonth();
                    refreshCalendar();
                }


            }
        });
        ImageButton next = (ImageButton) view.findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 5&&cal_month.get(GregorianCalendar.YEAR)==2018) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(getActivity(), "Event Detail is available for current session only.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    setNextMonth();
                    refreshCalendar();
                }
            }
        });
        GridView gridview = (GridView) view.findViewById(R.id.gv_calendar);
        gridview.setAdapter(hwAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedGridDate = HwAdapter.day_string.get(position);
                ((HwAdapter) parent.getAdapter()).getPositionList(selectedGridDate, getActivity());
            }

        });
        return view;
    }

    public void getTimeTable(){
        timetableIds = new ArrayList<String>();
        firebaseFirestore.collection("studentTimetableId")
            .whereEqualTo("studentId",firebaseUser.getUid())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {

                   if(task.isSuccessful()){

                       for (QueryDocumentSnapshot document : task.getResult()) {
                           timetableIds.add(document.getString("timetableId").toString());
                       }
                       firebaseFirestore.collection("timetable").whereIn(FieldPath.documentId(),timetableIds)
                           .get()
                           .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                              @Override
                              public void onComplete(@NonNull Task<QuerySnapshot> task2) {

                                  if(task2.isSuccessful()){
                                      for (QueryDocumentSnapshot document2 :
                                              task2.getResult()) {

                                          dataSeeds.getValueByField(new DataSeeds.getValueCallback() {
                                              @Override
                                              public void onCallback(String fieldValues) {
                                                  lectureName = fieldValues;

                                                  dataSeeds.getValueByField(new DataSeeds.getValueCallback() {
                                                      @Override
                                                      public void onCallback(String fieldValues) {

                                                          moduleName = fieldValues;
                                                          txtdata.setVisibility(View.GONE);
                                                          HomeCollection.date_collection_arr.add(
                                                                  new HomeCollection(
                                                                          lectureName,
                                                                          moduleName,
                                                                          document2.getString("date"),
                                                                          document2.getString("startTime"),
                                                                          document2.getString("endTime"),
                                                                          document2.getString("classType"),
                                                                          document2.getString("location"),
                                                                          document2.getString("batchNo")
                                                                  )
                                                          );
                                                      }
                                                  },"module","module",document2.getString("moduleId"));

                                              }
                                          },"users","firstname",document2.getString("lecturerId"));

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
    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    public void refreshCalendar() {
        hwAdapter.refreshDays();
        hwAdapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }
}