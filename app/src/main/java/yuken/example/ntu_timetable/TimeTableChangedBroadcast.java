package yuken.example.ntu_timetable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TimeTableChangedBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String extras = intent.getStringExtra("message");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                "timeTableNotification").setSmallIcon(R.drawable.ic_sharp_add_alert_24).setContentTitle("Your " +
                "Time Table").setContentText(extras).setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200,builder.build());
    }
}
