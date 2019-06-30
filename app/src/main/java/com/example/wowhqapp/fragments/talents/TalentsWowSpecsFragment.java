package com.example.wowhqapp.fragments.talents;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.example.wowhqapp.databases.entity.WowSpec;

import java.util.ArrayList;
import java.util.List;

public class TalentsWowSpecsFragment extends Fragment {
    private TalentsContract.TalentsView mTalentsView;
    private TalentsContract.TalentsPresenter mTalentsPresenter;
    private TalentsWowSpecsFragment.TalentsWowSpecsAdapter mWowSpecsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_talents_wow_obj_list, container, false);
        mTalentsView = (TalentsContract.TalentsView) getActivity();
        mTalentsPresenter = mTalentsView.getTalentsPresenter(); // TODO

        RecyclerView recyclerView = fragmentView.findViewById(R.id.talents_recycler_view_wow_obj_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmentView.getContext()));

        mWowSpecsAdapter = new TalentsWowSpecsAdapter();

        recyclerView.setAdapter(mWowSpecsAdapter);

        TalentsWowSpecsAsyncLoader asyncLoader = new TalentsWowSpecsAsyncLoader();
        asyncLoader.execute(mWowSpecsAdapter);

        return fragmentView;
    }


    class TalentsWowSpecsViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mNameView;
        private TextView mDescriptionView;

        private TextView mIdView;
        private TextView mOrderView;


        TalentsWowSpecsViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.fragment_talents_wow_spec_elem_img);
            mNameView = itemView.findViewById(R.id.fragment_talents_wow_spec_elem_name);
            mDescriptionView = itemView.findViewById(R.id.fragment_talents_wow_spec_elem_description);

            mIdView = itemView.findViewById(R.id.fragment_talents_wow_spec_elem_id);
            mOrderView = itemView.findViewById(R.id.fragment_talents_wow_spec_elem_order);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TextView idView = v.findViewById(R.id.fragment_talents_wow_spec_elem_id);
                    // TextView orderViewView = v.findViewById(R.id.fragment_talents_wow_spec_elem_order);

                    int id = Integer.parseInt(mIdView.getText().toString());
                    int order = Integer.parseInt(mOrderView.getText().toString());

                    mTalentsPresenter.getSettingRepository().setTalentsWowSpecId(id);
                    mTalentsPresenter.getSettingRepository().setTalentsWowSpecOrder(order);

                    String title = mTalentsView.getTalentsTitle() +" | " + mNameView.getText();
                    mTalentsView.setTalentsTitle(title);

                    mTalentsPresenter.loadStage(false);
                }
            });
        }
    }

    class TalentsWowSpecsAdapter extends RecyclerView.Adapter<TalentsWowSpecsViewHolder> {
        private List<WowSpec> mWowSpecs;
        // private Bitmap mBitmap;

        TalentsWowSpecsAdapter() {
            // mBitmap = Bitmap.createBitmap(mTalentsPresenter.fillColorsTemp(), 200, 200, Bitmap.Config.ARGB_8888);
            mWowSpecs = new ArrayList<>();
        }

        @NonNull
        @Override
        public TalentsWowSpecsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.fragment_talents_wow_spec_elem, viewGroup, false);

            Log.d("TAG", "onCreateViewHolder for element type " + i);

            return new TalentsWowSpecsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TalentsWowSpecsViewHolder talentsWowSpecsViewHolder, int i) {
            WowSpec wowSpec = mWowSpecs.get(i);

            talentsWowSpecsViewHolder.mIdView.setText(Integer.toString(wowSpec.getId()));
            talentsWowSpecsViewHolder.mOrderView.setText(Integer.toString(wowSpec.getSpecOrder()));

            talentsWowSpecsViewHolder.mNameView.setText(wowSpec.getName());
            talentsWowSpecsViewHolder.mDescriptionView.setText(wowSpec.getDescription());

            // BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), mBitmap);
            // talentsWowSpecsViewHolder.mImageView.setBackground(bitmapDrawable);

            Glide.with(getContext())
                    .load(mTalentsPresenter.createImageUrl(wowSpec))
                    .placeholder(R.drawable.question_mark_56)
                    .timeout(3000)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(talentsWowSpecsViewHolder.mImageView);


            Log.d("TAG", "binding element at position " + i);
        }

        @Override
        public int getItemCount() {
            return mWowSpecs.size();
        }

        public void setWowSpecs(List<WowSpec> wowSpecs) {
            mWowSpecs = wowSpecs;
        }
    }

    class TalentsWowSpecsAsyncLoader extends AsyncTask<TalentsWowSpecsAdapter, Void, TalentsWowSpecsAdapter> {
        @Override
        protected TalentsWowSpecsAdapter doInBackground(TalentsWowSpecsAdapter... talentsWowSpecsAdapters) {
            int classId = mTalentsPresenter.getSettingRepository().getTalentsWowClassId();
            talentsWowSpecsAdapters[0].setWowSpecs(mTalentsPresenter.getTalentsRepository().getWowSpecs(classId));
            return talentsWowSpecsAdapters[0];
        }

        @Override
        protected void onPostExecute(TalentsWowSpecsAdapter talentsWowSpecsAdapter) {
            super.onPostExecute(talentsWowSpecsAdapter);
            talentsWowSpecsAdapter.notifyDataSetChanged();
        }
    }
}
