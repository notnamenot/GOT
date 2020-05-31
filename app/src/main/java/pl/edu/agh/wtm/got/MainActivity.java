package pl.edu.agh.wtm.got;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Button btn_search;
    Button btn_trips;
    Button btn_badges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_search = findViewById(R.id.btn_search_route);
        btn_trips = findViewById(R.id.btn_trips);
        btn_badges = findViewById(R.id.btn_badges);

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
                Intent goToTripsView = new Intent(v.getContext(),TripsActivity.class);
                startActivity(goToTripsView);
            }
        });
    }
}