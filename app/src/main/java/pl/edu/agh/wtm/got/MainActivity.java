package pl.edu.agh.wtm.got;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Button btn_search;
    Button btn_trips;
    Button btn_badges;
    Button btn_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_bg));
        }

        btn_search = findViewById(R.id.btn_search_route);
        btn_trips = findViewById(R.id.btn_trips);
        btn_badges = findViewById(R.id.btn_badges);
//        btn_test = findViewById(R.id.btn_test);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSearchView = new Intent(v.getContext(),SearchActivity.class);
                startActivity(goToSearchView);
            }
        });

        btn_trips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent goToTripsView = new Intent(v.getContext(),TripsActivity.class);
//                startActivity(goToTripsView);
                Intent goToTestTripsView = new Intent(v.getContext(),NewTripsActivity.class);
                startActivity(goToTestTripsView);
            }
        });

        btn_badges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAchievementsView = new Intent(v.getContext(),BadgesActivity.class);
                startActivity(goToAchievementsView);
            }
        });

//        btn_test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent goToTestTripsView = new Intent(v.getContext(),NewTripsActivity.class);
//                startActivity(goToTestTripsView);
//            }
//        });
    }
}