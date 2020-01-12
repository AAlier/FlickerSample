package com.flicksample.views.search;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.flicksample.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Adapter extends BaseAdapter implements Filterable {

    private List<String> original;
    private List<String> data;
    private LayoutInflater inflater;

    public Adapter(Context context) {
        this.data = new ArrayList<String>();
        inflater = LayoutInflater.from(context);
    }

    public void setData(@NotNull Set<String> list) {
        this.original = new ArrayList<String>();
        this.original.addAll(list);
        this.data = new ArrayList<String>();
        this.data.addAll(list);
        notifyDataSetChanged();

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {

                    // Retrieve the autocomplete results.
                    List<String> searchData = new ArrayList<>();

                    for (String string : original) {
                        if (string.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            searchData.add(string);
                        }
                    }

                    // Assign the data to the FilterResults
                    filterResults.values = searchData;
                    filterResults.count = searchData.size();
                } else {
                    filterResults.values = original;
                    filterResults.count = original.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    data = (List<String>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SuggestionsViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_suggest, parent, false);
            viewHolder = new SuggestionsViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SuggestionsViewHolder) convertView.getTag();
        }

        String currentListData = (String) getItem(position);
        viewHolder.textView.setText(currentListData);
        return convertView;
    }

    private class SuggestionsViewHolder {

        TextView textView;
        ImageView imageView;

        public SuggestionsViewHolder(View convertView) {
            textView = (TextView) convertView.findViewById(R.id.suggestion_text);
            imageView = (ImageView) convertView.findViewById(R.id.suggestion_icon);
        }
    }
}