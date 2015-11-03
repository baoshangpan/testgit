package com.byd.glasses.activity;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.byd.glasses.R;
import com.byd.glasses.constant.VoiceConstant;

public class CloudActivity extends FragmentActivity {

    private static final String TAG = "-->CloudActivity";
    private ActionBar mActionBar;
    private Context mContext;
    
    public CloudActivity() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        System.out.println(TAG);
        setContentView(R.layout.activity_cloud);
        mActionBar = getActionBar();
        mActionBar.setTitle("ÔÆ·þÎñ");
        mContext = this;
        init();
    }

    private void init() {
        IntentFilter mFilter = new IntentFilter();  
        mFilter.addAction(VoiceConstant.RESULT_TO_CLOUD_ACTIVITY);
        registerReceiver(mReceiver, mFilter);
    }
    
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v(TAG, action);
            if (VoiceConstant.RESULT_TO_CLOUD_ACTIVITY.equals(action)) {
                String mResult = intent.getExtras().getString(VoiceConstant.KEY_DATA);
                Log.v(TAG, mResult);
                if ("µÇÂ¼".equals(mResult)) {
                    Log.v(TAG, "login match");
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mReceiver);
    }
}
