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

public class DialogAdaptorStudent extends BaseAdapter {
    Activity activity;

    private Activity context;
    private ArrayList<Dialogpojo> alCustom;
    private String sturl;


    public DialogAdaptorStudent(Activity context, ArrayList<Dialogpojo> alCustom) {
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
        View listViewItem = inflater.inflate(R.layout.row_addapt, null, true);

        TextView date=(TextView)listViewItem.findViewById(R.id.date);
        TextView module=(TextView)listViewItem.findViewById(R.id.module);
        TextView lecturer=(TextView)listViewItem.findViewById(R.id.lecturer);
        TextView startTime=(TextView)listViewItem.findViewById(R.id.startTime);
        TextView endTime=(TextView)listViewItem.findViewById(R.id.endTime);
        TextView type=(TextView)listViewItem.findViewById(R.id.type);
        TextView location=(TextView)listViewItem.findViewById(R.id.location);


        date.setText("Date : "+alCustom.get(position).getDate());
        module.setText("Module : "+alCustom.get(position).getModule());
        lecturer.setText("Lecturer : "+alCustom.get(position).getLecturer());
        startTime.setText("Start Time : "+alCustom.get(position).getStartTime());
        endTime.setText("End Time : "+alCustom.get(position).getEndTime());
        type.setText("Type : "+alCustom.get(position).getClassType());
        location.setText("Location : "+alCustom.get(position).getLocation());

        return  listViewItem;
    }

}
