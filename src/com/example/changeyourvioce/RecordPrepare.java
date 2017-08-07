/**
 * Ϊ¼����׼�����Լ�¼��ֹͣ
 * */
package com.example.changeyourvioce;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.tecunhuman.AndroidJNI;
import org.tecunhuman.ExtAudioRecorder;

import android.util.Log;

public class RecordPrepare {
	
	//¼����ʼ��׼��
	public static void prepare(ExtAudioRecorder extAudioRecorder, String dataPath){
		extAudioRecorder.setOutputFile(dataPath + "source.wav");
		extAudioRecorder.prepare();
		extAudioRecorder.start();
	}
	//¼��ֹͣ
	public static ExtAudioRecorder RecordStop(ExtAudioRecorder extAudioRecorder){
		// TODO Auto-generated method stub
//		extAudioRecorder.reset();
		extAudioRecorder.stop();
		extAudioRecorder.release();
		return extAudioRecorder = null;//���뽫ExtAudioRecorder�ָ�Ϊnull״̬��Ϊ�´�¼������׼����
	}
	/**
	 * @param �������ļ����ѱ����ļ�����Ƶ���ģ���ƵƵ�ʣ���Ƶ�ٶ�
	 * @return
	 * */
	public static void changeVoice(String inFilename, String outFilename, float tempoDelta, float pitchDelta, float rateDelta){
		AndroidJNI.soundStretch.process(inFilename, outFilename, tempoDelta, pitchDelta, rateDelta);
	}
	//��ֹ¼���ļ�����������ϵͳ��ǰʱ��
	private static String getCurrentSystemTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");       
		Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��        
		String time = formatter.format(curDate);
		Log.e("��ȡϵͳʱ���ʽ", time);
		return time;
	}
}
