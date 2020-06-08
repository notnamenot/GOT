package pl.edu.agh.wtm.got;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.wtm.got.models.Trip;

import static pl.edu.agh.wtm.got.Utils.convertIntToTime;
import static pl.edu.agh.wtm.got.Utils.metersToKilometers;

public class BadgesActivity extends AppCompatActivity {

    TextView tvPointsVal, tvLengthVal, tvTimeVal, tvUpsVal, tvDownsVal;
    GOTdao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_bg));
        }

        tvPointsVal = findViewById(R.id.tv_points_val1);
        tvLengthVal = findViewById(R.id.tv_length_val);
        tvTimeVal = findViewById(R.id.tv_time_val);
        tvUpsVal = findViewById(R.id.tv_ups_val);
        tvDownsVal = findViewById(R.id.tv_downs_val);

        dao = new GOTdao(BadgesActivity.this);

        List<Trip> allTrips = dao.getAllTrips();

        int points=0, length=0, time=0, ups=0, downs=0;

        for (Trip trip : allTrips) {
            points += trip.getPoints();
            length += trip.getLength();
            time += trip.getTime();
            ups += trip.getUps();
            downs += trip.getDowns();
        }

        tvPointsVal.setText(points + " punktów");
        tvLengthVal.setText(length + " km");
        tvTimeVal.setText(convertIntToTime(time));
        tvUpsVal.setText(metersToKilometers(ups) + " km");
        tvDownsVal.setText(metersToKilometers(downs) + " km");
    }
}
