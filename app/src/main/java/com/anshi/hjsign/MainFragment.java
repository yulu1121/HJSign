/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.anshi.hjsign;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anshi.hjsign.entry.RoomEntry;
import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;
    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 15;

    private final Handler mHandler = new Handler();
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private BackgroundManager mBackgroundManager;
    private RecyclerView mLeftRecyclerView;
    private RecyclerView mRightRecyclerView;
    private List<RoomEntry> mTopList;
    private CommonAdapter<RoomEntry> commonAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_main,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mLeftRecyclerView = view.findViewById(R.id.left_recycler);
        mLeftRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),10));
        mRightRecyclerView = view.findViewById(R.id.right_recycler);
        mRightRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),10));
        commonAdapter = new CommonAdapter<RoomEntry>(getContext(),R.layout.room_check,mTopList) {
            @Override
            protected void convert(ViewHolder holder, RoomEntry roomEntry, int position) {
                List<RoomEntry.RoomPersonEntry> roomPersonEntryList = roomEntry.getRoomPersonEntryList();
                Button btn = holder.getView(R.id.title_btn);
                btn.setText(roomEntry.getRoomTitle());
                RecyclerView mNameRecyclerView =  holder.getView(R.id.name_recycler);
                mNameRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
                mNameRecyclerView.setAdapter(new CommonAdapter<RoomEntry.RoomPersonEntry>( mContext,R.layout.child_recycler_item,roomPersonEntryList) {

                    @Override
                    protected void convert(ViewHolder holder, RoomEntry.RoomPersonEntry roomPersonEntry, int position) {
                                   EaseImageView mHeader =  holder.getView(R.id.header_iv);
                                   TextView mNameTv = holder.getView(R.id.name_tv);
                                   TextView mStatusTv = holder.getView(R.id.status_tv);
                                   ImageView mStatusIv = holder.getView(R.id.status_iv);
                                   switch (roomPersonEntry.getSex()){
                                       case "男":
                                           Glide.with(mContext).load(R.drawable.pg_male).into(mHeader);
                                           break;
                                       case "女":
                                           Glide.with(mContext).load(R.drawable.pg_femal).into(mHeader);
                                           break;
                                   }
                                   mNameTv.setText(roomPersonEntry.getName()==null?"无":roomPersonEntry.getName());
                                   mStatusTv.setText(roomPersonEntry.getStatus()==null?"无":roomPersonEntry.getStatus());
                                   switch (roomPersonEntry.getStatus()){
                                       case "在岗":
                                           Glide.with(mContext).load(R.drawable.green_dot).into(mStatusIv);
                                           break;
                                       case "离岗":
                                           Glide.with(mContext).load(R.drawable.red_dot).into(mStatusIv);
                                           break;
                                       case "请假":
                                       case "出差":
                                           Glide.with(mContext).load(R.drawable.blue_dot).into(mStatusIv);
                                           break;

                                   }
                    }
                });


            }
        };

        mLeftRecyclerView.setAdapter(commonAdapter);
        mRightRecyclerView.setAdapter(commonAdapter);
    }

    private void initData(){
            mTopList = new ArrayList<>();
        for (int i = 1; i <11 ; i++) {
            List<RoomEntry.RoomPersonEntry> listOne = new ArrayList<>();
            listOne.add(new RoomEntry.RoomPersonEntry("男","张三","在岗"));
            listOne.add(new RoomEntry.RoomPersonEntry("男","和玉","在岗"));
            listOne.add(new RoomEntry.RoomPersonEntry("女","张晓和","请假"));
            listOne.add(new RoomEntry.RoomPersonEntry("男","行文","在岗"));
            mTopList.add(new RoomEntry("10"+i,listOne));
        }
        for (int i = 1; i <11 ; i++) {
            List<RoomEntry.RoomPersonEntry> listTwo = new ArrayList<>();
            listTwo.add(new RoomEntry.RoomPersonEntry("男","行文","在岗"));
            listTwo.add(new RoomEntry.RoomPersonEntry("男","撒大声","离岗"));
            mTopList.add(new RoomEntry("20"+i,listTwo));
        }
        for (int i = 1; i <11 ; i++) {
            List<RoomEntry.RoomPersonEntry> listTwo = new ArrayList<>();
            listTwo.add(new RoomEntry.RoomPersonEntry("男","行文","在岗"));
            listTwo.add(new RoomEntry.RoomPersonEntry("男","撒大声","离岗"));
            listTwo.add(new RoomEntry.RoomPersonEntry("女","搜索","出差"));
            mTopList.add(new RoomEntry("30"+i,listTwo));
        }
        for (int i = 1; i <11 ; i++) {
            List<RoomEntry.RoomPersonEntry> listTwo = new ArrayList<>();
            listTwo.add(new RoomEntry.RoomPersonEntry("男","行文","在岗"));
            listTwo.add(new RoomEntry.RoomPersonEntry("男","撒大声","离岗"));
            listTwo.add(new RoomEntry.RoomPersonEntry("女","搜索","请假"));
            mTopList.add(new RoomEntry("40"+i,listTwo));
        }
        for (int i = 1; i <11 ; i++) {
            List<RoomEntry.RoomPersonEntry> listTwo = new ArrayList<>();
            listTwo.add(new RoomEntry.RoomPersonEntry("男","行文","在岗"));
            listTwo.add(new RoomEntry.RoomPersonEntry("男","撒大声","离岗"));
            listTwo.add(new RoomEntry.RoomPersonEntry("女","搜索","请假"));
            mTopList.add(new RoomEntry("50"+i,listTwo));
        }
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);
        initData();
       // prepareBackgroundManager();

        //setupUIElements();

        //loadRows();

        //setupEventListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (null != mBackgroundTimer) {
//            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
//            mBackgroundTimer.cancel();
//        }
    }
//
//    private void loadRows() {
//        List<Movie> list = MovieList.setupMovies();
//
//        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
//        CardPresenter cardPresenter = new CardPresenter();
//
//        int i;
//        for (i = 0; i < NUM_ROWS; i++) {
//            if (i != 0) {
//                Collections.shuffle(list);
//            }
//            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
//            for (int j = 0; j < NUM_COLS; j++) {
//                listRowAdapter.add(list.get(j % 5));
//            }
//            HeaderItem header = new HeaderItem(i, MovieList.MOVIE_CATEGORY[i]);
//            rowsAdapter.add(new ListRow(header, listRowAdapter));
//        }
//
//        HeaderItem gridHeader = new HeaderItem(i, "PREFERENCES");
//
//        GridItemPresenter mGridPresenter = new GridItemPresenter();
//        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
//        gridRowAdapter.add(getResources().getString(R.string.grid_view));
//        gridRowAdapter.add(getString(R.string.error_fragment));
//        gridRowAdapter.add(getResources().getString(R.string.personal_settings));
//        rowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));
//
//        setAdapter(rowsAdapter);
//    }
//
//    private void prepareBackgroundManager() {
//
//        mBackgroundManager = BackgroundManager.getInstance(getActivity());
//        mBackgroundManager.attach(getActivity().getWindow());
//
//        mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.drawable.default_background);
//        mMetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
//    }
//
//    private void setupUIElements() {
//        // setBadgeDrawable(getActivity().getResources().getDrawable(
//        // R.drawable.videos_by_google_banner));
//        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
//        // over title
//        setHeadersState(HEADERS_ENABLED);
//        setHeadersTransitionOnBackEnabled(true);
//
//        // set fastLane (or headers) background color
//        setBrandColor(ContextCompat.getColor(getActivity(), R.color.fastlane_background));
//        // set search icon color
//        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));
//    }
//
//    private void setupEventListeners() {
//        setOnSearchClickedListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Implement your own in-app search", Toast.LENGTH_LONG)
//                        .show();
//            }
//        });
//
//        setOnItemViewClickedListener(new ItemViewClickedListener());
//        setOnItemViewSelectedListener(new ItemViewSelectedListener());
//    }
//
//    private void updateBackground(String uri) {
//        int width = mMetrics.widthPixels;
//        int height = mMetrics.heightPixels;
//        Glide.with(getActivity())
//                .load(uri)
//                .centerCrop()
//                .error(mDefaultBackground)
//                .into(new SimpleTarget<GlideDrawable>(width, height) {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource,
//                                                GlideAnimation<? super GlideDrawable>
//                                                        glideAnimation) {
//                        mBackgroundManager.setDrawable(resource);
//                    }
//                });
//        mBackgroundTimer.cancel();
//    }
//
//    private void startBackgroundTimer() {
//        if (null != mBackgroundTimer) {
//            mBackgroundTimer.cancel();
//        }
//        mBackgroundTimer = new Timer();
//        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
//    }
//
//    private final class ItemViewClickedListener implements OnItemViewClickedListener {
//        @Override
//        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
//                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
//
//            if (item instanceof Movie) {
//                Movie movie = (Movie) item;
//                Log.d(TAG, "Item: " + item.toString());
//                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                intent.putExtra(DetailsActivity.MOVIE, movie);
//
//                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        getActivity(),
//                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
//                        DetailsActivity.SHARED_ELEMENT_NAME)
//                        .toBundle();
//                getActivity().startActivity(intent, bundle);
//            } else if (item instanceof String) {
//                if (((String) item).contains(getString(R.string.error_fragment))) {
//                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
//
//    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
//        @Override
//        public void onItemSelected(
//                Presenter.ViewHolder itemViewHolder,
//                Object item,
//                RowPresenter.ViewHolder rowViewHolder,
//                Row row) {
//            if (item instanceof Movie) {
//                mBackgroundUri = ((Movie) item).getBackgroundImageUrl();
//                startBackgroundTimer();
//            }
//        }
//    }
//
//    private class UpdateBackgroundTask extends TimerTask {
//
//        @Override
//        public void run() {
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    updateBackground(mBackgroundUri);
//                }
//            });
//        }
//    }
//
//    private class GridItemPresenter extends Presenter {
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent) {
//            TextView view = new TextView(parent.getContext());
//            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
//            view.setFocusable(true);
//            view.setFocusableInTouchMode(true);
//            view.setBackgroundColor(
//                    ContextCompat.getColor(getActivity(), R.color.default_background));
//            view.setTextColor(Color.WHITE);
//            view.setGravity(Gravity.CENTER);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
//            ((TextView) viewHolder.view).setText((String) item);
//        }
//
//        @Override
//        public void onUnbindViewHolder(ViewHolder viewHolder) {
//        }
//    }

}
