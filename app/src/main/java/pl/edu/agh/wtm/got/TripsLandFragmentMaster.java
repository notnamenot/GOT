package pl.edu.agh.wtm.got;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;

//import androidx.fragment.app.Fragment;
//
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link TripsLandFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class TripsLandFragmentMaster extends Fragment {

    private ViewGroup container;
    private LayoutInflater layoutInflater;
    private TextView tvDateVal, tvFromVal, tvToVal;

    public TripsLandFragmentMaster() {}

    public View initializeUI() {
        View view;

        // if there is already a layout inflated, remove it
        if (container != null) {
            container.removeAllViewsInLayout();
        }

        int orientation = getActivity().getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            view = layoutInflater.inflate(R.layout.fragment_trips_land_master,container,false); //TODO
        }
        else {
            view = layoutInflater.inflate(R.layout.fragment_trips_land_master,container,false);
        }

        tvDateVal = view.findViewById(R.id.tv_date_val);
        tvFromVal = view.findViewById(R.id.tv_from_vall);
        tvToVal = view.findViewById(R.id.tv_to_vall);

        return view;
    }




//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public TripsLandFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment TripsLandFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static TripsLandFragment newInstance(String param1, String param2) {
//        TripsLandFragment fragment = new TripsLandFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_trips_land_master, container, false);
//    }
}
