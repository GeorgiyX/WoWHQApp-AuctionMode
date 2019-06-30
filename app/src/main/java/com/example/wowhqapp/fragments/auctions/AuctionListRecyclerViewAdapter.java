package com.example.wowhqapp.fragments.auctions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.wowhqapp.R;
import com.example.wowhqapp.databases.entity.Lot;
import com.example.wowhqapp.fragments.auctions.AuctionListFragment.OnListFragmentInteractionListener;
import com.example.wowhqapp.fragments.auctions.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AuctionListRecyclerViewAdapter extends RecyclerView.Adapter<AuctionListRecyclerViewAdapter.ViewHolder> {

    private final List<Lot> mLots;
    private final OnListFragmentInteractionListener mListener;
    private final Context mContext; //Пока сохраняем context, но дальшоое можно поменять

    public AuctionListRecyclerViewAdapter(List<Lot> lots, Context context) {
        mLots = lots;
        mListener = (OnListFragmentInteractionListener)context;
        mContext = (Context) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mLot = mLots.get(position);
        holder.mOwnerTextView.setText(mLots.get(position).getOwner());
        holder.mSlugTextView.setText(mLots.get(position).getSlug() );
        holder.mTimeTextView.setText(mLots.get(position).getTime() );
        holder.mQuantityTextView.setText("x"+String.valueOf(mLots.get(position).getQuantity()));
        holder.mBidTextView.setText(String.valueOf(mLots.get(position).getBid()));
        holder.mBuyoutTextView.setText(String.valueOf(mLots.get(position).getBuyout()));
        holder.mNameTextView.setText(mLots.get(position).getItem() );


        Glide.with(mContext)
                .load(mListener.onGlideGetUrl(holder.mLot.getIcon()))
                .placeholder(R.drawable.question_mark_56)
                .timeout(3000)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mIconImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mLot);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mIconImageView;
        public final ImageButton mAddImageButton;
        public final ImageButton mShowMoreImageButton;
        public final TextView mOwnerTextView;
        public final TextView mSlugTextView;
        public final TextView mTimeTextView;
        public final TextView mQuantityTextView;
        public final TextView mBidTextView;
        public final TextView mBuyoutTextView;
        public final TextView mNameTextView;



        public Lot mLot;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mOwnerTextView = (TextView) view.findViewById(R.id.lot_owner);
            mSlugTextView = (TextView) view.findViewById(R.id.lot_server);
            mTimeTextView = (TextView) view.findViewById(R.id.lot_time);
            mQuantityTextView = (TextView) view.findViewById(R.id.lot_quantity);
            mBidTextView = (TextView) view.findViewById(R.id.lot_bid);
            mBuyoutTextView = (TextView) view.findViewById(R.id.lot_buyout);
            mNameTextView = (TextView) view.findViewById(R.id.lot_name);
            mIconImageView = (ImageView) view.findViewById(R.id.lot_icon);
            mAddImageButton = (ImageButton) view.findViewById(R.id.lot_add_btn);
            mShowMoreImageButton = (ImageButton) view.findViewById(R.id.lot_show_more_btn);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameTextView.getText() + "'";
        }
    }
}
