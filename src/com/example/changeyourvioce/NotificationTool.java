/**
 * ���ܹ㲥��ִ����Ӧ���¼������š�¼����change��ֹͣ
 * ������Ӧ��״̬ͨ��handler���ݻ�ȥ
 * */
package com.example.changeyourvioce;

import org.tecunhuman.ExtAudioRecorder;
import org.tecunhuman.ExtAudioRecorder.State;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class NotificationTool extends BroadcastReceiver{
	public final static String ACTION_BUTTON_PLAY = "com.notifications.intent.action.ButtonPlay";//play��ACTION
	public final static String ACTION_BUTTON_RECORD = "com.notifications.intent.action.ButtonRecord";//record��ACTION
	public final static String ACTION_BUTTON_CHANGE = "com.notifications.intent.action.ButtonChange";//record��ACTION
	public final static String ACTION_BUTTON_STOP = "com.notifications.intent.action.ButtonStop";//stopRecord��ACTION
	public final static int PLAY_ID = 0;
	public final static int RECORD_ID = 1;
	public final static int STOP_ID = 2;
	public final static int CHANGE_ID = 3;
	public static boolean isRecord;
	private HandlerShare handlerShare = null;
	private Handler mHandler = null;
	/**
	 * mExtAudioRecorder������Ϊ��̬ ȫ�ֵ� Ҫ��¼��ʱ���mExtAudioRecorder
	 * ֵ���ܱ��浽¼��ֹͣ�¼��ķ����������뱣֤�ڶ��ν���notificationʱ��mExtAudioRecorder��ֵ����
	 * */
	private static ExtAudioRecorder mExtAudioRecorder = null; 
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		if (intent.getAction().equals(ACTION_BUTTON_PLAY)) {
			//ִ��play method
			MusicPlay.playmusic(BaseUtil.getSavePath() + "change.wav");
			//����playmessage
			sendHandlerMessage(PLAY_ID);
			Log.e("notification", "play method execute");
		}else if (intent.getAction().equals(ACTION_BUTTON_RECORD)) {
			//ִ��record method
			new Thread(){
				public void run(){
					if (mExtAudioRecorder == null){
						mExtAudioRecorder = ExtAudioRecorder.getInstanse(false);
						Log.e("��ʼ��soundtouch", "��ʼ���ɹ���");
					}else {
						Log.e("��ʼ��soundtouch", "��ʼ��ʧ�ܣ�");
					}
					RecordPrepare.prepare(mExtAudioRecorder, BaseUtil.getSavePath());
					//����recordmessage
					sendHandlerMessage(RECORD_ID);
					Log.e("notification", "record method execute");	
				}
			}.start();
			
		}else if (intent.getAction().equals(ACTION_BUTTON_STOP)) {
			/**
			 * compareto()
			 * enumֵ��ͬ����0
			 * ���򷵻�-1
			 * */
			try {
				if (State.RECORDING.compareTo(mExtAudioRecorder.getState()) == 0) {
					//ִ��stop method
					Log.e("notification", "stopRecord method execute");
					mExtAudioRecorder = RecordPrepare.RecordStop(mExtAudioRecorder);
					//����stopmessage
					sendHandlerMessage(STOP_ID);
				}
			} catch (Exception e) {
				// TODO: handle exception û��¼�ƣ�ֱ�Ӱ� ֹͣ¼�� ��ť ������쳣
				Log.e("error state", "prepare() method called on illegal state");
			}

			System.out.println("mExtAudioRecorder = " + mExtAudioRecorder);
		}else if (intent.getAction().equals(ACTION_BUTTON_CHANGE)) {
			//ִ��change method
			RecordPrepare.changeVoice(BaseUtil.getSavePath() + "source.wav", BaseUtil.getSavePath() + "change.wav",
					VoiceChangeActivity.tempoDelta, VoiceChangeActivity.pitchDelta, VoiceChangeActivity.rateDelta);
			//����change message
			sendHandlerMessage(CHANGE_ID);
			Log.e("notification", "change voice method execute");
		}
	}
	public void sendHandlerMessage(int what){
		//��ù����handler����
		handlerShare = new HandlerShare();
		mHandler = handlerShare.getHandler();
		//����message
		mHandler.sendEmptyMessage(what);
	}
}
