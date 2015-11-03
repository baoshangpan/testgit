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

public class HudActivity extends FragmentActivity {

    private static final String TAG = "-->HudActivity";
    private ActionBar mActionBar;
    private Context mContext;
    
    public HudActivity() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        System.out.println(TAG);
        setContentView(R.layout.activity_hud);
        mActionBar = getActionBar();
        mActionBar.setTitle("HUD");
        mContext = this;
        init();
    }

    private void init() {
        IntentFilter mFilter = new IntentFilter();  
        mFilter.addAction(VoiceConstant.RESULT_TO_HUD_ACTIVITY);
        registerReceiver(mReceiver, mFilter);
    }
    
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v(TAG, action);
            if (VoiceConstant.RESULT_TO_HUD_ACTIVITY.equals(action)) {
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
