package yuken.example.ntu_timetable;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ViewTimeTableFragment extends Fragment {
    RecyclerView timeTableRecycleView;
    TimeTableAdapter timeTableAdapter;
    TextView aa;
    private AutoCompleteTextView batchNumber,moduleId;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    View view;
    List<String> batches,batchModuleIds,singleModuleId;
    private ArrayAdapter<String> batchArrayAdapter,moduleArrayAdapter;
    DataSeeds dataSeeds = new DataSeeds();
    Query keyQuery;
    public ViewTimeTableFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ViewTimeTableFragment newInstance(String param1, String param2) {
        ViewTimeTableFragment fragment = new ViewTimeTableFragment();
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
        view = inflater.inflate(R.layout.fragment_view_time_table, container, false);
        // Inflate the layout for this fragment
        timeTableRecycleView = view.findViewById(R.id.timeTableRecycleView2);
        batchNumber = view.findViewById(R.id.batchId);
        moduleId = view.findViewById(R.id.module);
        aa = view.findViewById(R.id.aa);
        timeTableRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        selectedDataBatches();
        batchArrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,batches);
        batchNumber.setAdapter(batchArrayAdapter);

        batchNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                moduleId.setText("");
                getModuleName(batchNumber.getText().toString());
                moduleArrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,batchModuleIds);
                moduleId.setAdapter(moduleArrayAdapter);
            }
        });

        singleModuleId = new ArrayList<>();
        keyQuery = firebaseFirestore.collection("timetable");
        queryRunner(keyQuery);
        moduleId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dataSeeds.getFiledValue(new DataSeeds.getTheValueByFieldCallBack() {
                    @Override
                    public void onCallback(String fieldValues) {
                        singleModuleId.add(fieldValues);
                        keyQuery = firebaseFirestore.collection("timetable").whereEqualTo(
                                "moduleId",fieldValues);
                        queryRunner(keyQuery);
                        timeTableAdapter.startListening();
                    }
                },"module","module",moduleId.getText().toString());
            }
        });

        //keyQuery = firebaseFirestore.collection("timetable");
        //queryRunner(keyQuery);

        return view;
    }
    public void queryRunner(Query query)
    {

        FirestoreRecyclerOptions<TimeTableModel> options =
                new FirestoreRecyclerOptions.Builder<TimeTableModel>()
                        .setQuery(query,
                                TimeTableModel.class)
                        .build();

        timeTableAdapter = new TimeTableAdapter(options);

        timeTableRecycleView.setAdapter(timeTableAdapter);
        timeTableAdapter.notifyDataSetChanged();
    }
    @Override
    public void onStart() {
        super.onStart();
        timeTableAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        timeTableAdapter.stopListening();
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

}