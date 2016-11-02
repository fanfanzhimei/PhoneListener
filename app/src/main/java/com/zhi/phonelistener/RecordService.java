package com.zhi.phonelistener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/11/2.
 */
public class RecordService extends Service{
    private MediaRecorder mediaRecorder;
    private String number;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(App.getContext(), "服务启动", Toast.LENGTH_SHORT).show();
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    class MyPhoneStateListener extends PhoneStateListener{  // 是别人给我打电话
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_RINGING:  // 来电
                    number = incomingNumber;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:  // 通话中
                    saveFile();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:  // 挂断
                    if(null != mediaRecorder) {
                        mediaRecorder.stop();
                        mediaRecorder.release();  // 释放资源
                        mediaRecorder = null;
                    }
                    break;
            }
        }
    }

    private void saveFile(){
        //  开始录音后，保存文件
        File file = new File(Environment.getExternalStorageDirectory(), number + "_"+System.currentTimeMillis()+".3gp");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  // 监听麦克的声音
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);  // 录音保存的格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(file.getAbsolutePath());  // 录音保存路径
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}