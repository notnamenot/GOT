package pl.edu.agh.wtm.got.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.wtm.got.GOTdao;
import pl.edu.agh.wtm.got.R;
import pl.edu.agh.wtm.got.models.Trip;

public class TripMasterAdapter extends BaseAdapter {

    private Context mContext;
    private List<Trip> tripList;
    private GOTdao dao;

    public TripMasterAdapter(Context mContext, List<Trip> tripList, GOTdao dao) {
        this.mContext = mContext;
        this.tripList = tripList;
        this.dao = dao;
    }

    @Override
    public int getCount() {
        return tripList.size();
    }

    @Override
    public Trip getItem(int position) {
        return tripList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tripList.indexOf(getItem(position));
    }

    private class ViewHolder{
        TextView tvDate;
        TextView tvFrom;
        TextView tvTo;
    }

//    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Trip trip = tripList.get(position);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_trips_land_master, null);

            holder = new ViewHolder();
            holder.tvDate = convertView.findViewById(R.id.tv_date_val);
            holder.tvFrom = convertView.findViewById(R.id.tv_from_vall);
            holder.tvTo = convertView.findViewById(R.id.tv_to_vall);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String gotPointFrom = dao.getGOTPoint(trip.getFrom()).getName();
        String gotPointTo = dao.getGOTPoint(trip.getTo()).getName();

        holder.tvDate.setText(trip.getDate());
        holder.tvFrom.setText(gotPointFrom);
        holder.tvTo.setText(gotPointTo);

        return convertView;
    }
}
