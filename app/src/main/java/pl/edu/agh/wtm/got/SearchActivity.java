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

        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.Toast;

        import java.util.List;

        import pl.edu.agh.wtm.got.models.GOTPoint;

public class SearchActivity extends AppCompatActivity {

    //reference to controls on layout
    Button btn_search;
    EditText edt_start_point;
    ListView lv;

    ArrayAdapter pointsAdapter;
    GOTdao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //starts the app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btn_search = findViewById(R.id.btn_search); //R od resource folder
        edt_start_point = findViewById(R.id.edt_start_point);
        lv = findViewById(R.id.lv);

        dao = new GOTdao(SearchActivity.this);

//        GOTPoint GOTpoint;
//        GOTpoint = new GOTPoint(1,"Orlica",1084,1);
//        boolean success = dao.addOne(GOTpoint);

        pointsAdapter = new ArrayAdapter<GOTPoint>(SearchActivity.this,android.R.layout.simple_list_item_1,dao.getAllGOTPoints()); //predefined adapter giving string
//        associate adapter with control on the screen
        lv.setAdapter(pointsAdapter);


        //button listeners
        btn_search.setOnClickListener(new View.OnClickListener() {
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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //position - whick item was clicked
                GOTPoint clickedPoint = (GOTPoint) parent.getItemAtPosition(position);
                dao.deleteOne(clickedPoint);

                pointsAdapter = new ArrayAdapter<GOTPoint>(SearchActivity.this,android.R.layout.simple_list_item_1,dao.getAllGOTPoints()); //predefined adapter giving string
                //associate adapter with control on the screen
                lv.setAdapter(pointsAdapter);
            }
        });

    }

}
