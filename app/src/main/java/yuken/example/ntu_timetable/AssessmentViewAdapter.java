package yuken.example.ntu_timetable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class AssessmentViewAdapter extends FirestoreRecyclerAdapter<AssessmentModule,
        AssessmentViewAdapter.myViewHolder> {


    public AssessmentViewAdapter(@NonNull FirestoreRecyclerOptions<AssessmentModule> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position,
                                    @NonNull AssessmentModule assessmentModule) {
        DataSeeds dataSeeds  = new DataSeeds();
        holder.publishDateTimeList.setText("Publish Date and Time :-"+assessmentModule.getPublishedDate() +" "+ assessmentModule.getPublishedTime());
        holder.dueDateTimeList.setText("Due Date and Time :- " + assessmentModule.getDueDate() +
                " " + assessmentModule
                        .getDueTime());
        holder.creditList.setText("Credit Score :- "+assessmentModule.getCredits());

        dataSeeds.getValueByField(new DataSeeds.getValueCallback() {
            @Override
            public void onCallback(String fieldValues) {
                holder.moduleList.setText("Module :-"+fieldValues);
            }
        },"module","module",assessmentModule.getModuleId());
        holder.editList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                snapshot.getId();
                AppCompatActivity activity =  (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                        new AssessmentScheduleFragment(assessmentModule,snapshot.getId())).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessments_list,
                parent,false);

        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView creditList,dueDateTimeList,publishDateTimeList,moduleList,editList;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            creditList = itemView.findViewById(R.id.creditList);
            dueDateTimeList = itemView.findViewById(R.id.dueDateTimeList);
            publishDateTimeList = itemView.findViewById(R.id.publishDateTimeList);
            moduleList = itemView.findViewById(R.id.moduleList);
            editList = itemView.findViewById(R.id.editList);

        }
    }
}
