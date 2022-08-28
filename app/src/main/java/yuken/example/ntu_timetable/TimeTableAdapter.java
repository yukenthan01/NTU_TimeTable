package yuken.example.ntu_timetable;

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

public class TimeTableAdapter extends FirestoreRecyclerAdapter<TimeTableModel,
        TimeTableAdapter.myViewHolder> {

    public TimeTableAdapter(@NonNull FirestoreRecyclerOptions<TimeTableModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull TimeTableModel model) {
        DataSeeds dataSeeds = new DataSeeds();
        holder.dateList.setText(model.getDate());
        holder.timeList.setText(model.getStartTime());
        dataSeeds.getValueByField(new DataSeeds.getLectureIdCallback() {
            @Override
            public void onCallback(String fieldValues) {
                holder.lecturer.setText(fieldValues);
            }
        },"users","firstname",model.getLecturerId());
        holder.location.setText(model.getLocation());
        holder.editList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                snapshot.getId();
                AppCompatActivity activity =  (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                        new TimeTableCreateFragment(model,snapshot.getId())).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_table_list,
                parent,false);

        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView dateList,timeList,location,lecturer,editList;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            dateList = itemView.findViewById(R.id.dateList);
            timeList = itemView.findViewById(R.id.timeList);
            location = itemView.findViewById(R.id.locationList);
            lecturer = itemView.findViewById(R.id.lecturerList);
            editList = itemView.findViewById(R.id.editList);

        }
    }
}
