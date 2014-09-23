package com.example.android.swn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by kumudini on 9/23/14.
 */
public class RegionReportFragment extends Fragment {

    private ArrayAdapter<String> sourceAdapter;

    public RegionReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList dummyList = new ArrayList();
        dummyList.add("Sump\nLine 2");
        dummyList.add("Source 2");
        sourceAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_main, // The name of the layout ID.
                        R.id.list_item_source_textview, // The ID of the textview to populate.
                        dummyList);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_source);
        listView.setAdapter(sourceAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detailsIntent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, sourceAdapter.getItem(i));
                startActivity(detailsIntent);
            }
        });
        return rootView;
    }

}
