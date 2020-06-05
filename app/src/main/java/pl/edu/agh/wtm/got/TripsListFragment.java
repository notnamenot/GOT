package pl.edu.agh.wtm.got;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.List;

import pl.edu.agh.wtm.got.adapters.TripMasterAdapter;
import pl.edu.agh.wtm.got.models.Trip;

public class TripsListFragment extends ListFragment {
    TripMasterAdapter adapter;
    GOTdao dao;
    List<Trip> trips;
    private ListView lvTrips;

    static  interface TripListListener {
        void itemClicked(int pos);
    }

    private TripListListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.lv_trip, container,false);
//        lvTrips = view.findViewById(R.id.list);
//        lvTrips.setAdapter(adapter);
//        return view;
        return inflater.inflate(R.layout.lv_trip, container, false); //fragment_trips_land_master
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dao = new GOTdao(getActivity());
        trips = dao.getAllTrips();

        System.out.println("trips: "+ trips);
        adapter = new TripMasterAdapter(getActivity(),trips, dao);
        setListAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.listener = (TripListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TripListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Trip trip = trips.get(position);

        Toast.makeText(getActivity(), trip.getDate(), Toast.LENGTH_SHORT).show();

        if (listener != null) {
            listener.itemClicked(position);
        }
    }

}
