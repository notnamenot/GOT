package pl.edu.agh.wtm.got;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.wtm.got.models.Trip;

import static pl.edu.agh.wtm.got.Utils.convertIntToTime;

public class TripDetailFragment extends Fragment {
    public static final String ARG_ITEM_POS = "tripPOS";
    private Trip trip;
    GOTdao dao;

    public TripDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = new GOTdao(getActivity());
        List<Trip> trips = dao.getAllTrips();

        if (getArguments().containsKey(ARG_ITEM_POS)) {
            trip = trips.get(getArguments().getInt(ARG_ITEM_POS));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.route_properties_row_item_2, container, false);

        System.out.println("TripDetailFragment.onCreateView trip"+trip);
        if (trip != null) { // TODO zmienic layout
            TextView tvPoints = v.findViewById(R.id.tv_points_val);
            tvPoints.setText(String.valueOf(trip.getPoints()));

            TextView tvLength = v.findViewById(R.id.tv_length_val);
            tvLength.setText(String.valueOf(trip.getLength())+"km");

            TextView tvTime = v.findViewById(R.id.tv_time_val);
            tvTime.setText(convertIntToTime(trip.getTime()));

            TextView tvUpsVal = v.findViewById(R.id.tv_ups_val);
            tvUpsVal.setText(String.valueOf(trip.getUps())+"m");

            TextView tvDownsVal = v.findViewById(R.id.tv_downs_val);
            tvDownsVal.setText(String.valueOf(trip.getDowns())+"m");

//            ListView lvTrips =
        }

        return v;
    }



}
