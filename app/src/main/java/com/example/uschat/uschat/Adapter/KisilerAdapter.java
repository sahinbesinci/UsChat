package com.example.uschat.uschat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.uschat.uschat.R;

import java.util.ArrayList;


/**
 * Created by sahin on 26.04.2016.
 */
public class KisilerAdapter extends BaseAdapter implements Filterable{

    ArrayList<String> kisiler;
    Context context;
    private LayoutInflater mLayoutInflater = null;
    CompleteListViewHolder viewHolder;
    public KisilerAdapter(Context context, ArrayList<String> kisiler)
    {
        this.context = context;
        this.kisiler = kisiler;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return kisiler.size();
    }

    @Override
    public Object getItem(int position) {
        return kisiler.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.kisiler_layout, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
        viewHolder.mTVKisi.setText(kisiler.get(position));
        return v;
    }

    @Override
    public Filter getFilter() {

        return null;
    }
}
class CompleteListViewHolder {
    public TextView mTVKisi,mTVKonusma,mTVGelen,mTVGonderen;
    public CompleteListViewHolder(View base) {
        mTVKisi = (TextView) base.findViewById(R.id.tvKisi);
        mTVKonusma = (TextView) base.findViewById(R.id.tvKonusma);
        mTVGelen = (TextView) base.findViewById(R.id.tvGelen);
        mTVGonderen = (TextView) base.findViewById(R.id.tvGonderen);
    }
}