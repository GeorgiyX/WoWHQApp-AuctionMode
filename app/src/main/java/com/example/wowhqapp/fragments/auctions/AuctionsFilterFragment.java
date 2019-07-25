package com.example.wowhqapp.fragments.auctions;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wowhqapp.R;
import com.example.wowhqapp.WowhqApplication;
import com.example.wowhqapp.contracts.MainContract;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AuctionsFilterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AuctionsFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuctionsFilterFragment extends Fragment implements MainContract.AuctionsFilterFragmentView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ExpandableListView mExpandableListView;
    private Context mContext;
    private ConstraintLayout mFilterHeader;
    private EditText mLevelStartEditText;
    private EditText mLevelEndEditText;
    private EditText mPriceStartEditText;
    private EditText mPriceEndEditText;
    private Spinner mOrderBySpinner;
    private Button mApplyButton;
    private Button mResetButton;
    private View.OnClickListener mOnButtonClickListener;


    public AuctionsFilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuctionsFilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AuctionsFilterFragment newInstance(String param1, String param2) {
        AuctionsFilterFragment fragment = new AuctionsFilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mApplyButton = null;
        mResetButton = null;
        mOnButtonClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.filter_btn_apply:
                        sendInputViewData();
                        mListener.onApplyButtonClick();
                        break;
                    case R.id.filter_btn_reset:
                        mListener.onResetButtonClick();
                        break;
                }
            }
        };
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_auctions_filter, container, false);
        mExpandableListView = (ExpandableListView) root_view.findViewById(R.id.fragment_filter_auctions);
        mApplyButton = (Button) root_view.findViewById(R.id.filter_btn_apply);
        mResetButton = (Button) root_view.findViewById(R.id.filter_btn_reset);
        mListener.inInitFilterFragment();
        mResetButton.setOnClickListener(mOnButtonClickListener);
        mApplyButton.setOnClickListener(mOnButtonClickListener);
        return root_view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentAuctionFilterInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(WowhqApplication.LOG_TAG, "[FilterFragment] onAttach ");
        mContext = context;
        mFilterHeader = null;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mContext = null;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.v(WowhqApplication.LOG_TAG, "[FilterFragment] onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.v(WowhqApplication.LOG_TAG, "[FilterFragment] onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setFilterAdapterToExpandableListViewAndSetHeader(ArrayList<Map<String, String>> groupArrayList, ArrayList<ArrayList<Map<String, String>>> childrenArrayList, String statLevel, String endLevel, String starPrice, String endPrice, Integer orderPosition) {
        if (mExpandableListView.getHeaderViewsCount() > 0){
            mExpandableListView.removeHeaderView(mFilterHeader);
        }
        mFilterHeader = (ConstraintLayout) getLayoutInflater().inflate(R.layout.auctions_filter_expandable_header, mExpandableListView, false);
        mFilterHeader.setLayoutParams(new AbsListView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mExpandableListView.addHeaderView(mFilterHeader);

        mLevelStartEditText = (EditText) mFilterHeader.findViewById(R.id.filter_header_item_level_start);
        mLevelEndEditText = (EditText) mFilterHeader.findViewById(R.id.filter_header_item_level_end);
        mPriceStartEditText = (EditText) mFilterHeader.findViewById(R.id.filter_header_item_price_start);
        mPriceEndEditText = (EditText) mFilterHeader.findViewById(R.id.filter_header_item_price_end);
        mOrderBySpinner = (Spinner) mFilterHeader.findViewById(R.id.filter_header_order_by_spinner);

        //Это для сохранения данных после закрытия фрагмента..
        if (statLevel != null) {
            mLevelStartEditText.setText(statLevel);
        }if (endLevel != null) {
            mLevelEndEditText.setText(endLevel);
        }if (starPrice != null) {
            mPriceStartEditText.setText(starPrice);
        }if (endPrice != null) {
            mPriceEndEditText.setText(endPrice);
        }if (orderPosition != null){
            mOrderBySpinner.setSelection(orderPosition);
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendInputViewData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mLevelStartEditText.addTextChangedListener(textWatcher);
        mLevelEndEditText.addTextChangedListener(textWatcher);
        mPriceStartEditText.addTextChangedListener(textWatcher);
        mPriceEndEditText.addTextChangedListener(textWatcher);

        mOrderBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sendInputViewData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Adapter block
        String[] groupFrom = new String[] {"class_name", "class_id"};
        int[] groupTo = new int[] {android.R.id.text1};
        String[] childFrom = new String[] {"subclass_name", "is_checked", "subclass_id" };
        int[] childTo =new int[] {R.id.auctions_filter_expandable_child_text_view, R.id.auctions_filter_expandable_child_check_box};
        mExpandableListView.setAdapter(new CustomExpandableListAdapter(mContext, groupArrayList, android.R.layout.simple_expandable_list_item_1, groupFrom, groupTo,
                childrenArrayList, R.layout.auctions_filter_expandable_child, childFrom, childTo));
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.v(WowhqApplication.LOG_TAG, "[FilterFragment] onFilterChildCategoryClick ");
                CheckBox checkBox = (CheckBox) v.findViewById(R.id.auctions_filter_expandable_child_check_box);
                checkBox.setChecked(!checkBox.isChecked());
                mListener.onFilterChildCategoryClick(groupPosition, childPosition, checkBox.isChecked());
                return false;
            }
        });
    }

    @Override
    public void setFilterResetButtonVisibility(boolean isVisible) {
        if (mResetButton != null){
            if (isVisible){
                Log.v(WowhqApplication.LOG_TAG, "VISIBLE RESET");
                mResetButton.setVisibility(View.VISIBLE);
            }else {
                Log.v(WowhqApplication.LOG_TAG, "GONE RESET");
                mResetButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showFilterInputErrorToast() {
        Toast.makeText(mContext, getResources().getString(R.string.filter_input_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideFilterKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow( getView().getRootView().getWindowToken(), 0);
    }

    private void  sendInputViewData(){
        mListener.onInputViewChange(mLevelStartEditText.getText().toString(), mLevelEndEditText.getText().toString(),
                mPriceStartEditText.getText().toString(),mPriceEndEditText.getText().toString(), mOrderBySpinner.getSelectedItemPosition());
    }

    public void setContextAndListener(Context context){
        mListener = (OnFragmentInteractionListener) context;
        mContext = context;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentAuctionFilterInteraction(Uri uri);
        void inInitFilterFragment();
        void onFilterChildCategoryClick(int groupPosition, int childPosition, boolean isChecked);
        void onApplyButtonClick();
        void onInputViewChange(String statLevel, String endLevel, String starPrice, String endPrice, Integer orderPosition);
        void onResetButtonClick();
    }
}
