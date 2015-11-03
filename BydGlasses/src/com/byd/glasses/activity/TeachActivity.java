package com.byd.glasses.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.byd.glasses.R;
import com.byd.glasses.R.layout;

public class TeachActivity extends FragmentActivity {

    private static final String TAG = "-->TeachActivity";
    private ActionBar mActionBar;
    public TeachActivity() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        System.out.println(TAG);
        setContentView(R.layout.activity_teach);
        mActionBar = getActionBar();
        mActionBar.setTitle("AR");
    }
}
