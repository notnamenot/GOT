package pl.edu.agh.wtm.got;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class TripDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_bg));
        }

        if (savedInstanceState == null) {
            TripDetailFragment tripDetailFragment = new TripDetailFragment();
            Bundle arguments = new Bundle();

            System.out.println("ARG_ITEM_POS" + getIntent().getIntExtra(TripDetailFragment.ARG_ITEM_POS, 0));
            arguments.putInt(TripDetailFragment.ARG_ITEM_POS, getIntent().getIntExtra(TripDetailFragment.ARG_ITEM_POS, 0));
            tripDetailFragment.setArguments(arguments);

//            getSupportFragmentManager().beginTransaction().add(R.id.fl_trip_detail, fragment).commit();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fl_trip_detail, tripDetailFragment); //w miejse placeholder ląduje lista
            fragmentTransaction.commit();

        }
    }
}
