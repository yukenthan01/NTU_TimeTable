package yuken.example.ntu_timetable;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class DefaultNotificationBroadcast extends BroadcastReceiver {
    Calendar calendar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    List<String> timetableIds,studentModuleIds;
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    @Override
    public void onReceive(Context context, Intent intent) {
        /////
        Intent i = new Intent(context,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_MUTABLE);
        /////


        //sendChangedNotificationSchedule(context,intent,pendingIntent);
        //dailyNotification(context,intent,pendingIntent);
        assessmentNotification(context,intent,pendingIntent);
    }
    public void assessmentNotification(Context context, Intent intent,PendingIntent pendingIntent)
    {
        studentModuleIds = new ArrayList<>();
        if(firebaseUser != null ){
            firebaseFirestore.collection("studentModule")
                .whereEqualTo("studentId",firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                studentModuleIds.add(document.getString("moduleId").toString());
                            }
                            firebaseFirestore.collection("assessments")
                                .whereIn("moduleId", studentModuleIds)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task2) {

                                        if (task2.isSuccessful()) {
                                            for (QueryDocumentSnapshot document2 :
                                                task2.getResult()) {

                                                    if (checkDate(document2.getString("publishedDate")) && checkTime(document2.getString("publishedTime"))){
                                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                                                                "timeTableNotification")
                                                                .setSmallIcon(R.drawable.ic_sharp_add_alert_24)
                                                                .setContentTitle("Assessment")
                                                                .setContentText("New Assessment " +
                                                                        "was Released")
                                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                                .setSound(alarmSound)
                                                                .setContentIntent(pendingIntent);

                                                        NotificationManagerCompat notificationManagerCompat =
                                                                NotificationManagerCompat.from(context);
                                                        notificationManagerCompat.notify(200,builder.build());
                                                        notificationStatusUpdate(document2.getId());
                                                    }
                                                    else {
                                                        Log.d("abcELse",
                                                                "onComplete: ELSE" );
                                                    }
                                            }
                                        }
                                    }
                            });
                    }
                }
            });
        }
    }
    public void dailyNotification(Context context, Intent intent,PendingIntent pendingIntent){
        if(firebaseUser != null ){
            firebaseFirestore.collection("studentTimetableId")
                    .whereEqualTo("studentId",firebaseUser.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    timetableIds.add(document.getString("timetableId").toString());
                                }
                                firebaseFirestore.collection("timetable")
                                        .whereIn(FieldPath.documentId(), timetableIds)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task2) {

                                                if (task2.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document2 :
                                                            task2.getResult()) {

                                                        String[] parts = document2.getString(
                                                                "startTime").split(":");
                                                        String finalTime ;
                                                        int ti = Integer.parseInt(parts[0])-3;
                                                        finalTime =
                                                               Integer.toString(ti) +":"+ parts[1];

                                                        if (checkDate(document2.getString("date")) && checkTime(finalTime)){
                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                                                                        "timeTableNotification")
                                                                        .setSmallIcon(R.drawable.ic_sharp_add_alert_24)
                                                                        .setContentTitle("Timetable")
                                                                        .setContentText("A Class " +
                                                                                "was scheduled " +
                                                                                "today")
                                                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                                        .setSound(alarmSound)
                                                                        .setContentIntent(pendingIntent);

                                                                NotificationManagerCompat notificationManagerCompat =
                                                                        NotificationManagerCompat.from(context);
                                                                notificationManagerCompat.notify(200,builder.build());
                                                                notificationStatusUpdate(document2.getId());
                                                        }
                                                        else {
                                                            Log.d("abcELse",
                                                                    "onComplete: ELSE" );
                                                        }
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
    }
    public void sendChnagedNotificationSchedule(Context context, Intent intent,
                                          PendingIntent pendingIntent){
        timetableIds = new ArrayList<String>();
        if(firebaseUser != null ){
            firebaseFirestore.collection("studentTimetableId")
            .whereEqualTo("studentId",firebaseUser.getUid())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            timetableIds.add(document.getString("timetableId").toString());
                        }
                        firebaseFirestore.collection("notifications")
                        .whereIn("alertId", timetableIds).whereEqualTo("alertType",
                                "timetable").whereEqualTo("status","active")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task2) {

                                if (task2.isSuccessful()) {
                                    for (QueryDocumentSnapshot document2 :
                                            task2.getResult()) {

                                        if (checkDate(document2.getString("date")) && checkTime(document2.getString("time"))){
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                                                    "timeTableNotification")
                                                    .setSmallIcon(R.drawable.ic_sharp_add_alert_24)
                                                    .setContentTitle("Timetable Changed")
                                                    .setContentText(document2.getString("message"))
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                    .setSound(alarmSound)
                                                    .setContentIntent(pendingIntent);

                                            NotificationManagerCompat notificationManagerCompat =
                                                    NotificationManagerCompat.from(context);
                                            notificationManagerCompat.notify(200,builder.build());
                                            notificationStatusUpdate(document2.getId());
                                        }

                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }
    public void notificationStatusUpdate(String documentId){
        firebaseFirestore.collection("notifications").document(documentId)
                .update(
                        "status","complete"
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //Toast.makeText(,"Updated successful",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                Log.d("onFailure", "notification update : "+e.getMessage());
            }
        });
    }
    public boolean checkDate(String date){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = format.format(new Date());
        try {
            Date day11 = format.parse(date);
            Date day22 = format.parse(currentDate);

            if(day11.equals(day22)){
               return true;
            }
            else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean checkTime(String time){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm",Locale.ENGLISH);
        String currentDate = format.format(new Date());
        try {
            Date day11 = format.parse(time);
            Date day22 = format.parse(currentDate);
            if(day11.equals(day22)){
               return true;
            }
            else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
