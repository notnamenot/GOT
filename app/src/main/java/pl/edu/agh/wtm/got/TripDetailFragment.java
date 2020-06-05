package pl.edu.agh.wtm.got;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.wtm.got.models.Trip;

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

        System.out.println("TripDetailFragment.onCreate getArguments().getInt(ARG_ITEM_POS)" + getArguments().getInt(ARG_ITEM_POS) );
        if (getArguments().containsKey(ARG_ITEM_POS)) {
            System.out.println("if (getArguments().containsKey(ARG_ITEM_POS)) ");
            trip = trips.get(getArguments().getInt(ARG_ITEM_POS));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.route_properties_row_item, container, false);

        System.out.println("TripDetailFragment.onCreateView trip"+trip);
        if (trip != null) {
            TextView tvTitle = v.findViewById(R.id.tv_points_val);
            tvTitle.setText(String.valueOf(trip.getPoints()));

            TextView tvYear = v.findViewById(R.id.tv_length_val);
            tvYear.setText(String.valueOf(trip.getLength()));
        }

        return v;
    }

}
