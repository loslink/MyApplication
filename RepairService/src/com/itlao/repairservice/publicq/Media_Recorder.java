package com.itlao.repairservice.publicq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.itlao.repairservice.utils.GetPhoneContacts.SamContact;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class Media_Recorder {


	private static MediaRecorder recorder;

	public static MediaRecorder initializeAudio() {  
        recorder = new MediaRecorder();// new出MediaRecorder对象  
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
        // 设置MediaRecorder的音频源为麦克风  
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);  
        // 设置MediaRecorder录制的音频格式  
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);  
        // 设置MediaRecorder录制音频的编码为amr.  
        recorder.setOutputFile("storage/sdcard1/peipei.amr");  
        // 设置录制好的音频文件保存路径  
        try {  
            recorder.prepare();// 准备录制  
            
        } catch (IllegalStateException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  

        return recorder;
    }  

	       
	    
}
