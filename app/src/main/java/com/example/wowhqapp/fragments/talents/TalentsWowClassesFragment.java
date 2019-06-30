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
import com.example.wowhqapp.databases.entity.WowClass;

import java.util.ArrayList;
import java.util.List;

public class TalentsWowClassesFragment extends Fragment {
    private TalentsContract.TalentsView mTalentsView;
    private TalentsContract.TalentsPresenter mTalentsPresenter;
    private TalentsWowClassesAdapter mWowClassesAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_talents_wow_obj_list, container, false);

        mTalentsView = (TalentsContract.TalentsView) getActivity();
        mTalentsPresenter = mTalentsView.getTalentsPresenter(); // TODO

        RecyclerView recyclerView = fragmentView.findViewById(R.id.talents_recycler_view_wow_obj_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmentView.getContext()));

        mWowClassesAdapter = new TalentsWowClassesAdapter();

        recyclerView.setAdapter(mWowClassesAdapter);

        TalentsWowClassesAsyncLoader asyncLoader = new TalentsWowClassesAsyncLoader();
        asyncLoader.execute(mWowClassesAdapter);

        return fragmentView;
    }

    class TalentsWowClassesViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mNameView;
        private TextView mIdView;

        TalentsWowClassesViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.fragment_talents_wow_class_elem_img);
            mNameView = itemView.findViewById(R.id.fragment_talents_wow_class_elem_name);
            mIdView = itemView.findViewById(R.id.fragment_talents_wow_class_elem_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = Integer.parseInt(mIdView.getText().toString());
                    mTalentsPresenter.getSettingRepository().setTalentsWowClassId(id);

                    String title = mTalentsView.getTalentsTitle() +" | " + mNameView.getText();
                    mTalentsView.setTalentsTitle(title);

                    mTalentsPresenter.loadStage(false);
                }
            });
        }
    }

    class TalentsWowClassesAdapter extends RecyclerView.Adapter<TalentsWowClassesViewHolder> {
        private List<WowClass> mWowClasses;
        // private Bitmap mBitmap;

        TalentsWowClassesAdapter() {
            // mBitmap = Bitmap.createBitmap(mTalentsPresenter.fillColorsTemp(), 200, 200, Bitmap.Config.ARGB_8888);
            mWowClasses = new ArrayList<>();
        }

        @NonNull
        @Override
        public TalentsWowClassesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.fragment_talents_wow_class_elem, viewGroup, false);

            Log.d("TAG", "onCreateViewHolder for element type " + i);

            return new TalentsWowClassesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TalentsWowClassesViewHolder talentsWowClassesViewHolder, int i) {
            WowClass wowClass = mWowClasses.get(i);

            talentsWowClassesViewHolder.mIdView.setText(Integer.toString(wowClass.getId()));
            talentsWowClassesViewHolder.mNameView.setText(wowClass.getName());

            // BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), mBitmap);
            // talentsWowClassesViewHolder.mImageView.setBackground(bitmapDrawable);

            Glide.with(getContext())
                    .load(mTalentsPresenter.createImageUrl(wowClass))
                    .placeholder(R.drawable.question_mark_56)
                    .timeout(3000)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(talentsWowClassesViewHolder.mImageView);

            Log.d("TAG", "binding element at position " + i);
        }

        @Override
        public int getItemCount() {
            return mWowClasses.size();
        }

        public void setWowClasses(List<WowClass> wowClasses) {
            mWowClasses = wowClasses;
        }
    }


    class TalentsWowClassesAsyncLoader extends AsyncTask<TalentsWowClassesAdapter, Void, TalentsWowClassesAdapter> {
        @Override
        protected TalentsWowClassesAdapter doInBackground(TalentsWowClassesAdapter... talentsWowClassesAdapters) {
            talentsWowClassesAdapters[0].setWowClasses(mTalentsPresenter.getTalentsRepository().getWowClasses());
            return talentsWowClassesAdapters[0];
        }

        @Override
        protected void onPostExecute(TalentsWowClassesAdapter talentsWowClassesAdapter) {
            super.onPostExecute(talentsWowClassesAdapter);
            talentsWowClassesAdapter.notifyDataSetChanged();
        }
    }
}
