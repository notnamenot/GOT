package pl.edu.agh.wtm.got;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

import pl.edu.agh.wtm.got.models.Trip;

public class NewTripsActivity extends AppCompatActivity implements TripsListFragment.TripListListener{
//
    public static final String TAG_MASTER = "MASTER";
    GOTdao dao;
    boolean dualPane;
    List<Trip> trips;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_trips);
//
//        dao = new GOTdao(this);
//        trips = dao.getAllTrips();
//
//        TripsLandFragmentMaster fragmentMaster;
//        FrameLayout frameLayout = findViewById(R.id.fl_trip_master_portrait);
//
//        if (frameLayout != null) {
//            dualPane = false;
//            FragmentTransaction transaction =
//        }
//
//
//    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trips);

        if (savedInstanceState == null) {
            System.out.println("savedInstanceState == null");
            dao = new GOTdao(this);
            List<Trip> trips = dao.getAllTrips();

            // set list fragment
            TripsListFragment tripsListFragment = new TripsListFragment();
//            getSupportFragmentManager().beginTransaction().add(R.id.fl_trip_list, tripsListFragment).commit();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fl_trip_list, tripsListFragment);
            fragmentTransaction.commit();


            // set detail fragment
            if (findViewById(R.id.fl_trip_detail) != null) {
                System.out.println("NewTripsActivity.onCreate findViewById(R.id.fl_trip_detail) != null");
                Bundle arguments = new Bundle();
                arguments.putInt(TripDetailFragment.ARG_ITEM_POS, 0);
                TripDetailFragment tripDetailFragment = new TripDetailFragment();
                tripDetailFragment.setArguments(arguments);
//                getSupportFragmentManager().beginTransaction().add(R.id.fl_trip_detail, fragment).commit();

                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fl_trip_detail, tripDetailFragment);
                fragmentTransaction.commit();
            }
        }
        else {System.out.println("savedInstanceState != null");}

        Toast.makeText(this ,"Dotknij by zobaczyć szczegóły",Toast.LENGTH_LONG).show();
    }

    @Override
    public void itemClicked(int pos) {
        if (findViewById(R.id.fl_trip_detail) == null) {
            System.out.println("itemClicked + fl_trip_detail==null");
            // start detail activity
            Intent detailIntent = new Intent(this, TripDetailActivity.class);
            detailIntent.putExtra(TripDetailFragment.ARG_ITEM_POS, pos);
            startActivity(detailIntent);
        } else {
            System.out.println("itemClicked + fl_trip_detail!=null");
            // replace detail fragment
            Bundle arguments = new Bundle();
            arguments.putInt(TripDetailFragment.ARG_ITEM_POS, pos);
            TripDetailFragment fragment = new TripDetailFragment();
            fragment.setArguments(arguments);

//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_trip_detail, fragment);
//            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
