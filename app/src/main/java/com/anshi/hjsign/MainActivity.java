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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private List<RoomEntry> mSecondTopList = new ArrayList<>();
    private CommonAdapter<RoomEntry> commonAdapter;
    private CommonAdapter<RoomEntry> mSecondAdapter;
    private TextView mCalendarDay;
    private TextView mCalendarDate;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_main);
        initView();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        registerReceiver(usBroadcastReceiver, filter);
        initData();
    }

    /**
     * 时间切换的广播接收者
     */
    private BroadcastReceiver usBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (null!=action){
                switch (action){
                    case Intent.ACTION_DATE_CHANGED:
                        mCalendarDay.setText(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
                        mCalendarDate.setText(getDate());
                        break;

                }
            }
        }
    };

    /**
     * 记得销毁广播,防止内存泄漏
     */
    @Override
    protected void onDestroy() {
        unregisterReceiver(usBroadcastReceiver);
        super.onDestroy();

    }
    private void initView() {
        mCalendarDay = findViewById(R.id.calendar_day);
        mCalendarDate = findViewById(R.id.calendar_date);
        mCalendarDay.setText(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        mCalendarDate.setText(getDate());
        mLeftRecyclerView = findViewById(R.id.left_recycler);
        mLeftRecyclerView.setLayoutManager(new GridLayoutManager(this,6));
        mRightRecyclerView = findViewById(R.id.right_recycler);
        mRightRecyclerView.setLayoutManager(new GridLayoutManager(this,10));
        commonAdapter = new CommonAdapter<RoomEntry>(this,R.layout.room_check,mTopList) {
            @Override
            protected void convert(ViewHolder holder, RoomEntry roomEntry, int position) {
                final List<RoomEntry.RoomPersonEntry> roomPersonEntryList = roomEntry.getRoomPersonEntryList();
                Button btn = holder.getView(R.id.title_btn);
                btn.setText(roomEntry.getRoomTitle());
                RecyclerView mNameRecyclerView =  holder.getView(R.id.name_recycler);
                if (roomPersonEntryList.size()>5&&roomPersonEntryList.size()<10){
                  mNameRecyclerView.setLayoutManager(new GridLayoutManager(mContext,2));
                    holder.getConvertView().setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,dpToPx(200,getResources())));
                }else if (roomPersonEntryList.size()>=10){
                    mNameRecyclerView.setLayoutManager(new GridLayoutManager(mContext,2));
                    holder.getConvertView().setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,dpToPx(280,getResources())));
                }else {
                    mNameRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
                    holder.getConvertView().setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,dpToPx(180,getResources())));
                }

                mNameRecyclerView.setAdapter(new CommonAdapter<RoomEntry.RoomPersonEntry>( mContext,R.layout.child_recycler_item,roomPersonEntryList) {

                    @Override
                    protected void convert(ViewHolder holder, RoomEntry.RoomPersonEntry roomPersonEntry, int position) {
                        EaseImageView mHeader =  holder.getView(R.id.header_iv);

                        TextView mNameTv = holder.getView(R.id.name_tv);
                        TextView mStatusTv = holder.getView(R.id.status_tv);
                        ImageView mStatusIv = holder.getView(R.id.status_iv);
                        if (roomPersonEntryList.size()>5){
                            mNameTv.setTextSize(8);
                            mStatusTv.setTextSize(8);
                        }else{
                            mNameTv.setTextSize(16);
                            mStatusTv.setTextSize(16);
                        }
                        switch (roomPersonEntry.getSex()){
                            case "男":
                                Glide.with(mContext).load(R.drawable.pg_male).into(mHeader);
                                break;
                            case "女":
                                Glide.with(mContext).load(R.drawable.pg_femal).into(mHeader);
                                break;
                             default:
                                 holder.getConvertView().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                 mHeader.setVisibility(View.GONE);
                                 mNameTv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                 mNameTv.setGravity(Gravity.CENTER);
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
                            default:
                                holder.getConvertView().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                mStatusIv.setVisibility(View.GONE);
                                mNameTv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                mNameTv.setGravity(Gravity.CENTER);
                                break;
                        }
                    }
                });
            }
        };
        mSecondAdapter = new CommonAdapter<RoomEntry>(this,R.layout.room_check,mSecondTopList) {
            @Override
            protected void convert(ViewHolder holder, RoomEntry roomEntry, int position) {
                final List<RoomEntry.RoomPersonEntry> roomPersonEntryList = roomEntry.getRoomPersonEntryList();
                Button btn = holder.getView(R.id.title_btn);
                btn.setText(roomEntry.getRoomTitle());
                RecyclerView mNameRecyclerView =  holder.getView(R.id.name_recycler);
                if (roomPersonEntryList.size()>5){
                    mNameRecyclerView.setLayoutManager(new GridLayoutManager(mContext,2));
                    holder.getConvertView().setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,dpToPx(220,getResources())));
                }else {
                    mNameRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
                    holder.getConvertView().setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,dpToPx(180,getResources())));
                }

                mNameRecyclerView.setAdapter(new CommonAdapter<RoomEntry.RoomPersonEntry>( mContext,R.layout.child_recycler_item,roomPersonEntryList) {

                    @Override
                    protected void convert(ViewHolder holder, RoomEntry.RoomPersonEntry roomPersonEntry, int position) {
                        EaseImageView mHeader =  holder.getView(R.id.header_iv);
                        TextView mNameTv = holder.getView(R.id.name_tv);
                        TextView mStatusTv = holder.getView(R.id.status_tv);
                        ImageView mStatusIv = holder.getView(R.id.status_iv);
                        if (roomPersonEntryList.size()>5){
                            mNameTv.setTextSize(10);
                            mStatusTv.setTextSize(10);
                        }else{
                            mNameTv.setTextSize(16);
                            mStatusTv.setTextSize(16);
                        }
                        switch (roomPersonEntry.getSex()){
                            case "男":
                                Glide.with(mContext).load(R.drawable.pg_male).into(mHeader);
                                break;
                            case "女":
                                Glide.with(mContext).load(R.drawable.pg_femal).into(mHeader);
                                break;
                            default:
                                mHeader.setVisibility(View.GONE);
                                holder.getConvertView().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                mNameTv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                mNameTv.setGravity(Gravity.CENTER);
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
                            default:
                                holder.getConvertView().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                mStatusIv.setVisibility(View.GONE);
                                mNameTv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                mNameTv.setGravity(Gravity.CENTER);
                                break;
                        }
                    }
                });
            }
        };
        mLeftRecyclerView.setAdapter(commonAdapter);
        mRightRecyclerView.setAdapter(mSecondAdapter);
    }

    private String getDate(){
        String dateFormat = "yyyy-MM";
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return format.format(new Date());
    }

    public static int dpToPx(float dp, Resources resources){
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }
    private void initData(){
        List<RoomEntry.RoomPersonEntry> listOne1 = new ArrayList<>();
        listOne1.add(new RoomEntry.RoomPersonEntry("男","王琨","在岗"));
        listOne1.add(new RoomEntry.RoomPersonEntry("女","杨洁","在岗"));
        mTopList.add(new RoomEntry("101",listOne1));
        List<RoomEntry.RoomPersonEntry> listOne15 = new ArrayList<>();
        listOne15.add(new RoomEntry.RoomPersonEntry("","档案室",""));
        mTopList.add(new RoomEntry("102",listOne15));
        List<RoomEntry.RoomPersonEntry> listOne16 = new ArrayList<>();
        listOne16.add(new RoomEntry.RoomPersonEntry("","荣誉室",""));
        mTopList.add(new RoomEntry("103",listOne16));
        List<RoomEntry.RoomPersonEntry> listOne17 = new ArrayList<>();
        listOne17.add(new RoomEntry.RoomPersonEntry("","司机班",""));
        mTopList.add(new RoomEntry("104",listOne17));
        List<RoomEntry.RoomPersonEntry> listOne18= new ArrayList<>();
        listOne18.add(new RoomEntry.RoomPersonEntry("","机房",""));
        mTopList.add(new RoomEntry("105",listOne18));
        List<RoomEntry.RoomPersonEntry> listOne2 = new ArrayList<>();
        listOne2.add(new RoomEntry.RoomPersonEntry("男","郭旭儿","在岗"));
        listOne2.add(new RoomEntry.RoomPersonEntry("女","焦船鑫","在岗"));
        listOne2.add(new RoomEntry.RoomPersonEntry("男","胡仪凡","在岗"));
        mTopList.add(new RoomEntry("201",listOne2));
        List<RoomEntry.RoomPersonEntry> listOne3 = new ArrayList<>();
        listOne3.add(new RoomEntry.RoomPersonEntry("女","张玉娇","在岗"));
        listOne3.add(new RoomEntry.RoomPersonEntry("女","赵之祺","在岗"));
        listOne3.add(new RoomEntry.RoomPersonEntry("女","刘时雨","在岗"));
        mTopList.add(new RoomEntry("203",listOne3));
        List<RoomEntry.RoomPersonEntry> listOne4 = new ArrayList<>();
        listOne4.add(new RoomEntry.RoomPersonEntry("男","吴华","在岗"));
        mTopList.add(new RoomEntry("204",listOne4));
        List<RoomEntry.RoomPersonEntry> listOne5 = new ArrayList<>();
        listOne5.add(new RoomEntry.RoomPersonEntry("男","李天宇","在岗"));
        listOne5.add(new RoomEntry.RoomPersonEntry("男","李晨","在岗"));
        mTopList.add(new RoomEntry("205",listOne5));
        List<RoomEntry.RoomPersonEntry> listOne6 = new ArrayList<>();
        listOne6.add(new RoomEntry.RoomPersonEntry("男","马杰","在岗"));
        listOne6.add(new RoomEntry.RoomPersonEntry("男","王斯阳","在岗"));
        listOne6.add(new RoomEntry.RoomPersonEntry("男","张俊虎","出差"));
        mTopList.add(new RoomEntry("206",listOne6));
        List<RoomEntry.RoomPersonEntry> listOne7 = new ArrayList<>();
        listOne7.add(new RoomEntry.RoomPersonEntry("男","梁杰","在岗"));
        listOne7.add(new RoomEntry.RoomPersonEntry("男","罗昆","离岗"));
        mTopList.add(new RoomEntry("207",listOne7));
        List<RoomEntry.RoomPersonEntry> listOne19 = new ArrayList<>();
        listOne19.add(new RoomEntry.RoomPersonEntry("","土地储备中心",""));
        mTopList.add(new RoomEntry("208-211",listOne19));
        List<RoomEntry.RoomPersonEntry> listOne8 = new ArrayList<>();
        listOne8.add(new RoomEntry.RoomPersonEntry("男","张继胜","在岗"));
        listOne8.add(new RoomEntry.RoomPersonEntry("男","胡葆松","在岗"));
        listOne8.add(new RoomEntry.RoomPersonEntry("女","罗碧","在岗"));
        listOne8.add(new RoomEntry.RoomPersonEntry("男","王建军","在岗"));
        mTopList.add(new RoomEntry("301",listOne8));
        List<RoomEntry.RoomPersonEntry> listOne9 = new ArrayList<>();
        listOne9.add(new RoomEntry.RoomPersonEntry("男","刘建波","在岗"));
        listOne9.add(new RoomEntry.RoomPersonEntry("女","黄巧云","在岗"));
        listOne9.add(new RoomEntry.RoomPersonEntry("男","陈泉","在岗"));
        listOne9.add(new RoomEntry.RoomPersonEntry("男","张锐","在岗"));
        mTopList.add(new RoomEntry("302",listOne9));
        List<RoomEntry.RoomPersonEntry> listOne10 = new ArrayList<>();
        listOne10.add(new RoomEntry.RoomPersonEntry("男","马程","在岗"));
        listOne10.add(new RoomEntry.RoomPersonEntry("男","王立峰","在岗"));
        listOne10.add(new RoomEntry.RoomPersonEntry("男","闫黎","请假"));
        mTopList.add(new RoomEntry("303",listOne10));
        List<RoomEntry.RoomPersonEntry> listOne11 = new ArrayList<>();
        listOne11.add(new RoomEntry.RoomPersonEntry("男","宋崇斌","在岗"));
        listOne11.add(new RoomEntry.RoomPersonEntry("男","黄伟君","在岗"));
        listOne11.add(new RoomEntry.RoomPersonEntry("男","田海波","请假"));
        mTopList.add(new RoomEntry("304",listOne11));
        List<RoomEntry.RoomPersonEntry> listOne20 = new ArrayList<>();
        listOne20.add(new RoomEntry.RoomPersonEntry("","会议室",""));
        mTopList.add(new RoomEntry("305",listOne20));
        List<RoomEntry.RoomPersonEntry> listOne21 = new ArrayList<>();
        listOne21.add(new RoomEntry.RoomPersonEntry("","洽谈室",""));
        mTopList.add(new RoomEntry("306",listOne21));
        List<RoomEntry.RoomPersonEntry> listOne12 = new ArrayList<>();
        listOne12.add(new RoomEntry.RoomPersonEntry("男","王建坤","在岗"));
        listOne12.add(new RoomEntry.RoomPersonEntry("女","夏晓瑜","在岗"));
        listOne12.add(new RoomEntry.RoomPersonEntry("男","耿强","请假"));
        mTopList.add(new RoomEntry("307",listOne12));
        List<RoomEntry.RoomPersonEntry> listOne13 = new ArrayList<>();
        listOne13.add(new RoomEntry.RoomPersonEntry("男","蒋继舒","在岗"));
        mTopList.add(new RoomEntry("308",listOne13));
        List<RoomEntry.RoomPersonEntry> listOne14 = new ArrayList<>();
        listOne14.add(new RoomEntry.RoomPersonEntry("男","李涛","在岗"));
        listOne14.add(new RoomEntry.RoomPersonEntry("女","王晓凤","在岗"));
        listOne14.add(new RoomEntry.RoomPersonEntry("女","叶士昭","在岗"));
        mTopList.add(new RoomEntry("309",listOne14));
        List<RoomEntry.RoomPersonEntry> listOne22 = new ArrayList<>();
        listOne22.add(new RoomEntry.RoomPersonEntry("男","尤伟","在岗"));
        listOne22.add(new RoomEntry.RoomPersonEntry("男","卢成","在岗"));
        mTopList.add(new RoomEntry("401",listOne22));
        List<RoomEntry.RoomPersonEntry> listOne23 = new ArrayList<>();
        listOne23.add(new RoomEntry.RoomPersonEntry("女","谢彩云","在岗"));
        listOne23.add(new RoomEntry.RoomPersonEntry("女","汪冰玉","在岗"));
        mTopList.add(new RoomEntry("402",listOne23));
        List<RoomEntry.RoomPersonEntry> listOne24 = new ArrayList<>();
        listOne24.add(new RoomEntry.RoomPersonEntry("女","蔚泽文","在岗"));
        listOne24.add(new RoomEntry.RoomPersonEntry("女","赵雁丽","在岗"));
        listOne24.add(new RoomEntry.RoomPersonEntry("女","陶阳","在岗"));
        mTopList.add(new RoomEntry("403",listOne24));
        List<RoomEntry.RoomPersonEntry> listOne25 = new ArrayList<>();
        listOne25.add(new RoomEntry.RoomPersonEntry("","财务部档案室",""));
        mTopList.add(new RoomEntry("404",listOne25));
        List<RoomEntry.RoomPersonEntry> listOne26 = new ArrayList<>();
        listOne26.add(new RoomEntry.RoomPersonEntry("","会议室",""));
        mTopList.add(new RoomEntry("405",listOne26));
        List<RoomEntry.RoomPersonEntry> listOne27 = new ArrayList<>();
        listOne27.add(new RoomEntry.RoomPersonEntry("女","魏晓玲","在岗"));
        listOne27.add(new RoomEntry.RoomPersonEntry("女","魏慧慧","在岗"));
        listOne27.add(new RoomEntry.RoomPersonEntry("女","刘雅飞","在岗"));
        mTopList.add(new RoomEntry("406",listOne27));
        List<RoomEntry.RoomPersonEntry> listOne28 = new ArrayList<>();
        listOne28.add(new RoomEntry.RoomPersonEntry("男","方洲","在岗"));
        listOne28.add(new RoomEntry.RoomPersonEntry("男","吴郧","在岗"));
        listOne28.add(new RoomEntry.RoomPersonEntry("女","程晶晶","在岗"));
        listOne28.add(new RoomEntry.RoomPersonEntry("女","蔡静","在岗"));
        mTopList.add(new RoomEntry("407",listOne28));
        List<RoomEntry.RoomPersonEntry> listOne29 = new ArrayList<>();
        listOne29.add(new RoomEntry.RoomPersonEntry("女","王莉","在岗"));
        listOne29.add(new RoomEntry.RoomPersonEntry("女","刘沙","在岗"));
        listOne29.add(new RoomEntry.RoomPersonEntry("女","陈星","在岗"));
        mTopList.add(new RoomEntry("408",listOne29));
        List<RoomEntry.RoomPersonEntry> listOne30 = new ArrayList<>();
        listOne30.add(new RoomEntry.RoomPersonEntry("男","鲁建山","在岗"));
        listOne30.add(new RoomEntry.RoomPersonEntry("女","蔡鑫","在岗"));
        listOne30.add(new RoomEntry.RoomPersonEntry("女","吴燕","在岗"));
        listOne30.add(new RoomEntry.RoomPersonEntry("女","刘燕","在岗"));
        mTopList.add(new RoomEntry("409",listOne30));
        List<RoomEntry.RoomPersonEntry> listOne31 = new ArrayList<>();
        listOne31.add(new RoomEntry.RoomPersonEntry("男","鲁宏武","在岗"));
        listOne31.add(new RoomEntry.RoomPersonEntry("男","张强","在岗"));
        listOne31.add(new RoomEntry.RoomPersonEntry("男","张正亮","在岗"));
        listOne31.add(new RoomEntry.RoomPersonEntry("女","孙倩","在岗"));
        listOne31.add(new RoomEntry.RoomPersonEntry("女","赵雪婷","在岗"));
        listOne31.add(new RoomEntry.RoomPersonEntry("男","王子","在岗"));
        mTopList.add(new RoomEntry("501",listOne31));
        List<RoomEntry.RoomPersonEntry> listOne32 = new ArrayList<>();
        listOne32.add(new RoomEntry.RoomPersonEntry("男","唐海勇","在岗"));
        listOne32.add(new RoomEntry.RoomPersonEntry("男","薛千里","离岗"));
        listOne32.add(new RoomEntry.RoomPersonEntry("男","余瑞","在岗"));
        listOne32.add(new RoomEntry.RoomPersonEntry("女","徐蕴星","离岗"));
        listOne32.add(new RoomEntry.RoomPersonEntry("女","张志贝","在岗"));
        listOne32.add(new RoomEntry.RoomPersonEntry("男","周俊华","在岗"));
        listOne32.add(new RoomEntry.RoomPersonEntry("男","任重远","离岗"));
        listOne32.add(new RoomEntry.RoomPersonEntry("男","李瑞杰","在岗"));
        listOne32.add(new RoomEntry.RoomPersonEntry("男","刘斌","在岗"));
        mTopList.add(new RoomEntry("502",listOne32));
        List<RoomEntry.RoomPersonEntry> listOne33= new ArrayList<>();
        listOne33.add(new RoomEntry.RoomPersonEntry("女","万媛","在岗"));
        listOne33.add(new RoomEntry.RoomPersonEntry("女","张帆","在岗"));
        listOne33.add(new RoomEntry.RoomPersonEntry("女","赵佳美","在岗"));
        listOne33.add(new RoomEntry.RoomPersonEntry("女","王云霄","在岗"));
        listOne33.add(new RoomEntry.RoomPersonEntry("男","李雪东","在岗"));
        listOne33.add(new RoomEntry.RoomPersonEntry("男","尚玉杰","离岗"));
        listOne33.add(new RoomEntry.RoomPersonEntry("女","焦晓霜","请假"));
        listOne33.add(new RoomEntry.RoomPersonEntry("女","胡佳","在岗"));
        listOne33.add(new RoomEntry.RoomPersonEntry("女","胡晶婷","在岗"));
        listOne33.add(new RoomEntry.RoomPersonEntry("女","陈守贤","离岗"));
        listOne33.add(new RoomEntry.RoomPersonEntry("女","高月","在岗"));
        listOne33.add(new RoomEntry.RoomPersonEntry("女","章胜亚","离岗"));
        listOne33.add(new RoomEntry.RoomPersonEntry("女","邱凡","在岗"));
        listOne33.add(new RoomEntry.RoomPersonEntry("女","徐静","在岗"));
        mTopList.add(new RoomEntry("503",listOne33));
        List<RoomEntry.RoomPersonEntry> listOne34 = new ArrayList<>();
        listOne34.add(new RoomEntry.RoomPersonEntry("","会议室",""));
        mTopList.add(new RoomEntry("504",listOne34));
        List<RoomEntry.RoomPersonEntry> listOne35= new ArrayList<>();
        listOne35.add(new RoomEntry.RoomPersonEntry("","/",""));
        mTopList.add(new RoomEntry("505",listOne35));
        List<RoomEntry.RoomPersonEntry> listOne36= new ArrayList<>();
        listOne36.add(new RoomEntry.RoomPersonEntry("男","熊峰","在岗"));
        mTopList.add(new RoomEntry("506",listOne36));
        List<RoomEntry.RoomPersonEntry> listTwo1= new ArrayList<>();
        listTwo1.add(new RoomEntry.RoomPersonEntry("","/",""));
        mSecondTopList.add(new RoomEntry("101",listTwo1));
        List<RoomEntry.RoomPersonEntry> listTwo2= new ArrayList<>();
        listTwo2.add(new RoomEntry.RoomPersonEntry("","/",""));
        mSecondTopList.add(new RoomEntry("102",listTwo2));
        List<RoomEntry.RoomPersonEntry> listTwo3= new ArrayList<>();
        listTwo3.add(new RoomEntry.RoomPersonEntry("","/",""));
        mSecondTopList.add(new RoomEntry("103",listTwo3));
        List<RoomEntry.RoomPersonEntry> listTwo4= new ArrayList<>();
        listTwo4.add(new RoomEntry.RoomPersonEntry("","/",""));
        mSecondTopList.add(new RoomEntry("104",listTwo4));
        List<RoomEntry.RoomPersonEntry> listTwo5= new ArrayList<>();
        listTwo5.add(new RoomEntry.RoomPersonEntry("","/",""));
        mSecondTopList.add(new RoomEntry("105",listTwo5));
        List<RoomEntry.RoomPersonEntry> listTwo6= new ArrayList<>();
        listTwo6.add(new RoomEntry.RoomPersonEntry("","/",""));
        mSecondTopList.add(new RoomEntry("106",listTwo6));
        List<RoomEntry.RoomPersonEntry> listTwo7= new ArrayList<>();
        listTwo7.add(new RoomEntry.RoomPersonEntry("","/",""));
        mSecondTopList.add(new RoomEntry("107",listTwo7));
        List<RoomEntry.RoomPersonEntry> listTwo8= new ArrayList<>();
        listTwo8.add(new RoomEntry.RoomPersonEntry("","/",""));
        mSecondTopList.add(new RoomEntry("108",listTwo8));
        List<RoomEntry.RoomPersonEntry> listTwo9= new ArrayList<>();
        listTwo9.add(new RoomEntry.RoomPersonEntry("","/",""));
        mSecondTopList.add(new RoomEntry("109",listTwo9));
        List<RoomEntry.RoomPersonEntry> listTwo10= new ArrayList<>();
        listTwo10.add(new RoomEntry.RoomPersonEntry("","/",""));
        mSecondTopList.add(new RoomEntry("110",listTwo10));
        List<RoomEntry.RoomPersonEntry> listTwo11= new ArrayList<>();
        listTwo11.add(new RoomEntry.RoomPersonEntry("","洽谈室",""));
        mSecondTopList.add(new RoomEntry("201",listTwo11));
        List<RoomEntry.RoomPersonEntry> listTwo12= new ArrayList<>();
        listTwo12.add(new RoomEntry.RoomPersonEntry("男","徐勇","在岗"));
        mSecondTopList.add(new RoomEntry("202",listTwo12));
        List<RoomEntry.RoomPersonEntry> listTwo13= new ArrayList<>();
        listTwo13.add(new RoomEntry.RoomPersonEntry("女","梁永劼","在岗"));
        listTwo13.add(new RoomEntry.RoomPersonEntry("女","田成成","在岗"));
        mSecondTopList.add(new RoomEntry("203",listTwo13));
        List<RoomEntry.RoomPersonEntry> listTwo14= new ArrayList<>();
        listTwo14.add(new RoomEntry.RoomPersonEntry("女","夏欢","在岗"));
        listTwo14.add(new RoomEntry.RoomPersonEntry("男","熊星宇","在岗"));
        mSecondTopList.add(new RoomEntry("204",listTwo14));
        List<RoomEntry.RoomPersonEntry> listTwo15= new ArrayList<>();
        listTwo15.add(new RoomEntry.RoomPersonEntry("男","谢洪波","在岗"));
        listTwo15.add(new RoomEntry.RoomPersonEntry("女","沈洁","在岗"));
        mSecondTopList.add(new RoomEntry("205",listTwo15));
        List<RoomEntry.RoomPersonEntry> listTwo16= new ArrayList<>();
        listTwo16.add(new RoomEntry.RoomPersonEntry("女","黎莉","在岗"));
        listTwo16.add(new RoomEntry.RoomPersonEntry("男","史俊龙","在岗"));
        mSecondTopList.add(new RoomEntry("206",listTwo16));
        List<RoomEntry.RoomPersonEntry> listTwo17= new ArrayList<>();
        listTwo17.add(new RoomEntry.RoomPersonEntry("女","徐丹","在岗"));
        listTwo17.add(new RoomEntry.RoomPersonEntry("男","王杰","在岗"));
        listTwo17.add(new RoomEntry.RoomPersonEntry("女","张琴","在岗"));
        listTwo17.add(new RoomEntry.RoomPersonEntry("男","田炜","在岗"));
        mSecondTopList.add(new RoomEntry("207",listTwo17));
        List<RoomEntry.RoomPersonEntry> listTwo18= new ArrayList<>();
        listTwo18.add(new RoomEntry.RoomPersonEntry("女","李莹","在岗"));
        listTwo18.add(new RoomEntry.RoomPersonEntry("男","刘菡","在岗"));
        listTwo18.add(new RoomEntry.RoomPersonEntry("男","杨天池","在岗"));
        listTwo18.add(new RoomEntry.RoomPersonEntry("男","陈鹏飞","在岗"));
        mSecondTopList.add(new RoomEntry("208",listTwo18));
        List<RoomEntry.RoomPersonEntry> listTwo19= new ArrayList<>();
        listTwo19.add(new RoomEntry.RoomPersonEntry("男","申耀强","在岗"));
        mSecondTopList.add(new RoomEntry("209",listTwo19));
        List<RoomEntry.RoomPersonEntry> listTwo20= new ArrayList<>();
        listTwo20.add(new RoomEntry.RoomPersonEntry("女","尹静","在岗"));
        listTwo20.add(new RoomEntry.RoomPersonEntry("女","宋爽","在岗"));
        listTwo20.add(new RoomEntry.RoomPersonEntry("男","周文杰","在岗"));
        mSecondTopList.add(new RoomEntry("210",listTwo20));
        List<RoomEntry.RoomPersonEntry> listTwo21= new ArrayList<>();
        listTwo21.add(new RoomEntry.RoomPersonEntry("","洽谈室",""));
        mSecondTopList.add(new RoomEntry("301",listTwo21));
        List<RoomEntry.RoomPersonEntry> listTwo22= new ArrayList<>();
        listTwo22.add(new RoomEntry.RoomPersonEntry("男","伍军","在岗"));
        mSecondTopList.add(new RoomEntry("302",listTwo22));
        List<RoomEntry.RoomPersonEntry> listTwo23= new ArrayList<>();
        listTwo23.add(new RoomEntry.RoomPersonEntry("男","黄开均","在岗"));
        mSecondTopList.add(new RoomEntry("303",listTwo23));
        List<RoomEntry.RoomPersonEntry> listTwo24= new ArrayList<>();
        listTwo24.add(new RoomEntry.RoomPersonEntry("男","刘关平","在岗"));
        mSecondTopList.add(new RoomEntry("304",listTwo24));
        List<RoomEntry.RoomPersonEntry> listTwo25= new ArrayList<>();
        listTwo25.add(new RoomEntry.RoomPersonEntry("男","何细民","在岗"));
        mSecondTopList.add(new RoomEntry("305",listTwo25));
        List<RoomEntry.RoomPersonEntry> listTwo26= new ArrayList<>();
        listTwo26.add(new RoomEntry.RoomPersonEntry("男","胡钧","在岗"));
        mSecondTopList.add(new RoomEntry("306",listTwo26));
        List<RoomEntry.RoomPersonEntry> listTwo27= new ArrayList<>();
        listTwo27.add(new RoomEntry.RoomPersonEntry("","洽谈室",""));
        mSecondTopList.add(new RoomEntry("307",listTwo27));
        List<RoomEntry.RoomPersonEntry> listTwo28= new ArrayList<>();
        listTwo28.add(new RoomEntry.RoomPersonEntry("男","郝智海","在岗"));
        mSecondTopList.add(new RoomEntry("308",listTwo28));
        List<RoomEntry.RoomPersonEntry> listTwo29= new ArrayList<>();
        listTwo29.add(new RoomEntry.RoomPersonEntry("男","潘峰","在岗"));
        mSecondTopList.add(new RoomEntry("309",listTwo29));
        List<RoomEntry.RoomPersonEntry> listTwo30= new ArrayList<>();
        listTwo30.add(new RoomEntry.RoomPersonEntry("男","陈建伟","在岗"));
        mSecondTopList.add(new RoomEntry("310",listTwo30));
        List<RoomEntry.RoomPersonEntry> listTwo31= new ArrayList<>();
        listTwo31.add(new RoomEntry.RoomPersonEntry("","洽谈室",""));
        mSecondTopList.add(new RoomEntry("401",listTwo31));
        List<RoomEntry.RoomPersonEntry> listTwo32= new ArrayList<>();
        listTwo32.add(new RoomEntry.RoomPersonEntry("男","胡顺成","在岗"));
        mSecondTopList.add(new RoomEntry("402",listTwo32));
        List<RoomEntry.RoomPersonEntry> listTwo33= new ArrayList<>();
        listTwo33.add(new RoomEntry.RoomPersonEntry("男","徐龙","在岗"));
        listTwo33.add(new RoomEntry.RoomPersonEntry("女","张君卓","在岗"));
        listTwo33.add(new RoomEntry.RoomPersonEntry("男","王志航","在岗"));
        mSecondTopList.add(new RoomEntry("403",listTwo33));
        List<RoomEntry.RoomPersonEntry> listTwo34= new ArrayList<>();
        listTwo34.add(new RoomEntry.RoomPersonEntry("男","王翬","在岗"));
        mSecondTopList.add(new RoomEntry("404",listTwo34));
        List<RoomEntry.RoomPersonEntry> listTwo35= new ArrayList<>();
        listTwo35.add(new RoomEntry.RoomPersonEntry("女","陶景怡","在岗"));
        listTwo35.add(new RoomEntry.RoomPersonEntry("女","黄婷婷","在岗"));
        listTwo35.add(new RoomEntry.RoomPersonEntry("女","黄亚昭","在岗"));
        mSecondTopList.add(new RoomEntry("405",listTwo35));
        List<RoomEntry.RoomPersonEntry> listTwo36= new ArrayList<>();
        listTwo36.add(new RoomEntry.RoomPersonEntry("女","李有泽","在岗"));
        listTwo36.add(new RoomEntry.RoomPersonEntry("女","周雅倜","在岗"));
        mSecondTopList.add(new RoomEntry("406",listTwo36));
        List<RoomEntry.RoomPersonEntry> listTwo37= new ArrayList<>();
        listTwo37.add(new RoomEntry.RoomPersonEntry("女","孟华荣","在岗"));
        mSecondTopList.add(new RoomEntry("407",listTwo37));
        List<RoomEntry.RoomPersonEntry> listTwo38= new ArrayList<>();
        listTwo38.add(new RoomEntry.RoomPersonEntry("女","姜富玲","在岗"));
        listTwo38.add(new RoomEntry.RoomPersonEntry("男","王超","在岗"));
        mSecondTopList.add(new RoomEntry("408",listTwo38));
        List<RoomEntry.RoomPersonEntry> listTwo39= new ArrayList<>();
        listTwo39.add(new RoomEntry.RoomPersonEntry("男","肖吉夫","在岗"));
        listTwo39.add(new RoomEntry.RoomPersonEntry("女","张祎阳","在岗"));
        listTwo39.add(new RoomEntry.RoomPersonEntry("女","余千惠","在岗"));
        mSecondTopList.add(new RoomEntry("409",listTwo39));
        List<RoomEntry.RoomPersonEntry> listTwo40= new ArrayList<>();
        listTwo40.add(new RoomEntry.RoomPersonEntry("女","谢晶晶","在岗"));
        listTwo40.add(new RoomEntry.RoomPersonEntry("女","陈祖君","在岗"));
        mSecondTopList.add(new RoomEntry("410",listTwo40));
        List<RoomEntry.RoomPersonEntry> listTwo41= new ArrayList<>();
        listTwo41.add(new RoomEntry.RoomPersonEntry("女","田羲","在岗"));
        listTwo41.add(new RoomEntry.RoomPersonEntry("男","余华栋","在岗"));
        mSecondTopList.add(new RoomEntry("501",listTwo41));
        List<RoomEntry.RoomPersonEntry> listTwo42= new ArrayList<>();
        listTwo42.add(new RoomEntry.RoomPersonEntry("男","曹进郊","在岗"));
        listTwo42.add(new RoomEntry.RoomPersonEntry("男","郭伟涛","在岗"));
        mSecondTopList.add(new RoomEntry("502",listTwo42));
        List<RoomEntry.RoomPersonEntry> listTwo43= new ArrayList<>();
        listTwo43.add(new RoomEntry.RoomPersonEntry("女","许丹丹","在岗"));
        listTwo43.add(new RoomEntry.RoomPersonEntry("女","尚婷婷","在岗"));
        listTwo43.add(new RoomEntry.RoomPersonEntry("女","郭宇静","在岗"));
        mSecondTopList.add(new RoomEntry("503",listTwo43));
        List<RoomEntry.RoomPersonEntry> listTwo44= new ArrayList<>();
        listTwo44.add(new RoomEntry.RoomPersonEntry("","机房",""));
        mSecondTopList.add(new RoomEntry("504",listTwo44));
        List<RoomEntry.RoomPersonEntry> listTwo45= new ArrayList<>();
        listTwo45.add(new RoomEntry.RoomPersonEntry("","/",""));
        mSecondTopList.add(new RoomEntry("505",listTwo45));
        List<RoomEntry.RoomPersonEntry> listTwo46= new ArrayList<>();
        listTwo46.add(new RoomEntry.RoomPersonEntry("女","周金凤","在岗"));
        listTwo46.add(new RoomEntry.RoomPersonEntry("女","胡挺杨","在岗"));
        mSecondTopList.add(new RoomEntry("506",listTwo46));
        List<RoomEntry.RoomPersonEntry> listTwo47= new ArrayList<>();
        listTwo47.add(new RoomEntry.RoomPersonEntry("女","李宝荣","在岗"));
        mSecondTopList.add(new RoomEntry("507",listTwo47));
        List<RoomEntry.RoomPersonEntry> listTwo48= new ArrayList<>();
        listTwo48.add(new RoomEntry.RoomPersonEntry("女","杨露露","在岗"));
        listTwo48.add(new RoomEntry.RoomPersonEntry("女","曹尼莉","在岗"));
        listTwo48.add(new RoomEntry.RoomPersonEntry("女","丁一帆","在岗"));
        mSecondTopList.add(new RoomEntry("508",listTwo48));
        List<RoomEntry.RoomPersonEntry> listTwo49= new ArrayList<>();
        listTwo49.add(new RoomEntry.RoomPersonEntry("女","李莎","在岗"));
        listTwo49.add(new RoomEntry.RoomPersonEntry("女","施智慧","在岗"));
        mSecondTopList.add(new RoomEntry("509",listTwo49));
        List<RoomEntry.RoomPersonEntry> listTwo50= new ArrayList<>();
        listTwo50.add(new RoomEntry.RoomPersonEntry("女","邓越敏","在岗"));
        listTwo50.add(new RoomEntry.RoomPersonEntry("女","彭诗园","在岗"));
        mSecondTopList.add(new RoomEntry("510",listTwo50));
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
        mSecondAdapter.notifyDataSetChanged();
    }

}
