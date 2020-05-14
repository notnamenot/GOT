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
    //reference to controls on layout
    Button btnSearch;
//    EditText edt_start_point;
//    ListView lv;
    Spinner spnMountainRanges;
    Spinner spnMountainChains;
    AutoCompleteTextView edtStartPoint;
    AutoCompleteTextView edtEndPoint;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) { //starts the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        edt_start_point = findViewById(R.id.edt_start_point);
//        lv = findViewById(R.id.lv);
        spnMountainRanges = findViewById(R.id.spn_mountain_ranges);
        spnMountainChains = findViewById(R.id.spn_mountain_chains);
        edtStartPoint = findViewById(R.id.actv_start_point);
        edtEndPoint = findViewById(R.id.actv_end_point);
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
        spnMountainChains.setAdapter(mountainChainAdapter);

//        arrayAdapter = new ArrayAdapter<GOTPoint>(SearchActivity.this,android.R.layout.simple_list_item_1,dao.getAllGOTPoints()); //predefined adapter giving string
//        lv.setAdapter(arrayAdapter); // associate adapter with control on the screen

        GOTPointSuggestions = new ArrayList<GOTPoint>();
        GOTPointAdapter = new AutoCompleteGOTPointAdapter( SearchActivity.this, GOTPointSuggestions);
        edtEndPoint.setAdapter(GOTPointAdapter);
        edtStartPoint.setAdapter(GOTPointAdapter);



        possibleRoutes = new ArrayList<>();
        possibleRoutes.add(new Route(0,10,5.8,420, 200,120));
        possibleRoutes.add(new Route(1,15,7.2,560, 150,30));

        routeAdapter = new RouteAdapter(possibleRoutes);
        rvRoutes.setLayoutManager(new LinearLayoutManager(this));
        rvRoutes.setAdapter(routeAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvRoutes.addItemDecoration(dividerItemDecoration);


        spnMountainRanges.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MountainRange mountainRange = (MountainRange) spnMountainRanges.getSelectedItem();
//                if (0 != mountainRange.getId()) {
                    List<MountainChain> filteredMountainChains = dao.getMountainChains(mountainRange.getId());
                    mountainChainSuggestions.clear();
                    mountainChainSuggestions.add(promptMountainChain);
                    mountainChainSuggestions.addAll(filteredMountainChains);
                    mountainChainAdapter.notifyDataSetChanged();
                    edtStartPoint.clearListSelection();
//                        new reloadMountainChainSuggestions(mountainRange.getId());
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spnMountainChains.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MountainChain mountainChain = (MountainChain) spnMountainChains.getSelectedItem();

                List<GOTPoint> filteredGOTPoints = dao.getGOTPoints(mountainChain.getId());

                GOTPointSuggestions.clear();
                GOTPointSuggestions.addAll(filteredGOTPoints);
                GOTPointAdapter.notifyDataSetChangedAll();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        //button listeners
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                GOTPoint GOTpoint;
//                try {
//                    GOTpoint = new GOTPoint(1,"Orlica",1084,1);
//                } catch (Exception e) {
//                    GOTpoint = new GOTPoint(-1,"Error",0,-1);
//                }

//                GOTdao dao = new GOTdao(SearchActivity.this);
////                boolean success = dao.addOne(GOTpoint);
//
////                Toast.makeText(MainActivity.this,"Success=" + success,Toast.LENGTH_SHORT).show();
//
//                ///////////
//                List<GOTPoint> list = dao.getAllGOTPoints();
////                Toast.makeText(MainActivity.this,list.toString(),Toast.LENGTH_SHORT).show();
//
//                pointsAdapter = new ArrayAdapter<GOTPoint>(SearchActivity.this,android.R.layout.simple_list_item_1,list); //predefined adapter giving string
//                //associate adapter with control on the screen
//                lv.setAdapter(pointsAdapter);

            }
        });

//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //position - whick item was clicked
////                GOTPoint clickedPoint = (GOTPoint) parent.getItemAtPosition(position);
////                dao.deleteOne(clickedPoint);
////
////                GOTPointAdapter = new ArrayAdapter<GOTPoint>(SearchActivity.this,android.R.layout.simple_list_item_1,dao.getAllGOTPoints()); //predefined adapter giving string
////                //associate adapter with control on the screen
////                lv.setAdapter(GOTPointAdapter);
//            }
//        });

    }

    private class reloadMountainChainSuggestions extends AsyncTask<Void,Void,Void> {
        private int mountainRangeId;
        reloadMountainChainSuggestions(int mountainRangeId){
            super();
            this.mountainRangeId = mountainRangeId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, Thread.currentThread().getStackTrace()[0].getMethodName());
            List<MountainChain> filteredMountainChains = dao.getMountainChains(mountainRangeId);

            mountainChainSuggestions.clear();
            mountainChainSuggestions.add(promptMountainChain);
            mountainChainSuggestions.addAll(filteredMountainChains);
            for (MountainChain m : mountainChainSuggestions) System.out.println(m);
            mountainChainAdapter.notifyDataSetChanged();
            return null;
        }
    }
}
