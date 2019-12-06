package com.example.myitime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class DetailActivity extends AppCompatActivity {

    ImageButton editButton, deletButton;
    public static final int ITEM_EDIT = 902;
    private String title;
    private int year;
    private int month;
    private int date;
    private int image;
    private Timer mTimer;
    ImageView backGround;
    TextView titleText;
    TextView setTime;
    TextView mDays_Tv, mHours_Tv, mMinutes_Tv, mSeconds_Tv;

    //下面的具体时间通过系统时间获得，现在先初始化做倒计时
    Calendar calendar = Calendar.getInstance();
    private long mDay;// 天
    private long mHour;//小时,
    private long mMin;//分钟,
    private long mSecond;//秒

    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                computeTime();
                mDays_Tv.setText(mDay+"");//天数不用补位
                mHours_Tv.setText(getTv(mHour));
                mMinutes_Tv.setText(getTv(mMin));
                mSeconds_Tv.setText(getTv(mSecond));
                if (mSecond == 0 &&  mDay == 0 && mHour == 0 && mMin == 0 ) {
                    mTimer.cancel();
                }
            }
        }
    };

    private String getTv(long l){
        if(l>=10){
            return l+"";
        }else{
            return "0"+l;//小于10,,前面补位一个"0"
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        backGround=(ImageView)findViewById(R.id.image_back_ground);
        titleText=(TextView)findViewById(R.id.name_text_view);
        setTime=(TextView)findViewById(R.id.text_view_settime);

        mTimer = new Timer();
        mDays_Tv = (TextView) findViewById(R.id.day_left);
        mHours_Tv = (TextView) findViewById(R.id.hour_left);
        mMinutes_Tv = (TextView) findViewById(R.id.minute_left);
        mSeconds_Tv = (TextView) findViewById(R.id.second_left);

        Log.i(DetailActivity.ACTIVITY_SERVICE, "This is Information");
        title=getIntent().getStringExtra("title");
        titleText.setText(title);
        year=getIntent().getIntExtra("year",2019);
        month=getIntent().getIntExtra("month",1);
        date=getIntent().getIntExtra("date",1);
        image=getIntent().getIntExtra("image",R.drawable.pic1);
        backGround.setImageResource(image);
        setTime.setText(String.valueOf(year)+'年'+String.valueOf(month)+'月'+String.valueOf(date)+'日');
        //获取当前时间，剩余天数由下面获得，24-当前小时为剩余小时，60-当前分为剩余分，60-当前秒为剩余秒
        mHour=24 - calendar.get(Calendar.HOUR_OF_DAY);
        mMin=60 - calendar.get(Calendar.MINUTE);
        mSecond=60 - calendar.get(Calendar.SECOND);

        //获取还有多少天
        String s1= String.valueOf(year)+'-'+String.valueOf(month)+'-'+String.valueOf(date);
        String s2 = String.valueOf(calendar.get(Calendar.YEAR))+'-'+String.valueOf(calendar.get(Calendar.MONTH))+'-'+String.valueOf(calendar.get(Calendar.DATE));
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        Date d1= null;
        try {
            d1 = df.parse(s1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date d2= null;
        try {
            d2 = df.parse(s2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mDay = (d1.getTime()-d2.getTime())/(60*60*1000*24);
        startRun();
    }

    private void startRun() {
        TimerTask mTimerTask = new TimerTask() {

            public void run() {
                Message message = Message.obtain();
                message.what = 1;
                timeHandler.sendMessage(message);
            }
        };
        mTimer.schedule(mTimerTask,0,1000);
    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
                if (mHour < 0) {
                    // 倒计时结束
                    mHour = 23;
                    mDay--;
                    if(mDay < 0){
                        // 倒计时结束
                        mDay = 0;
                        mHour= 0;
                        mMin = 0;
                        mSecond = 0;
                    }
                }
            }
        }
    }


}