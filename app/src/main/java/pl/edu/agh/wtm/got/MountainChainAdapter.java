package pl.edu.agh.wtm.got;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.wtm.got.models.MountainChain;

public class MountainChainAdapter extends BaseAdapter {

    Activity mActivity;
    List<MountainChain> mountainChainList;

    public MountainChainAdapter(Activity mActivity, List<MountainChain> mountainChainList) {
        this.mActivity = mActivity;
        this.mountainChainList = mountainChainList;
    }

    @Override
    public int getCount() {
        return mountainChainList.size();
    }

    @Override
    public MountainChain getItem(int position) {
        return mountainChainList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //converView - oneMountainChainLine
        if (convertView == null) { // TODO add it to other adapters for better performance
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //robi instancje z xmla
            convertView = inflater.inflate(R.layout.one_line_spinner,parent,false);// specify which layout we want to inflate
        }

        MountainChain mountainChainItem = this.getItem(position);

        TextView tvMountainChainName = convertView.findViewById(R.id.my_spinner);
        tvMountainChainName.setText(mountainChainItem.getName());

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}

