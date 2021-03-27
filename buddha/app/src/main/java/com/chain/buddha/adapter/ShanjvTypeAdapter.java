package com.chain.buddha.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by heshuai on 2018/9/25.
 */

public class ShanjvTypeAdapter extends BaseAdapter {

    private List<String> mList;
    private Context mContext;

    public ShanjvTypeAdapter(Context pContext, List<String> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 下面是重要代码
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(android.R.layout.simple_spinner_item, null);
        if (convertView != null) {
            try {
                String[] list = mList.get(position).split(",");

                TextView _TextView1 = (TextView) convertView.findViewById(android.R.id.text1);
                _TextView1.setText(list[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

}
