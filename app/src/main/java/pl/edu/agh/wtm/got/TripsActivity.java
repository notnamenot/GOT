package pl.edu.agh.wtm.got;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.List;

import pl.edu.agh.wtm.got.models.Trip;

public class TripsActivity extends AppCompatActivity {

    GOTdao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        dao = new GOTdao(this);
        List<Trip> trips = dao.getAllTrips();

        if (trips.size() != 0) {
            TripsFragment tripsFragment = new TripsFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.placeholder, tripsFragment); //w miejse placeholder ląduje lista
            fragmentTransaction.commit();
        }
        else {
            NoTripsFragment NoTripsFragment = new NoTripsFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.placeholder, NoTripsFragment); //w miejse placeholder tekst że nie ma wycieczek
            fragmentTransaction.commit();
        }
    }
}
