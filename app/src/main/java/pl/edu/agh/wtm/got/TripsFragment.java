package pl.edu.agh.wtm.got;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.edu.agh.wtm.got.adapters.TripAdapter;
import pl.edu.agh.wtm.got.models.Trip;

public class TripsFragment extends Fragment {

    GOTdao dao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dao = new GOTdao(getActivity());

        List<Trip> trips = dao.getAllTrips();

        View view = inflater.inflate(R.layout.rv_trips_fragment,container,false);

        RecyclerView rvTrips = view.findViewById(R.id.rv_trips);
        TripAdapter tripAdapter = new TripAdapter(getActivity(),dao,getFragmentManager(),trips);
        rvTrips.setAdapter(tripAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rvTrips.addItemDecoration(dividerItemDecoration);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvTrips.setLayoutManager(layoutManager);

        return view;
    }
}
