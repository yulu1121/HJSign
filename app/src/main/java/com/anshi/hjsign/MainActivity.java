/*
 * Copyright (C) 2014 The Android Open Source Project
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

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anshi.hjsign.entry.RoomEntry;
import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*
 * MainActivity class that loads {@link MainFragment}.
 */
public class MainActivity extends Activity {
    private RecyclerView mLeftRecyclerView;
    private RecyclerView mRightRecyclerView;
    private List<RoomEntry> mTopList  = new ArrayList<>();
    private CommonAdapter<RoomEntry> commonAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_main);
        initView();
        initData();
    }


    private void initView() {
        TextView mCalendarDay = findViewById(R.id.calendar_day);
        TextView mCalendarDate = findViewById(R.id.calendar_date);
        mCalendarDay.setText(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        mCalendarDate.setText(getDate());
        mLeftRecyclerView = findViewById(R.id.left_recycler);
        mLeftRecyclerView.setLayoutManager(new GridLayoutManager(this,10));
        mRightRecyclerView = findViewById(R.id.right_recycler);
        mRightRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        commonAdapter = new CommonAdapter<RoomEntry>(this,R.layout.room_check,mTopList) {
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
                                mStatusIv.setImageResource(R.drawable.green_dot);
                                break;
                            case "离岗":
                                mStatusIv.setImageResource(R.drawable.red_dot);
                                break;
                            case "请假":
                            case "出差":
                                mStatusIv.setImageResource(R.drawable.blue_dot);
                                break;

                        }
                    }
                });
            }
        };

        mLeftRecyclerView.setAdapter(commonAdapter);
        mRightRecyclerView.setAdapter(commonAdapter);
    }

    private String getDate(){
        String dateFormat = "yyyy-MM";
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return format.format(new Date());
    }

    private void initData(){
        for (int i = 1; i <11 ; i++) {
            List<RoomEntry.RoomPersonEntry> listOne = new ArrayList<>();
            listOne.add(new RoomEntry.RoomPersonEntry("男","张三","在岗"));
            listOne.add(new RoomEntry.RoomPersonEntry("男","和玉","离岗"));
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
//        for (int i = 1; i <11 ; i++) {
//            List<RoomEntry.RoomPersonEntry> listTwo = new ArrayList<>();
//            listTwo.add(new RoomEntry.RoomPersonEntry("男","行文","在岗"));
//            listTwo.add(new RoomEntry.RoomPersonEntry("男","撒大声","离岗"));
//            listTwo.add(new RoomEntry.RoomPersonEntry("女","搜索","请假"));
//            mTopList.add(new RoomEntry("40"+i,listTwo));
//        }
//        for (int i = 1; i <11 ; i++) {
//            List<RoomEntry.RoomPersonEntry> listTwo = new ArrayList<>();
//            listTwo.add(new RoomEntry.RoomPersonEntry("男","行文","在岗"));
//            listTwo.add(new RoomEntry.RoomPersonEntry("男","撒大声","离岗"));
//            listTwo.add(new RoomEntry.RoomPersonEntry("女","搜索","请假"));
//            mTopList.add(new RoomEntry("50"+i,listTwo));
//        }
        commonAdapter.notifyDataSetChanged();
    }

}
