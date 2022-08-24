package yuken.example.ntu_timetable;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeTableCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableCreateFragment extends Fragment {

    private TextInputEditText txtdate,txtStartTime,txtEndTimePicker;

    View view;
    private int year, month, dayDate, hour, minute;
    Calendar calendar;
    private ArrayList<String> batchName;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> moduleIds, moduleName, lecturerIds, lecturerName ;
    private AutoCompleteTextView batchNumber, moduleId, lecturerId, type;
    private DataSeeds dataSeeds;
    private int batchId,moduleIdRetrieve6;
    public TimeTableCreateFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TimeTableCreateFragment newInstance(String param1, String param2) {
        TimeTableCreateFragment fragment = new TimeTableCreateFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_time_table_create, container, false);
        dataSeeds = new DataSeeds();

        txtdate = view.findViewById(R.id.datePicker);
        txtStartTime =  view.findViewById(R.id.startTimePicker);
        txtEndTimePicker = view.findViewById(R.id.endTimePicker);
        batchNumber = view.findViewById(R.id.batchId);
        moduleId = view.findViewById(R.id.module);
        lecturerId = view.findViewById(R.id.lecturerId);

        //get the batch id details
        batchName = dataSeeds.batchName(getActivity());
        arrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,batchName);
        batchNumber.setAdapter(arrayAdapter);

        // Module id get
        batchNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                moduleId.setText("");
                batchId = dataSeeds.findBatchIdByName(batchNumber.getText().toString(),getActivity());
                moduleIds = dataSeeds.moduleId(String.valueOf(batchId),getActivity());
                moduleName = dataSeeds.moduleName(moduleIds,getActivity());
                arrayAdapter = new ArrayAdapter<>(view.getContext(),R.layout.list_item,moduleName);
                moduleId.setAdapter(arrayAdapter);
            }
        });

        txtdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //Toast.makeText(getActivity(), "sasas", Toast.LENGTH_SHORT).show();
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayDate = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(
                                    DatePicker view, int year,
                                    int monthOfYear, int dayOfMonth
                            ) {

                                int newMonth = monthOfYear + 1;
                                String twoDigitMonth = String.valueOf(newMonth);
                                String twoDigitDate = String.valueOf(dayOfMonth);
                                if (newMonth < 10) {
                                    twoDigitMonth = "0" + newMonth;
                                }
                                if (dayOfMonth < 10) {
                                    twoDigitDate = "0" + dayOfMonth;
                                }
                                txtdate.setText(twoDigitDate + "-" + (twoDigitMonth) + "-" + year);
                            }
                        }, year, month, dayDate);
                if(b){
                    datePickerDialog.show();
                }
            }
        });
        txtStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    startTimePicker();
                }
            }
        });
        txtEndTimePicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                {
                    endTimePicker();
                }
            }
        });

        return view;
    }

    public void endTimePicker() {
        calendar= Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(
                            TimePicker view, int hourOfDay,
                            int minute) {
                        String newHoursOfDay = String.valueOf(hourOfDay);
                        if(hourOfDay<10)
                        {
                            newHoursOfDay = "0"+hourOfDay;
                        }

                        String newMinute = String.valueOf(minute);
                        if(minute<10)
                        {
                            newMinute = "0"+minute;
                        }
                        txtEndTimePicker.setText(newHoursOfDay + ":" + newMinute);

                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }

    public void startTimePicker() {
        calendar= Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(
                            TimePicker view, int hourOfDay,
                            int minute) {
                        String newHoursOfDay = String.valueOf(hourOfDay);
                        if(hourOfDay<10)
                        {
                            newHoursOfDay = "0"+hourOfDay;
                        }

                        String newMinute = String.valueOf(minute);
                        if(minute<10)
                        {
                            newMinute = "0"+minute;
                        }
                        txtStartTime.setText(newHoursOfDay + ":" + newMinute);

                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }

}