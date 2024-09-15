package com.codebee.waterrider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.productFragmentContainer,HomeFragment.class, null)
                .commit();


        navigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId==R.id.navHome){

                System.out.println(itemId);
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.productFragmentContainer, HomeFragment.class, null)
                        .commit();

            } else if (itemId==R.id.navtrack) {

                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.productFragmentContainer, TrackOrderFragment.class, null)
                        .commit();


            } else if (itemId==R.id.navchat) {

                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.productFragmentContainer, ChatFragment.class, null)
                        .commit();


            }



            return true;
        });

    }
}