package com.ceft.perto;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.ceft.perto.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rbddevs.splashy.Splashy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
       new Splashy(this)  // For JAVA : new Splashy(this)
                .setLogo(R.drawable.perto_logo)
                .setTitle("")
               .setSubTitle("Welcome to Perto").setSubTitleColor(R.color.black).setSubTitleSize(35)
               .setProgressColor(R.color.white)
               .setBackgroundColor(R.color.white)
               .setFullScreen(true)
               .setTime(5000)
                .show();


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //  AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
        //  R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        //   .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }


}