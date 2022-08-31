package yuken.example.ntu_timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboard extends AppCompatActivity {
    ImageView profileImageView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    View haderXml;
    TextView fullNmeTextView;
    DataSeeds dataSeeds =new DataSeeds();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        dataSeeds.getFullName(new DataSeeds.fullNameCallBack() {
            @Override
            public void onCallback(String fullName) {
                fullNmeTextView = AdminDashboard.this.findViewById(R.id.fullname);
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
                    navigationView.inflateMenu(R.menu.admin_menu);

//                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id) {
                    case R.id.nav_timetable:
                        toolbar.setTitle(R.string.create_timetable);
                        item.setChecked(true);
                        replaceFragment(new TimeTableCreateFragment());
                        break;
                    case R.id.nav_view_timetable:
                        toolbar.setTitle("View Timetable");
                        replaceFragment(new ViewTimeTableFragment());
                        break;
                    case R.id.nav_register:
                        toolbar.setTitle(R.string.user_register);
                        item.setChecked(true);
                        replaceFragment(new UserRegistrationFragment());
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
}