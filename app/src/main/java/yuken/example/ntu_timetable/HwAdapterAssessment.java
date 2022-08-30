package yuken.example.ntu_timetable;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HwAdapterAssessment extends BaseAdapter {
    private Activity context;
    private java.util.Calendar month;
    public GregorianCalendar pmonth;
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int mnthlength;
    String itemvalue, curentDateString;
    DateFormat df;

    private ArrayList<String> items;
    public static List<String> day_string;
    public ArrayList<AssessmentHomeCollection>  date_collection_arr;
    private String gridvalue;
    private ListView listTeachers;
    private ArrayList<DialogpojoAssessmenet> alCustom=new ArrayList<DialogpojoAssessmenet>();

    public HwAdapterAssessment(Activity context, GregorianCalendar monthCalendar,ArrayList<AssessmentHomeCollection> date_collection_arr) {
        this.date_collection_arr=date_collection_arr;
        HwAdapterAssessment.day_string = new ArrayList<String>();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        this.context = context;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);

        this.items = new ArrayList<String>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();

    }

    public int getCount() {
        return day_string.size();
    }

    public Object getItem(int position) {
        return day_string.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.cal_item, null);

        }


        dayView = (TextView) v.findViewById(R.id.date);
        String[] separatedTime = day_string.get(position).split("-");


        gridvalue = separatedTime[2].replaceFirst("^0*", "");
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            dayView.setTextColor(Color.parseColor("#A9A9A9"));
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if ((Integer.parseInt(gridvalue) < 20) && (position > 28)) {
            dayView.setTextColor(Color.parseColor("#A9A9A9"));
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else {
            dayView.setTextColor(Color.parseColor("#696969"));
        }


        if (day_string.get(position).equals(curentDateString)) {

            v.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            v.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        dayView.setText(gridvalue);

        // create date string for comparison
        String date = day_string.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        setEventView(v, position,dayView);

        return v;
    }

    public void refreshDays() {
        // clear items
        items.clear();
        day_string.clear();
        Locale.setDefault(Locale.US);
        pmonth = (GregorianCalendar) month.clone();
        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
        pmonthmaxset = (GregorianCalendar) pmonth.clone();

        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);


        for (int n = 0; n < 42; n++) {

            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            day_string.add(itemvalue);

        }
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }

    public void setEventView(View v,int pos,TextView txt){

        int len=AssessmentHomeCollection.date_collection_arr.size();
        for (int i = 0; i < len; i++) {
            AssessmentHomeCollection cal_obj=AssessmentHomeCollection.date_collection_arr.get(i);
            String date=cal_obj.publishedDate;
            int len1=day_string.size();
            if (len1>pos) {

                if (day_string.get(pos).equals(date)) {
                    if ((Integer.parseInt(gridvalue) > 1) && (pos < firstDay)) {

                    } else if ((Integer.parseInt(gridvalue) < 7) && (pos > 28)) {

                    } else {
                        v.setBackgroundColor(Color.parseColor("#343434"));
                        v.setBackgroundResource(R.drawable.rounded_calender);
                        txt.setTextColor(Color.parseColor("#696969"));
                    }

                }
            }}
    }

    public void getPositionList(String date,final Activity act){

        int len= AssessmentHomeCollection.date_collection_arr.size();
        JSONArray jbarrays=new JSONArray();
        for (int j=0; j<len; j++){
            if (AssessmentHomeCollection.date_collection_arr.get(j).publishedDate.equals(date)){
                HashMap<String, String> maplist = new HashMap<String, String>();

                maplist.put("moduleId",AssessmentHomeCollection.date_collection_arr.get(j).moduleId);
                maplist.put("credits",AssessmentHomeCollection.date_collection_arr.get(j).credits);
                maplist.put("publishedDate",AssessmentHomeCollection.date_collection_arr.get(j).publishedDate);
                maplist.put("publishedTime",AssessmentHomeCollection.date_collection_arr.get(j).publishedTime);
                maplist.put("dueDate",AssessmentHomeCollection.date_collection_arr.get(j).dueDate);
                maplist.put("dueTime",AssessmentHomeCollection.date_collection_arr.get(j).dueTime);
                maplist.put("lecturerId",AssessmentHomeCollection.date_collection_arr.get(j).lecturerId);

                JSONObject json1 = new JSONObject(maplist);
                jbarrays.put(json1);
            }
        }
        if (jbarrays.length()!=0) {
            final Dialog dialogs = new Dialog(context);
            dialogs.setContentView(R.layout.assessment_dialog_inform);
            listTeachers = (ListView) dialogs.findViewById(R.id.list_teachers);
            ImageView imgCross = (ImageView) dialogs.findViewById(R.id.img_cross);
            listTeachers.setAdapter(new DialogAdapterAssessment(context, getMatchList(jbarrays + "")));
            imgCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogs.dismiss();
                }
            });
            dialogs.show();

        }

    }
    private ArrayList<DialogpojoAssessmenet> getMatchList(String detail) {
        try {
            JSONArray jsonArray = new JSONArray(detail);
            alCustom = new ArrayList<DialogpojoAssessmenet>();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.optJSONObject(i);
                DialogpojoAssessmenet pojo = new DialogpojoAssessmenet();
                pojo.setModuleId(jsonObject.optString("moduleId"));
                pojo.setCredits(jsonObject.optString("credits"));
                pojo.setPublishedDate(jsonObject.optString("publishedDate"));
                pojo.setPublishedTime(jsonObject.optString("publishedTime"));
                pojo.setDueDate(jsonObject.optString("dueDate"));
                pojo.setDueTime(jsonObject.optString("dueTime"));
                pojo.setLecturerId(jsonObject.optString("lecturerId"));

                alCustom.add(pojo);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return alCustom;
    }
}