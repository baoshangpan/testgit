
package com.byd.glasses;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.byd.glasses.constant.VoiceConstant;
import com.byd.glasses.service.VoiceService;
import com.byd.glasses.util.NetUtil;
import com.byd.glasses.util.SwipeViewAdapter;
import com.byd.glasses.util.ToastUtil;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "-->MainActivity";
    private ViewPager mViewPager;
    private TextView mTxtPrew,mTxtNext;
    private ActionBar mActionBar;
    private Context mContext;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG, "onCreate");
        mContext = this;
        init();
    }

    private void init() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mTxtPrew = (TextView) findViewById(R.id.textViewPrev);
        mTxtNext = (TextView) findViewById(R.id.textViewNext);
        mViewPager.setAdapter(new SwipeViewAdapter(getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener(new GuidePageChangeListener());
        
        Log.v(TAG, "register broadcast");
        IntentFilter mainFilter = new IntentFilter();
        mainFilter.addAction(VoiceConstant.RESULT_TO_MAIN_ACTIVITY);
        registerReceiver(mMainReceiver, mainFilter);
        
        mActionBar = getActionBar();
        if (!NetUtil.isConnect(MainActivity.this)) {
            mActionBar.setTitle("No Network Connected");
            ToastUtil.toast(mContext, "No Network Connected");
        } else {
            mActionBar.setTitle("HOME");
            startService(new Intent(MainActivity.this, VoiceService.class));
            Log.v(TAG, "start service");
        }
    }

    private BroadcastReceiver mMainReceiver = new BroadcastReceiver() {
        
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (VoiceConstant.RESULT_TO_MAIN_ACTIVITY.equals(action)) {
                String mResult = intent.getExtras().getString(VoiceConstant.KEY_DATA);
                Log.v(TAG, mResult);
                if ("云服务".equals(mResult)) {
                    Log.v(TAG, "cloud service match");
                }else if ("下一页".equals(mResult)) {
                    Log.v(TAG, "next command match");
                }
            }
        }
    };

    public class GuidePageChangeListener implements OnPageChangeListener{

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            Log.v(TAG, String.valueOf(arg0));
            switch(arg0){
                case 0:
                    mTxtPrew.setVisibility(View.INVISIBLE);
                    mTxtNext.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    mTxtPrew.setVisibility(View.VISIBLE);
                    mTxtNext.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    mTxtPrew.setVisibility(View.VISIBLE);
                    mTxtNext.setVisibility(View.INVISIBLE);
                    break;
                default :
                    mTxtPrew.setVisibility(View.INVISIBLE);
                    mTxtNext.setVisibility(View.INVISIBLE);
                    break;
            }
        }
        
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            Log.v(TAG, "finish");
            finish();
        }
        return true;
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
        sendBroadcast(new Intent(VoiceConstant.SERVICE_PAUSE));
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        sendBroadcast(new Intent(VoiceConstant.SERVICE_RESTART));
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        sendBroadcast(new Intent(VoiceConstant.SERVICE_STOP));
        this.unregisterReceiver(mMainReceiver);
    }
}
