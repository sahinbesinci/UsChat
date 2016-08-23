package com.example.uschat.uschat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.uschat.uschat.R;

import java.util.ArrayList;

/**
 * Created by sahin on 26.04.2016.
 */
public class ChatAdapter extends BaseAdapter {
    ArrayList<String[]> yazi;
    Context context;
    private LayoutInflater mLayoutInflater = null;
    CompleteListViewHolder viewHolder;
    public ChatAdapter(Context context, ArrayList<String[]> yazi)
    {
        this.context = context;
        this.yazi = yazi;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return yazi.size();
    }

    @Override
    public Object getItem(int position) {
        return yazi.get(position);
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
            if (yazi.get(position)[1].toString() == "0")
                v = li.inflate(R.layout.chat_gelen_layout, null);
            else
                v = li.inflate(R.layout.chat_gonderen_layout, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
        if (yazi.get(position)[1].toString() == "0")
            viewHolder.mTVGelen.setText(yazi.get(position)[0].toString());
        else
            viewHolder.mTVGonderen.setText(yazi.get(position)[0].toString());
        return v;
    }
}
