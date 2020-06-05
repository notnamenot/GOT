package pl.edu.agh.wtm.got;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

public class NoTripsFragment extends Fragment
{
    Button btnSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.no_trips,container,false);

        btnSearch = view.findViewById(R.id.btn_search_route);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSearchView = new Intent(v.getContext(),SearchActivity.class);
                goToSearchView.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);//Intent.FLAG_ACTIVITY_SINGLE_TOP); //TODO kasować back stack
                startActivity(goToSearchView);
            }
        });

        return view;
    }
}
