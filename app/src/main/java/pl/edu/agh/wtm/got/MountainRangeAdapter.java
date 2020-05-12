package pl.edu.agh.wtm.got;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.wtm.got.models.MountainRange;

public class MountainRangeAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<MountainRange> mountainRangeList;

    public MountainRangeAdapter(Activity mActivity, List<MountainRange> mountainRangeList) {
        this.mActivity = mActivity;
        this.mountainRangeList = mountainRangeList;
    }

    @Override
    public int getCount() { return mountainRangeList.size(); }

    @Override
    public MountainRange getItem(int position) {
        return mountainRangeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View oneMountainRangeLine;

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //robi instancje z xmla
        oneMountainRangeLine = inflater.inflate(R.layout.one_line_spinner_divider,parent,false);// specify which layout we want to inflate

        MountainRange mountainRange = this.getItem(position);

        TextView mountainRangeName = oneMountainRangeLine.findViewById(R.id.my_spinner);
        mountainRangeName.setText(mountainRange.getName());

        return oneMountainRangeLine;
    }
}
