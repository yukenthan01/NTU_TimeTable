package yuken.example.ntu_timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

public class StudentDashboardActivity extends AppCompatActivity {
    ImageView profileImageView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    View haderXml;
    TextView fullNmeTextView;
    DataSeeds dataSeeds =new DataSeeds();

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

//        //////////////////////////////////////////////////////////////////
        createNotificationChannel();
//                notificationWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
//         .build();
        //long currentTimeInMilliSeconds = System.currentTimeMillis();

//        notificationWorkRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class,
//                15*60*1000,
//                TimeUnit.MILLISECONDS)
//                .build();
//
//        WorkManager
//                .getInstance(this)
//                .enqueue(notificationWorkRequest);
        Intent intent = new Intent(getApplicationContext(),
                DefaultNotificationBroadcast.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), 0, intent,
                        PendingIntent.FLAG_IMMUTABLE);


        AlarmManager alarmManager =
                null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        }

        long currentTimeInMilliSeconds = System.currentTimeMillis();

//        alarmManager.set(AlarmManager.RTC_WAKEUP,
//                currentTimeInMilliSeconds, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,currentTimeInMilliSeconds,10,
                pendingIntent);
////////////////////////////////////////////////////////////////////////////////////

        dataSeeds.getFullName(new DataSeeds.fullNameCallBack() {
            @Override
            public void onCallback(String fullName) {
                fullNmeTextView = StudentDashboardActivity.this.findViewById(R.id.fullname);
                fullNmeTextView.setText(fullName);
            }
        });

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        haderXml = navigationView.getHeaderView(0);
        profileImageView = haderXml.findViewById(R.id.profilepicnew);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
//                if (finalUserRole.equals("admin")){
                    navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.student_menu);

//                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id) {
                    case R.id.nav_view_timetable:
                        toolbar.setTitle("View Timetable");
                        replaceFragment(new StudentTimetableFragment());
                        break;

                    case R.id.nav_view_assignment:
                        toolbar.setTitle("Assessment Schedule");
                        item.setChecked(true);
                        replaceFragment(new StudentAssessmentViewFragment());
                        break;

                    case R.id.nav_profile:
                        toolbar.setTitle(R.string.tittle_profile);
                        item.setChecked(true);
                        replaceFragment(new ProfileFragment());
                        break;
                    case R.id.nav_password_change:
                        toolbar.setTitle(R.string.change_password);
                        item.setChecked(true);
                        replaceFragment(new ChangePasswordFragment());
                        break;
                    case R.id.logout:
                        item.setChecked(true);
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(),
                                LoginActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "Timetable and Assessment";
            String description = "Channel for reminding the changes";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("timeTableNotification",name,
                    importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}