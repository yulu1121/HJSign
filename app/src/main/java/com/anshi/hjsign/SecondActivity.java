package com.anshi.hjsign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anshi.hjsign.chart.BarChart.LBarChartView;
import com.anshi.hjsign.chart.PieChartView;
import com.anshi.hjsign.entry.PersonDetailEntry;
import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SecondActivity extends Activity {
    //  0    255    255
    private int yellowColor = Color.argb(255, 255, 255, 0);
    private int greenColor = Color.argb(255, 0, 255, 0);
    private int redColor = Color.argb(255, 255, 0, 0);
    private int pureColor = Color.argb(255,   255 , 0  , 255  );
    private void initPieDatas() {

        List<Float> mRatios = new ArrayList<>();

        List<String> mDescription = new ArrayList<>();

        List<Integer> mArcColors = new ArrayList<>();

        mRatios.add(0.91f);
        mRatios.add(0.04f);
        mRatios.add(0.01f);
        mRatios.add(0.04f);

        mArcColors.add(greenColor);
        mArcColors.add(redColor);
        mArcColors.add(yellowColor);
        mArcColors.add(pureColor);

        mDescription.add("到岗");
        mDescription.add("离岗");
        mDescription.add("请假");
        mDescription.add("出差");

        //点击动画开启
        pieChartView.setCanClickAnimation(true);
        pieChartView.setDatas(mRatios, mArcColors, mDescription);
    }

    private void initNewBarDatas() {
        final List<Double> datas = new ArrayList<>();
        final List<String> description = new ArrayList<>();
        datas.add(130d);
        datas.add(7d);
        datas.add(2d);
        datas.add(14d);
        description.add("到岗");
        description.add("离岗");
        description.add("请假");
        description.add("出差");
        barChartView.setDatas(datas, description, true);

    }

    private PieChartView pieChartView;
    private LBarChartView barChartView;
    private RecyclerView mMeetingRecycler;
    private RecyclerView mNotArrvingRecycler;
    private TextView mCalendarDay;
    private TextView mCalendarDate;
    private List<PersonDetailEntry> mTopList  = new ArrayList<>();
    private List<PersonDetailEntry> mSecondTopList = new ArrayList<>();
    private HeaderAndFooterWrapper mHeaderOneAdapter;
    private HeaderAndFooterWrapper mHeaderTwoAdapter;
    private CommonAdapter<PersonDetailEntry> adapterOne;
    private CommonAdapter<PersonDetailEntry> adapterTwo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        registerReceiver(usBroadcastReceiver, filter);
        initData();
    }

    private void initData() {
       mTopList.add(new PersonDetailEntry("北楼305","","可用","会议室"));
       mTopList.add(new PersonDetailEntry("北楼306","","使用中","洽谈室"));
       mTopList.add(new PersonDetailEntry("北楼405","","使用中","会议室"));
       mTopList.add(new PersonDetailEntry("北楼504","","可用","会议室"));
       mTopList.add(new PersonDetailEntry("南楼201","","可用","洽谈室"));
       mTopList.add(new PersonDetailEntry("南楼301","","使用中","洽谈室"));
       mTopList.add(new PersonDetailEntry("南楼307","","可用","洽谈室"));
       mTopList.add(new PersonDetailEntry("南楼401","","使用中","洽谈室"));
       adapterOne.notifyDataSetChanged();
       mHeaderOneAdapter.notifyDataSetChanged();
       mSecondTopList.add(new PersonDetailEntry("北楼207","男","","罗昆"));
       mSecondTopList.add(new PersonDetailEntry("北楼502","男","","薛千里"));
       mSecondTopList.add(new PersonDetailEntry("北楼502","女","","徐蕴星"));
       mSecondTopList.add(new PersonDetailEntry("北楼502","男","","章胜亚"));
       mSecondTopList.add(new PersonDetailEntry("南楼409","男","","肖吉夫"));
       adapterTwo.notifyDataSetChanged();
       mHeaderTwoAdapter.notifyDataSetChanged();
       initNewBarDatas();
       initPieDatas();
    }

    private void initView() {
        mCalendarDay = findViewById(R.id.calendar_day);
        mCalendarDate = findViewById(R.id.calendar_date);
        mCalendarDay.setText(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        mCalendarDate.setText(getDate());
        pieChartView = findViewById(R.id.pie_chart);
        barChartView = findViewById(R.id.bar_chart);
        mMeetingRecycler = findViewById(R.id.meeting_recycler);
        mNotArrvingRecycler = findViewById(R.id.not_on_recycler);
        mMeetingRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mNotArrvingRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapterOne = new CommonAdapter<PersonDetailEntry>(this,R.layout.one_recycler_item,mTopList) {
            @Override
            protected void convert(ViewHolder holder, PersonDetailEntry personDetailEntry, int position) {
                TextView mMeetingName = holder.getView(R.id.meeting_name);
                TextView mMeetingCategory = holder.getView(R.id.meeting_category);
                ImageView mMeetingStatus = holder.getView(R.id.meeting_status_iv);
                mMeetingName.setText(personDetailEntry.getRoomTitle());
                mMeetingCategory.setText(personDetailEntry.getName());
                switch (personDetailEntry.getStatus()){
                    case "可用":
                        mMeetingStatus.setImageResource(R.drawable.green_dot);
                        break;
                    case "使用中":
                       mMeetingStatus.setImageResource(R.drawable.red_dot);
                        break;
                }
            }
        };
        @SuppressLint("InflateParams") View headerOne = LayoutInflater.from(this).inflate(R.layout.home_recycler_header,null);
        mHeaderOneAdapter = new HeaderAndFooterWrapper(adapterOne);
        mHeaderOneAdapter.addHeaderView(headerOne);
        mMeetingRecycler.setAdapter(mHeaderOneAdapter);
        adapterTwo = new CommonAdapter<PersonDetailEntry>(this,R.layout.two_recycler_item,mSecondTopList) {
            @Override
            protected void convert(ViewHolder holder, PersonDetailEntry personDetailEntry, int position) {
                 TextView mTitle = holder.getView(R.id.member_room_title);
                 TextView mName = holder.getView(R.id.member_name);
                 ImageView mSexIv= holder.getView(R.id.member_sex);
                 mTitle.setText(personDetailEntry.getRoomTitle());
                 mName.setText(personDetailEntry.getName());
                 switch (personDetailEntry.getSex()){
                     case "男":
                         Glide.with(mContext).load(R.drawable.pg_man_blue_new).into(mSexIv);
                         break;
                     case "女":
                         Glide.with(mContext).load(R.drawable.pg_woman_blue_new).into(mSexIv);
                         break;
                 }
            }
        };
        @SuppressLint("InflateParams") View headerTwo = LayoutInflater.from(this).inflate(R.layout.second_header,null);
        mHeaderTwoAdapter = new HeaderAndFooterWrapper(adapterTwo);
        mHeaderTwoAdapter.addHeaderView(headerTwo);
        mNotArrvingRecycler.setAdapter(mHeaderTwoAdapter);
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
                        mCalendarDay.setText(getDate());
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

    private String getDate(){
        //yyyy年MM月dd日 HH:mm:ss
        String dateFormat = "yyyy-MM";
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return format.format(new Date());
    }
}
