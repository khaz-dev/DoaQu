package com.khazasid.android.doaqu;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.khazasid.android.doaqu.Database.DoaSupportJoin;

import java.util.ArrayList;
import java.util.List;

public class
DoasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<DoaSupportJoin> data = new ArrayList<>();

    private final Activity mContext;

    private int lastPosition = -1;

    DoasAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.doa_list_item, parent, false);

        return new DoaViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        DoaViewHolder doaViewHolder = (DoaViewHolder) holder;
        doaViewHolder.bind(data.get(holder.getLayoutPosition()));

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (holder.getLayoutPosition() > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = holder.getLayoutPosition();

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

    void setData(List<DoaSupportJoin> newData, RecyclerView recyclerView, TextView emptyData) {

        if(newData.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyData.setVisibility(View.VISIBLE);
        } else{
            PostDiffCallback postDiffCallback = new PostDiffCallback(data, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);

            data = newData;

            diffResult.dispatchUpdatesTo(this);

            recyclerView.setVisibility(View.VISIBLE);
            emptyData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public class DoaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView tvNumber, tvTitle, tvFootnote;


        DoaViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvFootnote = itemView.findViewById(R.id.tvFootnote);
        }

        @Override
        public void onClick(View view)
        {

            Intent detailIntent = new Intent(mContext, DoaDetailActivity.class);
            short rowId = data.get(getLayoutPosition()).getRowId();
            detailIntent.putExtra(DetailFragment.ROW_ID_KEY, rowId);

            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP){
                ActivityCompat.startActivity(mContext, detailIntent, null);
            } else {
                Pair<View, String> cardPair, numberContainerPair, titlePair, footnotePair;

                cardPair = Pair.create(view, mContext.getString(R.string.transition_doa_detail));
                numberContainerPair = Pair.create(view.findViewById(R.id.tvNumber), mContext.getString(R.string.number_container_trans));
                titlePair = Pair.create(tvTitle, mContext.getString(R.string.title_trans));
                footnotePair = Pair.create(tvFootnote, mContext.getString(R.string.footnote_trans));

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, cardPair, numberContainerPair, titlePair, footnotePair);

                ActivityCompat.startActivity(mContext, detailIntent, options.toBundle());
            }

        }

        void bind(final DoaSupportJoin doa) {
            if (doa != null) {

                tvNumber.setText(String.valueOf(doa.getRowId()));
                tvTitle.setText(doa.getTitle());
                tvFootnote.setText(doa.getFootnote());

            }
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
