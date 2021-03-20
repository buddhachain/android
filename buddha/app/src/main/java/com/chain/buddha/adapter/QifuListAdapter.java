package com.chain.buddha.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chain.buddha.R;

import java.util.List;

/**
 * Created by heshuai on 2018/9/25.
 */

public class QifuListAdapter extends BaseAdapter {

    private Activity context;
    private List<String> taskList;
    private LayoutInflater mInflater;

    public QifuListAdapter(Activity context, List<String> taskList) {
        this.context = context;
        this.taskList = taskList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holdertask;
        if (convertView == null || convertView.getTag() == null) {
            holdertask = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_qifu_list, parent, false);
            holdertask.iv_qifu = (ImageView) convertView.findViewById(R.id.iv_qifu);
            holdertask.tv_title = (TextView) convertView.findViewById(R.id.tv_qifu_title);
            holdertask.tv_content = (TextView) convertView.findViewById(R.id.tv_qifu_content);
            holdertask.tv_tag1 = (TextView) convertView.findViewById(R.id.tv_tag1);
            holdertask.tv_tag2 = (TextView) convertView.findViewById(R.id.tv_tag2);
            holdertask.tv_tag3 = (TextView) convertView.findViewById(R.id.tv_tag3);
            holdertask.tv_join_person = (TextView) convertView.findViewById(R.id.tv_join_person);
            holdertask.tv_address = (TextView) convertView.findViewById(R.id.tv_address);

            convertView.setTag(holdertask);
        } else {
            holdertask = (ViewHolder) convertView.getTag();
        }
        String listBean = taskList.get(position);
        if (listBean != null) {
            convertView.setVisibility(View.VISIBLE);
        } else {
            convertView.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv_qifu;
        TextView tv_title;
        TextView tv_content;
        TextView tv_tag1, tv_tag2, tv_tag3;
        TextView tv_join_person;
        TextView tv_address;
    }

}
