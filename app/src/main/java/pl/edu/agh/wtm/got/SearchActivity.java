//package pl.edu.agh.wtm.got;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//
//import java.util.List;
//
//import pl.edu.agh.wtm.got.models.GOTPoint;

//public class SearchActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//    }
//}

package pl.edu.agh.wtm.got;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.wtm.got.adapters.AutoCompleteGOTPointAdapter;
import pl.edu.agh.wtm.got.adapters.MountainChainAdapter;
import pl.edu.agh.wtm.got.adapters.MountainRangeAdapter;
import pl.edu.agh.wtm.got.adapters.RouteAdapter;
import pl.edu.agh.wtm.got.models.GOTPoint;
import pl.edu.agh.wtm.got.models.MountainChain;
import pl.edu.agh.wtm.got.models.MountainRange;
import pl.edu.agh.wtm.got.models.Route;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    private static final String SELECTED_MOUNTAIN_RANGE_ID = "MountainRangeId";
    private static final String SELECTED_MOUNTAIN_CHAIN_ID = "MountainChainId";
    private static final String SELECTED_START_POINT_ID = "StartPointId";
    private static final String SELECTED_END_POINT_ID = "EndPointId";
    //reference to controls on layout
    Button btnSearch;
    Spinner spnMountainRanges;
    Spinner spnMountainChains;
    AutoCompleteTextView actvStartPoint;
    AutoCompleteTextView actvEndPoint;
    RecyclerView rvRoutes;

//    ArrayAdapter arrayAdapter;
//    ArrayAdapter mountainRangeAdapter;
//    ArrayAdapter mountainChainAdapter;
    MountainRangeAdapter mountainRangeAdapter;
    MountainChainAdapter mountainChainAdapter;
    AutoCompleteGOTPointAdapter GOTPointAdapter;
    RouteAdapter routeAdapter;

    MountainRange promptMountainRange;// = new MountainRange(0, getApplicationContext().getString(R.string.select_mountain_range),0,"");
    MountainChain promptMountainChain;// = new MountainChain(0,getApplicationContext().getString(R.string.select_mountain_chain),0,0);


    List<MountainChain> mountainChainSuggestions;
    List<GOTPoint> GOTPointSuggestions;
    List<Route> possibleRoutes;

    GOTdao dao;

    MountainRange selectedMountainRange;
    MountainChain selectedMountainChain;
    GOTPoint startPoint;
    GOTPoint endPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) { //starts the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_bg));
        }

        spnMountainRanges = findViewById(R.id.spn_mountain_ranges);
        spnMountainChains = findViewById(R.id.spn_mountain_chains);
        actvStartPoint = findViewById(R.id.actv_start_point);
        actvEndPoint = findViewById(R.id.actv_end_point);
        btnSearch = findViewById(R.id.btn_search); //R od resource folder
        rvRoutes = findViewById(R.id.rv_routes);

        dao = new GOTdao(SearchActivity.this);

        promptMountainRange = new MountainRange(0, getApplicationContext().getString(R.string.select_mountain_range),0,"");
        List<MountainRange> mountainRangeList = new ArrayList<MountainRange>(){
            {
                add(promptMountainRange);
                addAll(dao.getAllMountainRanges());
            }
        };
        mountainRangeAdapter = new MountainRangeAdapter(SearchActivity.this, mountainRangeList);
        spnMountainRanges.setAdapter(mountainRangeAdapter);

        promptMountainChain = new MountainChain(0,getApplicationContext().getString(R.string.select_mountain_chain),0,0);
        mountainChainSuggestions = new ArrayList<MountainChain>() {
            {
                add(promptMountainChain);
            }
        };
        mountainChainAdapter = new MountainChainAdapter(SearchActivity.this, mountainChainSuggestions);
        spnMountainChains.setAdapter(mountainChainAdapter); // associate adapter with control on the screen

        GOTPointSuggestions = new ArrayList<GOTPoint>();
        GOTPointAdapter = new AutoCompleteGOTPointAdapter( SearchActivity.this, GOTPointSuggestions);
        actvStartPoint.setAdapter(GOTPointAdapter);
        actvEndPoint.setAdapter(GOTPointAdapter);

        possibleRoutes = new ArrayList<>();
        routeAdapter = new RouteAdapter(this, possibleRoutes);
        rvRoutes.setLayoutManager(new LinearLayoutManager(this));
        rvRoutes.setAdapter(routeAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvRoutes.addItemDecoration(dividerItemDecoration);


        spnMountainRanges.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMountainRange = (MountainRange) spnMountainRanges.getSelectedItem();

                List<MountainChain> filteredMountainChains = dao.getMountainChains(selectedMountainRange.getId());
                mountainChainSuggestions.clear();
                mountainChainSuggestions.add(promptMountainChain);
                mountainChainSuggestions.addAll(filteredMountainChains);
                mountainChainAdapter.notifyDataSetChanged();

//                    edtStartPoint.clearListSelection();
                actvStartPoint.setText("");
                actvEndPoint.setText("");
                actvStartPoint.setHint(getResources().getString(R.string.start_point));
                actvEndPoint.setHint(getResources().getString(R.string.end_point));
                startPoint = null;
                endPoint = null;

//                System.out.println("before asynctask");
//                new ReloadMountainChainSuggestions(mountainRange.getId()).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spnMountainChains.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMountainChain = (MountainChain) spnMountainChains.getSelectedItem();

                List<GOTPoint> filteredGOTPoints = dao.getGOTPoints(selectedMountainChain.getId());

                GOTPointSuggestions.clear();
                GOTPointSuggestions.addAll(filteredGOTPoints);
                GOTPointAdapter.notifyDataSetChangedAll();

                actvStartPoint.setText("");
                actvEndPoint.setText("");
                startPoint = null;
                endPoint = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        actvStartPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //setOnItemSelectedListener
                startPoint = GOTPointAdapter.getItem(position);
//                Toast.makeText(SearchActivity.this,
//                        GOTPointAdapter.getItem(position).toString(),
//                        Toast.LENGTH_SHORT).show();
            }
        });

        actvEndPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                endPoint = GOTPointAdapter.getItem(position);
//                Toast.makeText(SearchActivity.this,
//                        GOTPointAdapter.getItem(position).toString(),
//                        Toast.LENGTH_SHORT).show();
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO diseable button when points arent chosen
                if (startPoint != null && endPoint != null) {
                    Graph g = new Graph(startPoint.getId(), endPoint.getId(), startPoint.getMountainChainId(), SearchActivity.this);
                    possibleRoutes.clear();
                    possibleRoutes.addAll(g.getFoundRoutes());
                    routeAdapter.notifyDataSetChanged();

                    Toast.makeText(SearchActivity.this, getResources().getText(R.string.press_route), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (selectedMountainRange != null && selectedMountainChain != null) {
            outState.putInt(SELECTED_MOUNTAIN_RANGE_ID, selectedMountainRange.getId());
            outState.putInt(SELECTED_MOUNTAIN_CHAIN_ID, selectedMountainChain.getId());
        }
        if (startPoint != null && endPoint != null) {
            outState.putInt(SELECTED_START_POINT_ID, startPoint.getId());
            outState.putInt(SELECTED_END_POINT_ID, endPoint.getId());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        List<MountainChain> filteredMountainChains = dao.getMountainChains(savedInstanceState.getInt(SELECTED_MOUNTAIN_RANGE_ID));
        mountainChainSuggestions.clear();
        mountainChainSuggestions.add(promptMountainChain);
        mountainChainSuggestions.addAll(filteredMountainChains);
        mountainChainAdapter.notifyDataSetChanged();

        List<GOTPoint> filteredGOTPoints = dao.getGOTPoints(savedInstanceState.getInt(SELECTED_MOUNTAIN_CHAIN_ID));
        GOTPointSuggestions.clear();
        GOTPointSuggestions.addAll(filteredGOTPoints);
        GOTPointAdapter.notifyDataSetChangedAll();
        // TODO restore mountainchain spinner
        System.out.println("start:" + savedInstanceState.getInt(SELECTED_START_POINT_ID) + " end:" + savedInstanceState.getInt(SELECTED_END_POINT_ID));
        startPoint = dao.getGOTPoint(savedInstanceState.getInt(SELECTED_START_POINT_ID));
        endPoint = dao.getGOTPoint(savedInstanceState.getInt(SELECTED_END_POINT_ID));
        if (startPoint != null && endPoint != null) {

            actvStartPoint.setHint(startPoint.getName());
            actvEndPoint.setHint(endPoint.getName());

            Graph g = new Graph(startPoint.getId(), endPoint.getId(), startPoint.getMountainChainId(), SearchActivity.this);
            possibleRoutes.clear();
            possibleRoutes.addAll(g.getFoundRoutes());
            routeAdapter.notifyDataSetChanged();
        }
    }

    //
//    private class ReloadMountainChainSuggestions extends AsyncTask<Void,Void,Void> {
//        private int mountainRangeId;
//
//        ReloadMountainChainSuggestions(int mountainRangeId){
//            super();
//            this.mountainRangeId = mountainRangeId;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            Log.d(TAG, Thread.currentThread().getStackTrace()[0].getMethodName());
//            List<MountainChain> filteredMountainChains = dao.getMountainChains(mountainRangeId);
//
//            mountainChainSuggestions.clear();
//            mountainChainSuggestions.add(promptMountainChain);
//            mountainChainSuggestions.addAll(filteredMountainChains);
//            for (MountainChain m : mountainChainSuggestions) System.out.println(m);
//            mountainChainAdapter.notifyDataSetChanged();
//            return null;
//        }
//
//    }
}
