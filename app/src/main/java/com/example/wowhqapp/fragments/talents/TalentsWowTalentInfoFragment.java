package com.example.wowhqapp.fragments.talents;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class TalentsWowTalentInfoFragment extends Fragment {
    // private final String CURRENT_TALENT_ID = "CURRENT_TALENT_ID";
    private TalentsContract.TalentsPresenter mTalentsPresenter;
    private int mCurrentTalentId;
    private View mFragmentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_talents_wow_talent_info, container, false);
        mTalentsPresenter = ((TalentsContract.TalentsView) getActivity()).getTalentsPresenter();

        mCurrentTalentId = mTalentsPresenter.getSettingRepository().getTalentsWowTalentId();
        /*if (savedInstanceState != null) {
            mCurrentTalentId = savedInstanceState.getInt(CURRENT_TALENT_ID);
        }*/

        mFragmentView.findViewById(R.id.fragment_talents_wow_talent_info_button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTalentsPresenter.getSettingRepository().setTalentsWowTalentId(-1);
                mTalentsPresenter.loadStage(false);
            }
        });

        TalentsWowTalentsInfoAsyncLoader asyncLoader = new TalentsWowTalentsInfoAsyncLoader();
        asyncLoader.execute(mTalentsPresenter);

        return mFragmentView;
    }

   /* @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_TALENT_ID, mCurrentTalentId);
    }*/

    private class TalentsWowTalentsInfoAsyncLoader
            extends AsyncTask<TalentsContract.TalentsPresenter, Void, Talent> {
        @Override
        protected Talent doInBackground(TalentsContract.TalentsPresenter... talentsPresenters) {
            return talentsPresenters[0].getTalentsRepository().getWowTalent(mCurrentTalentId);
        }

        @Override
        protected void onPostExecute(Talent talent) {
            super.onPostExecute(talent);

            TextView nameView = mFragmentView.findViewById(R.id.fragment_talents_wow_talent_info_name);
            TextView descriptionView = mFragmentView.findViewById(R.id.fragment_talents_wow_talent_info_description);
            ImageView imageView = mFragmentView.findViewById(R.id.fragment_talents_wow_talent_info_img);

            nameView.setText(talent.getName());
            descriptionView.setText(talent.getDescription());
            Glide.with(getContext())
                    .load(mTalentsPresenter.createImageUrl(talent))
                    .placeholder(R.drawable.question_mark_56)
                    .timeout(3000)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }
}
