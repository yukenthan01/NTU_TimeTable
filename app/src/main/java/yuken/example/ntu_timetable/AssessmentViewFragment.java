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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssessmentViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentViewFragment extends Fragment {
    View view;
    Query keyQuery;
    AssessmentViewAdapter assessmentViewAdapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    RecyclerView assessmentRecycleView;
    List<String> singleModuleId,lecturerModuleId = new ArrayList<>() ;
    private ArrayAdapter<String> moduleArrayAdapter;
    private AutoCompleteTextView moduleId;
    DataSeeds dataSeeds =  new DataSeeds();

    public AssessmentViewFragment() {
        // Required empty public constructor
    }

    public static AssessmentViewFragment newInstance(String param1, String param2) {
        AssessmentViewFragment fragment = new AssessmentViewFragment();
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
        view = inflater.inflate(R.layout.fragment_assessment_view, container, false);
        moduleId =  view.findViewById(R.id.module);
        assessmentRecycleView = view.findViewById(R.id.assessmentRecycleView);
        assessmentRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        getModuleName();

        moduleArrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,lecturerModuleId);
        moduleId.setAdapter(moduleArrayAdapter);
        singleModuleId = new ArrayList<>();
        keyQuery = firebaseFirestore.collection("assessments");
        queryRunner(keyQuery);

        moduleId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dataSeeds.getFiledValue(new DataSeeds.getTheValueByFieldCallBack() {
                    @Override
                    public void onCallback(String fieldValues) {
                        singleModuleId.add(fieldValues);
                        keyQuery = firebaseFirestore.collection("assessments").whereEqualTo(
                                "moduleId",fieldValues);
                        queryRunner(keyQuery);
                        assessmentViewAdapter.startListening();
                    }
                },"module","module",moduleId.getText().toString());
            }
        });

        return view;
    }
    public void queryRunner(Query query)
    {

        FirestoreRecyclerOptions<AssessmentModule> options =
                new FirestoreRecyclerOptions.Builder<AssessmentModule>()
                        .setQuery(query,
                                AssessmentModule.class)
                        .build();

        assessmentViewAdapter = new AssessmentViewAdapter(options);

        assessmentRecycleView.setAdapter(assessmentViewAdapter);
        assessmentViewAdapter.notifyDataSetChanged();

    }
    @Override
    public void onStart() {
        super.onStart();
        assessmentViewAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        assessmentViewAdapter.stopListening();
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
}