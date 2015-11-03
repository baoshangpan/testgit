package com.byd.glasses.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.byd.glasses.constant.VoiceConstant;

public class SystemUtil extends ContextWrapper{
    private static final String TAG ="-->SystemUtil";
    private Context mContext;
    
    public SystemUtil(Context base) {
        super(base);
        this.mContext = base;
    }

    public String getRunningActivityName(){
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }
    
    public boolean isCurrentAppActivity(){
        String s,subString;
        s = getRunningActivityName();//get the full activity name,e.g. com.byd.glasses.activity.MainActivity
        subString = s.substring(0, s.indexOf(".", s.indexOf(".", s.indexOf(".")+1)+1)+1);//get package name before the third "."  e.g. com.byd.glasses.
        Log.v(TAG, subString);
        if (VoiceConstant.PACKAGE.equals(subString)) {
            return true;
        }
        return false;
    }
}
