package pl.edu.agh.wtm.got;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.wtm.got.models.GOTPoint;

public class AutoCompleteGOTPointAdapter extends ArrayAdapter<GOTPoint> {

    Context mContext;
    List<GOTPoint> GOTPointList;    // przefiltrowana
    List<GOTPoint> GOTPointListAll; // a copy of passed list// FULL

    public AutoCompleteGOTPointAdapter(@NonNull Context context, @NonNull List<GOTPoint> GOTPointList) {
        super(context, 0, GOTPointList);
        this.mContext = context;
        this.GOTPointList = GOTPointList;
        this.GOTPointListAll = new ArrayList<>(GOTPointList); // żeby nie wskazywały tego samego
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChangedAll() {
        GOTPointListAll.clear(); // musi być dla zmiany z Searchactivity, nie może z wewnątrz
        GOTPointListAll.addAll(GOTPointList);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //to apply custom layout
        if (convertView == null) { // TODO add it to other adapters for better performance
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.one_line_spinner,parent,false); //TODO row zamiast line
        }

        TextView textViewGOTPointName = convertView.findViewById(R.id.my_spinner);

        GOTPoint GOTPoint = this.getItem(position);

        if (GOTPoint != null) {
            textViewGOTPointName.setText(GOTPoint.getName());
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((GOTPoint) resultValue).getName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<GOTPoint> suggestion = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) { //
                    suggestion.addAll(GOTPointListAll);
                }
                else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (GOTPoint gotPoint : GOTPointListAll) {
                        if (gotPoint.getName().toLowerCase().contains(filterPattern)) { //startsWith
                            suggestion.add(gotPoint);
                        }
                    }
                }

                filterResults.values = suggestion;
                filterResults.count = suggestion.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                GOTPointList.clear();
                if (results != null && results.count > 0) {
                    GOTPointList.addAll((List) results.values);

                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    GOTPointList.addAll(GOTPointListAll);
                }
                notifyDataSetChanged();
            }
        };
    }
}
