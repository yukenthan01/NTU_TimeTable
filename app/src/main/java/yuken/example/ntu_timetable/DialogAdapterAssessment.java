package yuken.example.ntu_timetable;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DialogAdapterAssessment extends BaseAdapter {
    Activity activity;

    private Activity context;
    private ArrayList<DialogpojoAssessmenet> alCustom;
    private String sturl;

    public DialogAdapterAssessment(Activity context, ArrayList<DialogpojoAssessmenet> alCustom) {
        this.context = context;
        this.alCustom = alCustom;

    }
    @Override
    public int getCount() {
        return alCustom.size();

    }

    @Override
    public Object getItem(int i) {
        return alCustom.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.assessment_row_addapter, null, true);

        TextView assModule = (TextView)listViewItem.findViewById(R.id.assModule);
        TextView assCredits = (TextView)listViewItem.findViewById(R.id.assCredits);
        TextView assPublishDate = (TextView)listViewItem.findViewById(R.id.assPublishDate);
        TextView assPublishTime = (TextView)listViewItem.findViewById(R.id.assPublishTime);
        TextView assDuedate = (TextView)listViewItem.findViewById(R.id.assDuedate);
        TextView assDueTime = (TextView)listViewItem.findViewById(R.id.assDueTime);
        TextView assLecturer = (TextView)listViewItem.findViewById(R.id.assLecturer);


        assModule.setText("Module : "+alCustom.get(position).getModuleId());
        assCredits.setText("Credits : "+alCustom.get(position).getCredits());
        assPublishDate.setText("Publish Date : "+alCustom.get(position).getPublishedDate());
        assPublishTime.setText("Publish Time : "+alCustom.get(position).getPublishedTime());
        assDuedate.setText("Submission Date : "+alCustom.get(position).getDueDate());
        assDueTime.setText("Submission Time : "+alCustom.get(position).getDueTime());
        assLecturer.setText("Lecturer : "+alCustom.get(position).getLecturerId());

        return  listViewItem;
    }
}
