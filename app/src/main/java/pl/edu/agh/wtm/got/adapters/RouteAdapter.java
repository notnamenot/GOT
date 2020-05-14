package pl.edu.agh.wtm.got.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompatExtras;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.edu.agh.wtm.got.R;
import pl.edu.agh.wtm.got.models.Route;

//responsible for displaying items in recycler view
public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    List<Route> routeList;

    public RouteAdapter(List<Route> routeList) {
        this.routeList = routeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rv_row_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvPointsVal.setText(String.valueOf(routeList.get(position).getPoints()));
        holder.tvLengthVal.setText(routeList.get(position).getLength() + "km");
        holder.tvTimeVal.setText(convertIntToTime(routeList.get(position).getTime()));
        holder.tvUpsVal.setText(routeList.get(position).getSumUps() + "m");
        holder.tvDownsVal.setText(routeList.get(position).getSumDowns() + "m");
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    // responsible for managing the rows
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvPointsVal, tvLengthVal, tvTimeVal, tvUpsVal, tvDownsVal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPointsVal = itemView.findViewById(R.id.tv_points_val);
            tvLengthVal = itemView.findViewById(R.id.tv_length_val);
            tvTimeVal = itemView.findViewById(R.id.tv_time_val);
            tvUpsVal = itemView.findViewById(R.id.tv_ups_val);
            tvDownsVal = itemView.findViewById(R.id.tv_downs_val);

            itemView.setOnClickListener(this);
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
        }
    }

    private String convertIntToTime(int time) {
        int h = time / 60;
        int m = time % 60;
        return h + ":" + m + "h";
    }
}



