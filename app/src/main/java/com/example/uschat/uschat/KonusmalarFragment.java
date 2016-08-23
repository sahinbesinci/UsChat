package com.example.uschat.uschat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.uschat.uschat.Adapter.KisilerAdapter;
import com.example.uschat.uschat.Adapter.KonusmalarAdapter;

import java.util.ArrayList;

/**
 * Created by sahin on 26.04.2016.
 */
public class KonusmalarFragment extends PageFragment {
    SearchView mSearchView;
    ArrayList<String> tmp;
    KonusmalarAdapter konusmalarAdapter;
    ListView lvContent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        lvContent = (ListView) view.findViewById(R.id.lvContent);
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        tmp = new ArrayList<>();
        tmp.add("Beşinci");
        tmp.add("Şahin");
        konusmalarAdapter = new KonusmalarAdapter(getContext(),tmp);
        lvContent.setAdapter(konusmalarAdapter);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> _tmp = new ArrayList<>();
                for (String s : tmp) {
                    if (s.substring(0, newText.length() > s.length() ? s.length() : newText.length()).equals(newText))
                        _tmp.add(s);
                }
                konusmalarAdapter = new KonusmalarAdapter(getContext(), _tmp);
                lvContent.setAdapter(konusmalarAdapter);
                return false;
            }
        });
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(),ChatActivity.class);
                startActivity(i);
            }
        });
        return view;
    }
}