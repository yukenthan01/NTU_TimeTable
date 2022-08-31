package yuken.example.ntu_timetable;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationWorker extends Worker{
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    List<String> timetableIds;

    private int year, month, dayDate, hour, minute;
    Calendar calendar;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        ContextCompat.getMainExecutor(getApplicationContext()).execute(new Runnable() {
            @Override
            public void run() {
                //sendNotificationSchedule();
               // testdate();
//                        Intent intent = new Intent(getApplicationContext(),
//                DefaultNotificationBroadcast.class);
//        PendingIntent pendingIntent =
//                PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        AlarmManager alarmManager =
//                null;
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
//        }
//
//        long currentTimeInMilliSeconds = System.currentTimeMillis();
//
////        alarmManager.set(AlarmManager.RTC_WAKEUP,
////                currentTimeInMilliSeconds+10, pendingIntent);
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,currentTimeInMilliSeconds,10,pendingIntent);
            }

        });
        return null;
    }
    public void testdate(){
        final String stringDate = "2014-07-17 10:00";

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = inputFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -3);      // adds one hour
        calendar.getTime();

        Log.d("cal", "testdate: "+calendar.getTime());
    }
    public void sendNotificationSchedule(){
        timetableIds = new ArrayList<String>();
        if(firebaseUser != null ){
            firebaseFirestore.collection("studentTimetableId")
                .whereEqualTo("studentId",firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {

                       if(task.isSuccessful()){

                           for (QueryDocumentSnapshot document : task.getResult()) {
                               timetableIds.add(document.getString("timetableId").toString());
                           }
                           firebaseFirestore.collection("notifications")
                               .whereIn("alertId",timetableIds).whereEqualTo("alertType",
                                           "timetable")
                               .get()
                               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                  @Override
                                  public void onComplete(@NonNull Task<QuerySnapshot> task2) {

                                      if(task2.isSuccessful()){
                                          for (QueryDocumentSnapshot document2 :
                                                  task2.getResult()) {
                                              Log.d("abc", "onComplete: "+document2);

//                                              Intent intent = new Intent(getApplicationContext(),
//                                                      TimeTableChangedBroadcast.class);
//                                              intent.putExtra("message", message);
//
//                                              PendingIntent pendingIntent =
//                                                      PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//                                              AlarmManager alarmManager =
//                                                      null;
//                                              if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                                  alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
//                                              }
//
//                                              long currentTimeInMilliSeconds = System.currentTimeMillis();
//
//                                              alarmManager.set(AlarmManager.RTC_WAKEUP,
//                                                      currentTimeInMilliSeconds, pendingIntent);

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
    }
}
