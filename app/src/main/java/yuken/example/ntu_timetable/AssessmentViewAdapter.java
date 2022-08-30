package yuken.example.ntu_timetable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

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

        Log.d("TAG", "queryRunner: "+holder);

        dataSeeds.getValueByField(new DataSeeds.getValueCallback() {
            @Override
            public void onCallback(String fieldValues) {
                holder.moduleList.setText("Module :-"+fieldValues);
            }
        },"module","module",assessmentModule.getModuleId());

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessments_list,
                parent,false);

        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView creditList,dueDateTimeList,publishDateTimeList,moduleList;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            creditList = itemView.findViewById(R.id.creditList);
            dueDateTimeList = itemView.findViewById(R.id.dueDateTimeList);
            publishDateTimeList = itemView.findViewById(R.id.publishDateTimeList);
            moduleList = itemView.findViewById(R.id.moduleList);

        }
    }
}
