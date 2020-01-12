package com.flicksample.ui.main.adapter;

import com.flicksample.model.Photo;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PhotoDiffUtil {

    public static void notifyChanges(RecyclerView.Adapter<?> adapter,
                                     final List<Photo> oldList, final List<Photo> newList) {

        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList == null ? 0 : oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList == null ? 0 : newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                Photo oldItem = oldList.get(oldItemPosition);
                Photo newItem = newList.get(newItemPosition);
                return oldItem != null && newItem != null &&
                        oldItem.getId() != null &&
                        oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Photo oldItem = oldList.get(oldItemPosition);
                Photo newItem = newList.get(newItemPosition);
                return oldItem != null && newItem != null &&
                        oldItem.getId() != null &&
                        oldItem.getId().equals(newItem.getId()) &&
                        oldItem.getSmallUrl() != null &&
                        oldItem.getSmallUrl().equals(newItem.getSmallUrl()) &&
                        oldItem.getThumbnailUrl() != null &&
                        oldItem.getThumbnailUrl().equals(newItem.getThumbnailUrl());
            }
        }).dispatchUpdatesTo(adapter);
    }
}
