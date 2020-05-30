package pl.edu.agh.wtm.got.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.edu.agh.wtm.got.R;
import pl.edu.agh.wtm.got.models.GOTPoint;


public class GOTPointAdapter extends RecyclerView.Adapter<GOTPointAdapter.ViewHolder> {

//    Activity mActivity;
    List<GOTPoint> GOTPointList;

    public GOTPointAdapter( List<GOTPoint> GOTPointList) {
//        this.mActivity = mActivity;
        this.GOTPointList = GOTPointList;
    }

//    @Override
//    public int getCount() {
//        return GOTPointList.size();
//    }
//
//    @Override
//    public GOTPoint getItem(int position) {
//        return GOTPointList.get(position);
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_got_point_row_item,parent,false);
        return new GOTPointAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(GOTPointList.get(position).getName());
        holder.tvHeight.setText(GOTPointList.get(position).getHeight() + "m");
    }

    @Override
    public long getItemId(int position) { return GOTPointList.get(position).getId(); }

    @Override
    public int getItemCount() {
        return GOTPointList.size();
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) { //converView - oneMountainChainLine
//        if (convertView == null) { // TODO add it to other adapters for better performance
//            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //robi instancje z xmla
//            convertView = inflater.inflate(R.layout.rv_got_point_row_item,parent,false);// specify which layout we want to inflate
//        }
//
//        GOTPoint gotPoint = this.getItem(position);
//
//        TextView tvName = convertView.findViewById(R.id.tv_name);
//        TextView tvHeight = convertView.findViewById(R.id.tv_height);
//
//        tvName.setText(gotPoint.getName());
//        tvHeight.setText(String.valueOf(gotPoint.getHeight()));
//
//        return convertView;
//    }

//    @Override
//    public void notifyDataSetChanged() {
//        super.notifyDataSetChanged();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvHeight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvHeight = itemView.findViewById(R.id.tv_height);
        }
    }

}
