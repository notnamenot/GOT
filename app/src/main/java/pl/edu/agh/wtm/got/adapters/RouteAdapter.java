package pl.edu.agh.wtm.got.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.edu.agh.wtm.got.GOTdao;
import pl.edu.agh.wtm.got.R;
import pl.edu.agh.wtm.got.models.Route;
import pl.edu.agh.wtm.got.models.Subroute;

//responsible for displaying items in recycler view
public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    List<Route> routeList;
    private Context mContext;
    GOTdao dao;

    public RouteAdapter(Context context, List<Route> routeList) {
        this.mContext = context;
        this.routeList = routeList;
        this.dao = new GOTdao(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rv_route_row_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvPointsVal.setText(String.valueOf(routeList.get(position).getPoints()));
        holder.tvLengthVal.setText(routeList.get(position).getLength() + "km");
        holder.tvTimeVal.setText(convertIntToTime(routeList.get(position).getTime()));
        holder.tvUpsVal.setText(routeList.get(position).getUps() + "m");
        holder.tvDownsVal.setText(routeList.get(position).getDowns() + "m");

        GOTPointAdapter gotPointAdapter = new GOTPointAdapter(routeList.get(position).getGotPoints());
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        holder.rvGotPoints.setLayoutManager(layoutManager);
        holder.rvGotPoints.setAdapter(gotPointAdapter);

    }

    private void calculateRoute(int position) {
        List<Subroute> subroutes = routeList.get(position).getSubroutes();
        int points = 0;
        double length = 0;
        int time = 0;
        int ups = 0;
        int downs = 0;
        for (Subroute subroute : subroutes) {// TODO dostanie już przeliczony, tu do wywalenia!
            points += subroute.getPoints();
            length += subroute.getLength();
            time += subroute.getTime();
            ups += subroute.getUps();
            downs += subroute.getDowns();
        }
        routeList.get(position).setPoints(points);
        routeList.get(position).setLength(length);
        routeList.get(position).setTime(time);
        routeList.get(position).setUps(ups);
        routeList.get(position).setDowns(downs);
    }

    private int calculatePoints(int position) {
        List<Subroute> subroutes = routeList.get(position).getSubroutes();
        int points = 0;
        for (Subroute subroute : subroutes) {
            points += subroute.getPoints();
        }
        return points;
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    // responsible for managing the rows
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvPointsVal, tvLengthVal, tvTimeVal, tvUpsVal, tvDownsVal;
        RecyclerView rvGotPoints;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPointsVal = itemView.findViewById(R.id.tv_points_val);
            tvLengthVal = itemView.findViewById(R.id.tv_length_val);
            tvTimeVal = itemView.findViewById(R.id.tv_time_val);
            tvUpsVal = itemView.findViewById(R.id.tv_ups_val);
            tvDownsVal = itemView.findViewById(R.id.tv_downs_val);
            rvGotPoints = itemView.findViewById(R.id.rv_got_points);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            System.out.println(position);
            Route route = routeList.get(getAdapterPosition());
            System.out.println(route.getPoints());
//            if (getAdapterPosition() == RecyclerView.NO_POSITION)
//                Toast.makeText(v.getContext(), "hello", Toast.LENGTH_SHORT).show();
//            else
//                 Toast.makeText(v.getPgetContext(), position, Toast.LENGTH_SHORT).show();
//            rvGotPoints.setVisibility(View.INVISIBLE);
        }


        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();

            Route route = routeList.get(getAdapterPosition());
            System.out.println(route.getPoints() + " looooooooong click");
            Toast.makeText(mContext,
                    routeList.get(getAdapterPosition()).toString(),
                    Toast.LENGTH_LONG).show();

            dao.insertTrip(route);
            return false;
        }
    }

    private String convertIntToTime(int time) {
        int h = time / 60;
        int m = time % 60;
        return h + ":" + m + "h";
    }
}



