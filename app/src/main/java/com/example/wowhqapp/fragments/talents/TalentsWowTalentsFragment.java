package com.example.wowhqapp.fragments.talents;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.wowhqapp.R;
import com.example.wowhqapp.contracts.TalentsContract;
import com.example.wowhqapp.databases.entity.Talent;

import java.util.ArrayList;
import java.util.List;

public class TalentsWowTalentsFragment extends Fragment {
    private TalentsContract.TalentsPresenter mTalentsPresenter;
    private TalentsWowTalentsFragment.TalentsWowTalentsAdapter mWowTalentsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_talents_wow_obj_list, container, false);
        mTalentsPresenter = ((TalentsContract.TalentsView) getActivity()).getTalentsPresenter(); // TODO

        RecyclerView recyclerView = fragmentView.findViewById(R.id.talents_recycler_view_wow_obj_list);
        recyclerView.setLayoutManager(new GridLayoutManager(fragmentView.getContext(), 3));

        mWowTalentsAdapter = new TalentsWowTalentsAdapter();

        recyclerView.setAdapter(mWowTalentsAdapter);

        TalentsWowTalentsAsyncLoader asyncLoader = new TalentsWowTalentsAsyncLoader();
        asyncLoader.execute(mWowTalentsAdapter);

        return fragmentView;
    }

    class TalentsWowTalentViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mNameView;

        private TextView mIdView;


        TalentsWowTalentViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.fragment_talents_wow_talent_elem_img);
            mNameView = itemView.findViewById(R.id.fragment_talents_wow_talent_elem_name);

            mIdView = itemView.findViewById(R.id.fragment_talents_wow_talent_elem_id);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = Integer.parseInt(mIdView.getText().toString());
                    mTalentsPresenter.getSettingRepository().setTalentsWowTalentId(id);
                    mTalentsPresenter.loadStage(false);
                }
            });
        }
    }

    class TalentsWowTalentsAdapter extends RecyclerView.Adapter<TalentsWowTalentViewHolder> {
        private List<Talent> mWowTalentsList;
        // private Bitmap mBitmap;

        TalentsWowTalentsAdapter() {
            // mBitmap = Bitmap.createBitmap(mTalentsPresenter.fillColorsTemp(), 200, 200, Bitmap.Config.ARGB_8888);
            mWowTalentsList = new ArrayList<>();
        }

        @NonNull
        @Override
        public TalentsWowTalentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.fragment_talents_wow_talent_elem, viewGroup, false);

            Log.d("TAG", "onCreateViewHolder for element type " + i);

            return new TalentsWowTalentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TalentsWowTalentViewHolder talentsWowTalentViewHolder, int i) {
            Talent wowTalent = mWowTalentsList.get(i);

            talentsWowTalentViewHolder.mIdView.setText(Integer.toString(wowTalent.getId()));
            talentsWowTalentViewHolder.mNameView.setText(wowTalent.getName());

            // BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), mBitmap);
            // talentsWowTalentViewHolder.mImageView.setBackground(bitmapDrawable);

            Glide.with(getContext())
                    .load(mTalentsPresenter.createImageUrl(wowTalent))
                    .placeholder(R.drawable.question_mark_56)
                    .timeout(3000)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(talentsWowTalentViewHolder.mImageView);

            Log.d("TAG", "binding element at position " + i);
        }

        /*private String createImageUrl(Talent wowTalent) {
            return "http://media.blizzard.com/wow/icons/56/" + talent.getIcon() + ".jpg";
        }*/

        @Override
        public int getItemCount() {
            return mWowTalentsList.size();
        }

        public void setWowTalentsList(List<Talent> wowTalentsList) {
            mWowTalentsList = wowTalentsList;
        }
    }

    class TalentsWowTalentsAsyncLoader extends AsyncTask<TalentsWowTalentsAdapter, Void, TalentsWowTalentsAdapter> {
        @Override
        protected TalentsWowTalentsAdapter doInBackground(TalentsWowTalentsAdapter... talentsWowSpecsAdapters) {
            talentsWowSpecsAdapters[0].setWowTalentsList(mTalentsPresenter.getTalentsRepository().getWowTalents().getTalents());
            return talentsWowSpecsAdapters[0];
        }

        @Override
        protected void onPostExecute(TalentsWowTalentsAdapter talentsWowSpecsAdapter) {
            super.onPostExecute(talentsWowSpecsAdapter);
            talentsWowSpecsAdapter.notifyDataSetChanged();
        }
    }

}
