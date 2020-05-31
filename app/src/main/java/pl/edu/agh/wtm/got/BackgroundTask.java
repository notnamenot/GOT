package pl.edu.agh.wtm.got;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import pl.edu.agh.wtm.got.models.Route;

//        1. to co leci do doInBackground, 3. to co wraca z do  in background
public class BackgroundTask extends AsyncTask<String,Void,String> {

    private Context ctx;
    private Route route;
    private GOTdao dao;

    public BackgroundTask(Context ctx, GOTdao dao, Route route) {
        this.ctx = ctx;
        this.route = route;
        this.dao = dao;
    }

    @Override
    protected String doInBackground(String... params) {


        String method = params[0];
        if (method.equals("add_trip")) {
            dao.insertTrip(route);
            return "Wycieczka dodana";
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
    }
}
