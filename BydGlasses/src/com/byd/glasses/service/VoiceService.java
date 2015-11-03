
package com.byd.glasses.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.byd.glasses.constant.CommonActivity;
import com.byd.glasses.constant.VoiceConstant;
import com.byd.glasses.util.SystemUtil;
import com.byd.glasses.util.VoiceJsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

public class VoiceService extends Service {

    private static final String TAG = "-->VoiceService";
    private SpeechRecognizer mIat;
    private Context mContext;
    private getActivityThread mGetActivityThread = null;
    private SystemUtil mCurrentActivity;
    private boolean isRunning = true;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Log.v(TAG, "onCreate");
        mCurrentActivity = new SystemUtil(mContext);
        init();
    }

    private void init() {
        IntentFilter serverFilter = new IntentFilter();
        serverFilter.addAction(VoiceConstant.SERVICE_STOP);
        serverFilter.addAction(VoiceConstant.SERVICE_PAUSE);
        serverFilter.addAction(VoiceConstant.SERVICE_RESTART);
        registerReceiver(mServerReceiver, serverFilter);
        initVoice();
//        mGetActivityThread = new getActivityThread();
//        mGetActivityThread.start();
    }

    private void initVoice() {
        StringBuffer param = new StringBuffer();
        param.append("appid=56205f4a");
        param.append(",");
        param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
        param.append(",");
        param.append(SpeechConstant.FORCE_LOGIN + "=true");// not run in main thread
        SpeechUtility.createUtility(mContext, param.toString());
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
        setVoiceParam();
        startVoice();
    }

    private BroadcastReceiver mServerReceiver = new BroadcastReceiver() {
        
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v(TAG, action);
            if (VoiceConstant.SERVICE_STOP.equals(action)) {
                Log.v(TAG, "stopSelf");
                stopVoice();
                stopSelf();
            } else if (VoiceConstant.SERVICE_PAUSE.equals(action)){
                Log.v(TAG, "pauseVoice");
                if (!mCurrentActivity.isCurrentAppActivity()) {
                    stopVoice();
                }
            } else if (VoiceConstant.SERVICE_RESTART.equals(action)) {
                Log.v(TAG, "restartVoice");
                startVoice();
            }
        }
    };

    private void startVoice() {
        if (null != mIat && !mIat.isListening()) {
            mIat.startListening(mRecognizerListener);
        }
    }

    private void restartVoice() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "restart");
                stopVoice();
                startVoice();
            }
        }, 50);
    }
    
    private void stopVoice() {
        if (null != mIat && mIat.isListening()) {
            mIat.stopListening();
            mIat.cancel();
        }
    }

    private void setVoiceParam() {
        mIat.setParameter(SpeechConstant.PARAMS, null);
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        mIat.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, "30000");
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
    }
    
    
    
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int errorCode) {
            if (ErrorCode.SUCCESS != errorCode) {
                Log.v(TAG, "Initial failed ,error code£º " + errorCode);
            }
        }
    };

    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            Log.v(TAG, "Volume: " + volume);
//            Log.v(TAG, "Audio stream: " + data.length);
        }

        @Override
        public void onResult(RecognizerResult result, boolean isLast) {
            Log.v(TAG, result.getResultString());
            String res = VoiceJsonParser.parseIatResult(result.getResultString());
            Log.v(TAG, res);
            String currentActivity = mCurrentActivity.getRunningActivityName();
            Log.d(TAG, currentActivity);
            if (CommonActivity.MAIN_ACTIVITY.equals(currentActivity)) {
                Intent dataIntent = new Intent(VoiceConstant.RESULT_TO_MAIN_ACTIVITY);
                dataIntent.putExtra(VoiceConstant.KEY_DATA, res);
                sendBroadcast(dataIntent);
            }else if (CommonActivity.CLOUD_ACTIVITY.equals(currentActivity)) {
                Intent dataIntent = new Intent(VoiceConstant.RESULT_TO_CLOUD_ACTIVITY);
                dataIntent.putExtra(VoiceConstant.KEY_DATA, res);
                sendBroadcast(dataIntent);
            }else if (CommonActivity.HUD_ACTIVITY.equals(currentActivity)) {
                Intent dataIntent = new Intent(VoiceConstant.RESULT_TO_HUD_ACTIVITY);
                dataIntent.putExtra(VoiceConstant.KEY_DATA, res);
                sendBroadcast(dataIntent);
            }else if (CommonActivity.TEACH_ACTIVITY.equals(currentActivity)) {
                Intent dataIntent = new Intent(VoiceConstant.RESULT_TO_TEACH_ACTIVITY);
                dataIntent.putExtra(VoiceConstant.KEY_DATA, res);
                sendBroadcast(dataIntent);
            }
            restartVoice();
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
//                Log.v(TAG, "Session ID: " + sid); //always null while debug
            }
        }

        @Override
        public void onError(SpeechError error) {
            Log.v(TAG, "Error code: " + error.getPlainDescription(true));
            if (ErrorCode.ERROR_NO_NETWORK == error.getErrorCode()) {
                stopVoice();
            } else {
                restartVoice();
            }
        }

        @Override
        public void onEndOfSpeech() {
            Log.v(TAG, "End of speech");
        }

        @Override
        public void onBeginOfSpeech() {
            Log.v(TAG, "Begin of speech");
        }
    };

    private class getActivityThread extends Thread {
        boolean isCurrent;
        public void run(){
            while(isRunning){
                isCurrent = mCurrentActivity.isCurrentAppActivity();
                Log.v(TAG, String.valueOf(isCurrent));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        if (null != mGetActivityThread) {
            mGetActivityThread.interrupt();
            mGetActivityThread = null;
            isRunning = false;
            Log.v(TAG, "stopThread");
        }
        this.unregisterReceiver(mServerReceiver);
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
