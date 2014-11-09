/*
 * This file is part of LoLin1.
 *
 * LoLin1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LoLin1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoLin1. If not, see <http://www.gnu.org/licenses/>.
 *
 * Created by Jorge Antonio Diaz-Benito Soriano.
 */

package org.jorge.lolin1.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.jorge.lolin1.R;
import org.jorge.lolin1.datamodel.FeedArticle;
import org.jorge.lolin1.ui.fragment.FeedListFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private static Float SELECTED_ITEM_ALPHA, UNSELECTED_ITEM_ALPHA;
    private final List<FeedArticle> items = new ArrayList<>();
    private final Context mContext;
    private final FloatingActionButton mFabShareButton, mFabMarkAsReadButton;
    private int mSelectedIndex = FeedListFragment.NO_ITEM_SELECTED, mDefaultImageId;
    private final IOnItemSelectedListener mCallback;

    public NewsAdapter(Context context, FloatingActionButton fabButtonMarkAsRead, FloatingActionButton fabButtonShare, IOnItemSelectedListener onItemSelectedListener, Integer defaultImageId) {
        this.mContext = context;
        this.mFabShareButton = fabButtonShare;
        this.mFabMarkAsReadButton = fabButtonMarkAsRead;
        this.mDefaultImageId = defaultImageId;
        this.mCallback = onItemSelectedListener;

        items.add(new FeedArticle());
        items.add(new FeedArticle());
        items.add(new FeedArticle());
        items.add(new FeedArticle());
        items.add(new FeedArticle());
        items.add(new FeedArticle());
        items.add(new FeedArticle());
        items.add(new FeedArticle());
        items.add(new FeedArticle());
        items.add(new FeedArticle());

        TypedValue outValue = new TypedValue();
        mContext.getResources().getValue(R.dimen.feed_article_selected_alpha, outValue, true);
        SELECTED_ITEM_ALPHA = outValue.getFloat();
        mContext.getResources().getValue(R.dimen.feed_article_unselected_alpha, outValue, true);
        UNSELECTED_ITEM_ALPHA = outValue.getFloat();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_news_article, viewGroup, Boolean.FALSE);
        return new ViewHolder(v);
    }

    private void hideSelectedItemButtons() {
        hideMarkAsReadButton();
        hideShareButton();
    }

    private void hideMarkAsReadButton() {
        mFabMarkAsReadButton.hide();
    }

    private void hideShareButton() {
        mFabShareButton.hide();
    }

    private void showSelectedItemButtons(int itemIndex) {
        if (!items.get(itemIndex).isRead())
            showMarkAsReadButton();
        showShareButton();
    }

    private void showMarkAsReadButton() {
        mFabMarkAsReadButton.setVisibility(View.VISIBLE);
        mFabMarkAsReadButton.show();
    }

    private void showShareButton() {
        mFabShareButton.setVisibility(View.VISIBLE);
        mFabShareButton.show();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setSelectedIndex(i);
                return Boolean.TRUE;
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Show preview of article or launch web intent depending on setting
            }
        });
        FeedArticle item = items.get(i);
        if (!item.isRead()) {
            viewHolder.titleView.setTextSize(mContext.getResources().getInteger(R.integer.feed_article_on_list_title_unread));
            viewHolder.titleView.setTypeface(null, Typeface.BOLD_ITALIC);
        }
        final String title = item.getTitle();
        viewHolder.titleView.setText(title);
        Picasso.with(mContext)
                .load(item.getImageUrl())
                .error(mDefaultImageId)
                .into(viewHolder.imageView);
        viewHolder.imageView.setContentDescription(title);
        final int selectedIndex = getSelectedItemIndex();
        if (selectedIndex == i || selectedIndex == FeedListFragment.NO_ITEM_SELECTED) {
            if (selectedIndex != FeedListFragment.NO_ITEM_SELECTED)
                showSelectedItemButtons(i);
            viewHolder.imageView.setAlpha(SELECTED_ITEM_ALPHA);
        } else {
            viewHolder.imageView.setAlpha(UNSELECTED_ITEM_ALPHA);
        }
    }

    private void setSelectedIndex(int index) {
        mSelectedIndex = index;
        notifyDataSetChanged();
        mCallback.setSelectedIndex(index);
    }

    public void clearSelection() {
        mSelectedIndex = FeedListFragment.NO_ITEM_SELECTED;
        notifyDataSetChanged();
        hideSelectedItemButtons();
        mCallback.clearSelection();
    }

    private int getSelectedItemIndex() {
        return mSelectedIndex;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView titleView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(android.R.id.title);
            imageView = (ImageView) itemView.findViewById(android.R.id.icon);
        }
    }

    public interface IOnItemSelectedListener {

        public void setSelectedIndex(int selectedIndex);

        public void clearSelection();
    }
}