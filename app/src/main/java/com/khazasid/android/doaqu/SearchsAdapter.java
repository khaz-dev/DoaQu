package com.khazasid.android.doaqu;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.khazasid.android.doaqu.Database.DoaSupportJoin;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class SearchsAdapter extends RecyclerView.Adapter<SearchsAdapter.DoaViewHolder> {

    private List<DoaSupportJoin> searchDoa = new ArrayList<>();

    static String[] search;
    private final Activity mContext;
    private final AppBarLayout mainToolbar;

    SearchsAdapter(Activity context, AppBarLayout mainToolbar){
        this.mContext = context;
        this.mainToolbar = mainToolbar;
    }

    @NonNull
    @Override
    public DoaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.doa_list_search_item, parent, false);

        return new DoaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoaViewHolder holder, int position) {
        holder.bind(searchDoa.get(position));
    }

    @Override
    public int getItemCount() {
        return searchDoa.size();
    }

    void setData(List<DoaSupportJoin> newDataDoa, TextView emptySearchText, TextView searchResult) {
        if(searchResult.getVisibility() == View.INVISIBLE){
            searchResult.setVisibility(View.VISIBLE);
        }

        SearchsAdapter.PostDiffCallback postDiffCallback = new SearchsAdapter.PostDiffCallback(searchDoa, newDataDoa);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);

        searchDoa = newDataDoa;

        diffResult.dispatchUpdatesTo(this);

        if(searchDoa.isEmpty()){
            searchResult.animate().translationY(-searchResult.getHeight());
            emptySearchText.setVisibility(View.VISIBLE);

        }
        else{
            emptySearchText.setVisibility(View.GONE);
            searchResult.setText(mContext.getResources().getString(R.string.search_result,
                    searchDoa.size()));

            if(mainToolbar.getElevation() != 0){
                mainToolbar.setElevation(0);
            }
            searchResult.animate().translationY(0);

        }

    }

    class DoaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView tvTitle, tvTranslate, tvFootnote;
        private final ImageView ivBookmark;

        DoaViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTranslate = itemView.findViewById(R.id.tvTranslate);
            ivBookmark = itemView.findViewById(R.id.ivBookmark);
            tvFootnote = itemView.findViewById(R.id.tvFootnote);
        }

        @Override
        public void onClick(View view)
        {
            // get the position on recyclerview.
            int position = getLayoutPosition();

            Intent detailIntent = new Intent(mContext, DoaDetailActivity.class);
            short rowId = searchDoa.get(position).getRowId();
            detailIntent.putExtra(DetailFragment.ROW_ID_KEY, rowId);

            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP){
                ActivityCompat.startActivity(mContext, detailIntent, null);
            } else {
                Pair<View, String> cardPair,titlePair,footnotePair;

                cardPair = Pair.create(view, mContext.getString(R.string.transition_detail_search));
                titlePair = Pair.create(tvTitle, mContext.getString(R.string.title_trans));
                footnotePair = Pair.create(tvFootnote, mContext.getString(R.string.footnote_trans));

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, cardPair, titlePair, footnotePair);

                ActivityCompat.startActivity(mContext, detailIntent, options.toBundle());
            }
        }

        void bind(final DoaSupportJoin doa) {
            if (doa != null) {
                tvTitle.setText(highlightText(doa.getTitle()));
                tvTranslate.setText(highlightText(doa.getTranslate()));
                tvFootnote.setText(highlightText(doa.getFootnote()));

                if(doa.getBookmark() == 1){
                    ivBookmark.setVisibility(View.VISIBLE);

                } else{
                    ivBookmark.setVisibility(View.GONE);
                }
            }
        }

        private CharSequence highlightText(String originalText) {
            if (search != null && search.length > 0) {
                Spannable highlighted = new SpannableString(originalText);
                for ( String word: search) {
                    String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD)
                            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                            .toLowerCase();
                    int start = normalizedText.indexOf(word);
                    if (start >= 0) {
                        while (start >= 0) {
                            int spanStart = Math.min(start, originalText.length());
                            int spanEnd = Math.min(start + word.length(), originalText.length());
                            highlighted.setSpan(new ForegroundColorSpan(ContextCompat
                                            .getColor(mContext, R.color.colorAccent)), spanStart, spanEnd,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            start = normalizedText.indexOf(word, spanEnd);
                        }
                    }
                }
                return highlighted;
            }
            return originalText;
        }

    }

    static class PostDiffCallback extends DiffUtil.Callback {

        private final List<DoaSupportJoin> oldPosts, newPosts;

        PostDiffCallback(List<DoaSupportJoin> oldPosts, List<DoaSupportJoin> newPosts) {
            this.oldPosts = oldPosts;
            this.newPosts = newPosts;
        }

        @Override
        public int getOldListSize() {
            return oldPosts.size();
        }

        @Override
        public int getNewListSize() {
            return newPosts.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

            return oldPosts.get(oldItemPosition).getRowId() == newPosts.get(newItemPosition).getRowId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).equals(newPosts.get(newItemPosition));
        }

    }
}
