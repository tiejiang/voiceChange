/**
 * 为录音做准备，以及录音停止
 * */
package com.example.changeyourvioce;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.tecunhuman.AndroidJNI;
import org.tecunhuman.ExtAudioRecorder;

import android.util.Log;

public class RecordPrepare {
	
	//录音开始的准备
	public static void prepare(ExtAudioRecorder extAudioRecorder, String dataPath){
		extAudioRecorder.setOutputFile(dataPath + "source.wav");
		extAudioRecorder.prepare();
		extAudioRecorder.start();
	}
	//录音停止
	public static ExtAudioRecorder RecordStop(ExtAudioRecorder extAudioRecorder){
		// TODO Auto-generated method stub
//		extAudioRecorder.reset();
		extAudioRecorder.stop();
		extAudioRecorder.release();
		return extAudioRecorder = null;//必须将ExtAudioRecorder恢复为null状态，为下次录制做好准备！
	}
	/**
	 * @param 待变音文件，已变音文件，音频节拍，音频频率，音频速度
	 * @return
	 * */
	public static void changeVoice(String inFilename, String outFilename, float tempoDelta, float pitchDelta, float rateDelta){
		AndroidJNI.soundStretch.process(inFilename, outFilename, tempoDelta, pitchDelta, rateDelta);
	}
	//防止录音文件重名，加上系统当前时间
	private static String getCurrentSystemTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");       
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间        
		String time = formatter.format(curDate);
		Log.e("获取系统时间格式", time);
		return time;
	}
}
