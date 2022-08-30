package yuken.example.ntu_timetable;

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
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentAssessmentViewFragment extends Fragment {
    View view;
    public GregorianCalendar cal_month, cal_month_copy;
    private HwAdapterAssessment hwAdapterAssessment;
    private TextView ass_month;
    List<String> moduleIds;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DataSeeds dataSeeds = new DataSeeds();
    Map<String,Object> names = new HashMap<>();

    public StudentAssessmentViewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StudentAssessmentViewFragment newInstance(String param1, String param2) {
        StudentAssessmentViewFragment fragment = new StudentAssessmentViewFragment();
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
        view = inflater.inflate(R.layout.fragment_student_assessment_view, container, false);

        AssessmentHomeCollection.date_collection_arr=new ArrayList<AssessmentHomeCollection>();
        getTimeTable();
//        AssessmentHomeCollection.date_collection_arr.add(
//                new AssessmentHomeCollection("2022-08-30" ,"Diwali","Holiday","this is holiday"));
//        AssessmentHomeCollection.date_collection_arr.add(
//                new AssessmentHomeCollection("2022-08-10" ,"Holi","Holiday","this is holiday"));
//        AssessmentHomeCollection.date_collection_arr.add( new AssessmentHomeCollection("2017-07-08" ,"Statehood Day","Holiday","this is holiday"));
//        AssessmentHomeCollection.date_collection_arr.add( new AssessmentHomeCollection("2017-08-08" ,"Republic Unian","Holiday","this is holiday"));
//        AssessmentHomeCollection.date_collection_arr.add( new AssessmentHomeCollection("2017-07-09" ,"ABC","Holiday","this is holiday"));
//        AssessmentHomeCollection.date_collection_arr.add( new AssessmentHomeCollection("2017-06-15" ,"demo","Holiday","this is holiday"));
//        AssessmentHomeCollection.date_collection_arr.add( new AssessmentHomeCollection("2017-09-26" ,"weekly off","Holiday","this is holiday"));
//        AssessmentHomeCollection.date_collection_arr.add( new AssessmentHomeCollection("2018-01-08" ,"Events","Holiday","this is holiday"));
//        AssessmentHomeCollection.date_collection_arr.add( new AssessmentHomeCollection("2018-01-16" ,"Dasahara","Holiday","this is holiday"));
//        AssessmentHomeCollection.date_collection_arr.add( new AssessmentHomeCollection("2018-02-09" ,"Christmas","Holiday","this is holiday"));



        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        hwAdapterAssessment = new HwAdapterAssessment(getActivity(), cal_month,
                AssessmentHomeCollection.date_collection_arr);

       ass_month = view.findViewById(R.id.ass_month);
       ass_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));


        ImageButton previous = view.findViewById(R.id.ib_prev);
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
        ImageButton next = view.findViewById(R.id.Ib_next);
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
        GridView gridview = view.findViewById(R.id.ass_gv_calendar);
        gridview.setAdapter(hwAdapterAssessment);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedGridDate = HwAdapterAssessment.day_string.get(position);
                ((HwAdapterAssessment) parent.getAdapter()).getPositionList(selectedGridDate, getActivity());
            }

        });

        return view;
    }
    public void getTimeTable(){
        moduleIds = new ArrayList<String>();
        firebaseFirestore.collection("studentModule")
            .whereEqualTo("studentId",firebaseUser.getUid())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {

                   if(task.isSuccessful()){

                       for (QueryDocumentSnapshot document : task.getResult()) {
                           moduleIds.add(document.getString("moduleId").toString());

                       }
                       firebaseFirestore.collection("assessments").whereIn("moduleId",
                                       moduleIds)
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

                                                  names.put("lectureName",fieldValues);
                                                  dataSeeds.getValueByField(new DataSeeds.getValueCallback() {
                                                      @Override
                                                      public void onCallback(String fieldValues) {
                                                         // Log.d("TAG", "onCallback: "+document2);
                                                          names.put("moduleName",fieldValues);
                                                            //String moduleId, String credits, String publishedDate, String publishedTime, String dueDate, String dueTime, String status, String lecturerId
                                                          AssessmentHomeCollection.date_collection_arr.add(
                                                                  new AssessmentHomeCollection(
                                                                          names.get("moduleName").toString(),
                                                                          document2.getString("credits"),
                                                                          document2.getString("publishedDate"),
                                                                          document2.getString("publishedTime"),
                                                                          document2.getString("dueDate"),
                                                                          document2.getString("dueTime"),
                                                                          names.get("lectureName").toString()
                                                                  )
                                                          );
                                                          refreshCalendar();
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
//        refreshCalendar();
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
        hwAdapterAssessment.refreshDays();
        hwAdapterAssessment.notifyDataSetChanged();
        ass_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }
}