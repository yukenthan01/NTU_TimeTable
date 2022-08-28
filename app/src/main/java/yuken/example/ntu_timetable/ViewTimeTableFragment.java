package yuken.example.ntu_timetable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ViewTimeTableFragment extends Fragment {
    RecyclerView timeTableRecycleView;
    TimeTableAdapter timeTableAdapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    View view;
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
        timeTableRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query keyQuery = firebaseFirestore.collection("timetable");
        FirestoreRecyclerOptions<TimeTableModel> options =
                new FirestoreRecyclerOptions.Builder<TimeTableModel>()
                        .setQuery(keyQuery,
                                TimeTableModel.class)
                        .build();

        timeTableAdapter = new TimeTableAdapter(options);

        timeTableRecycleView.setAdapter(timeTableAdapter);
        return view;
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

}