package pl.edu.agh.wtm.got.adapters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.edu.agh.wtm.got.BackgroundTask;
import pl.edu.agh.wtm.got.GOTdao;
import pl.edu.agh.wtm.got.NoTripsFragment;
import pl.edu.agh.wtm.got.R;
import pl.edu.agh.wtm.got.models.Route;
import pl.edu.agh.wtm.got.models.Trip;


public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    List<Trip> tripList;
    private Context mContext;
    GOTdao dao;
    FragmentManager fragmentManager;

    public TripAdapter(Context mContext, GOTdao dao, FragmentManager fragmentManager, List<Trip> tripList) {
        this.tripList = tripList;
        this.mContext = mContext;
        this.dao = dao;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rv_trip_row_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Trip trip = tripList.get(position);
        String gotPointFrom = dao.getGOTPoint(trip.getFrom()).getName();
        String gotPointTo = dao.getGOTPoint(trip.getTo()).getName();

        holder.tvDateVal.setText(trip.getDate()); // tripList.get(position).getDate()
        holder.tvFromVal.setText(gotPointFrom);
        holder.tvToVal.setText(gotPointTo);

        holder.tvPointsVal.setText(String.valueOf(trip.getPoints()));
        holder.tvLengthVal.setText(trip.getLength() + "km");
        holder.tvTimeVal.setText(convertIntToTime(trip.getTime()));
        holder.tvUpsVal.setText(trip.getUps() + "m");
        holder.tvDownsVal.setText(trip.getDowns() + "m");

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvDateVal, tvFromVal, tvToVal;
        TextView tvPointsVal, tvLengthVal, tvTimeVal, tvUpsVal, tvDownsVal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDateVal = itemView.findViewById(R.id.tv_date_val);
            tvFromVal = itemView.findViewById(R.id.tv_from_val); // TODO change on src and dest
            tvToVal = itemView.findViewById(R.id.tv_to_val);

            tvPointsVal = itemView.findViewById(R.id.tv_points_val);
            tvLengthVal = itemView.findViewById(R.id.tv_length_val);
            tvTimeVal = itemView.findViewById(R.id.tv_time_val);
            tvUpsVal = itemView.findViewById(R.id.tv_ups_val);
            tvDownsVal = itemView.findViewById(R.id.tv_downs_val);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();

            Trip trip = tripList.get(position);
            System.out.println(trip.getPoints() + " looooooooong click");
//
//            BackgroundTask backgroundTask = new BackgroundTask(mContext,dao,trip);
//            backgroundTask.execute("remove_trip");
            dao.removeTrip(trip);
            Toast.makeText(mContext,"Wycieczka usunięta",Toast.LENGTH_LONG).show();
            tripList.clear();
            tripList.addAll(dao.getAllTrips());

            if (tripList.size() == 0)
            {
                NoTripsFragment NoTripsFragment = new NoTripsFragment();
//                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.placeholder, NoTripsFragment); //w miejse placeholder tekst że nie ma wycieczek
                fragmentTransaction.commit();
            }
            else {
                notifyDataSetChanged();
            }

            return false;
        }
    }

    private String convertIntToTime(int time) {
        int h = time / 60;
        int m = time % 60;
        return h + ":" + m + "h";
    }
}
