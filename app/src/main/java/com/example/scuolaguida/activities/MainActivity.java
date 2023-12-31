package com.example.scuolaguida.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.scuolaguida.R;
import com.example.scuolaguida.databinding.ActivityMainBinding;
import com.example.scuolaguida.fragments.LogFragment;
import com.example.scuolaguida.fragments.LoginFragment;
import com.example.scuolaguida.fragments.SignupFragment;
import com.example.scuolaguida.models.FirebaseWrapper;
import com.example.scuolaguida.models.MyWorker;
import com.example.scuolaguida.models.PermissionManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private void goToActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        this.startActivity(intent);
        this.finish();
    }
    private static final String TAG = MainActivity.class.getCanonicalName();
    androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
    DrawerLayout drawerLayout;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseWrapper.Auth auth = new FirebaseWrapper.Auth();
        if (!auth.isAuthenticated()) {
            // Go to Activity for LogIn or SignUp
            this.goToActivity(EnterActivity.class);
        }
        FirebaseUser user = auth.getUser();
        drawerLayout = findViewById(R.id.drawer_layout);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profilo, R.id.nav_impostazioni)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class, 24, TimeUnit.HOURS,23, TimeUnit.HOURS)
                .addTag(TAG).setInitialDelay(0, TimeUnit.HOURS).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);


        ImageView italia = findViewById(R.id.linguaitaliano);
        ImageView inglese = findViewById(R.id.linguainglese);
        italia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale = new Locale("it-rIT");
                Configuration configuration = getResources().getConfiguration();
                configuration.setLocale(locale);
                getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());
                Intent intent = MainActivity.this.getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        inglese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale = new Locale("en");
                Configuration configuration = getResources().getConfiguration();
                configuration.setLocale(locale);
                getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());
                Intent intent = MainActivity.this.getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void renderFragment(boolean isLogin) {
        Fragment fragment = null;
        if (isLogin) {
            fragment = LogFragment.newInstance(LoginFragment.class, "signinCallback", boolean.class);
        } else {
            fragment = LogFragment.newInstance(SignupFragment.class, "signinCallback", boolean.class);
        }
        if (this.fragmentManager == null) {
            this.fragmentManager = this.getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.app_bar_main, fragment);
        fragmentTransaction.commit();
    }

}