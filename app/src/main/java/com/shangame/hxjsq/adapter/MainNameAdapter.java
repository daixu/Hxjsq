package com.shangame.hxjsq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.shangame.hxjsq.R;

import java.util.ArrayList;
import java.util.List;

public class MainNameAdapter extends BaseAdapter implements Filterable {
    private NameFilter mFilter;
    private List<String> mData;
    private Context mContext;

    public MainNameAdapter(Context context, List<String> data) {
        mFilter = new NameFilter();
        mContext = context;
        mData = data;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public int getCount() {
        return null != mData && mData.size() > 0 ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_main_name, null);
            holder = new ViewHolder();
            holder.textName = convertView.findViewById(R.id.text_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textName.setText(mData.get(position));
        return convertView;
    }

    public void add(String item) {
        mData.add(item);
        List<String> list = new ArrayList<>();
        if (mData.size() > 4) {
            for (int i = mData.size() - 4; i < mData.size(); i++) {
                list.add(mData.get(i));
            }
        } else {
            list.addAll(mData);
        }
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView textName;
    }

    private class NameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (null == mData || mData.size() == 0) {
                return null;
            }
            filterResults.values = mData;
            filterResults.count = mData.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }
}
