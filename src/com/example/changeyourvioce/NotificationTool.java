/**
 * 接受广播、执行相应的事件：播放、录音、change、停止
 * 并把相应的状态通过handler传递回去
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
	public final static String ACTION_BUTTON_PLAY = "com.notifications.intent.action.ButtonPlay";//play的ACTION
	public final static String ACTION_BUTTON_RECORD = "com.notifications.intent.action.ButtonRecord";//record的ACTION
	public final static String ACTION_BUTTON_CHANGE = "com.notifications.intent.action.ButtonChange";//record的ACTION
	public final static String ACTION_BUTTON_STOP = "com.notifications.intent.action.ButtonStop";//stopRecord的ACTION
	public final static int PLAY_ID = 0;
	public final static int RECORD_ID = 1;
	public final static int STOP_ID = 2;
	public final static int CHANGE_ID = 3;
	public static boolean isRecord;
	private HandlerShare handlerShare = null;
	private Handler mHandler = null;
	/**
	 * mExtAudioRecorder必须设为静态 全局的 要不录制时候的mExtAudioRecorder
	 * 值不能保存到录制停止事件的发生，即必须保证第二次接收notification时候mExtAudioRecorder的值还在
	 * */
	private static ExtAudioRecorder mExtAudioRecorder = null; 
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		if (intent.getAction().equals(ACTION_BUTTON_PLAY)) {
			//执行play method
			MusicPlay.playmusic(BaseUtil.getSavePath() + "change.wav");
			//发送playmessage
			sendHandlerMessage(PLAY_ID);
			Log.e("notification", "play method execute");
		}else if (intent.getAction().equals(ACTION_BUTTON_RECORD)) {
			//执行record method
			new Thread(){
				public void run(){
					if (mExtAudioRecorder == null){
						mExtAudioRecorder = ExtAudioRecorder.getInstanse(false);
						Log.e("初始化soundtouch", "初始化成功！");
					}else {
						Log.e("初始化soundtouch", "初始化失败！");
					}
					RecordPrepare.prepare(mExtAudioRecorder, BaseUtil.getSavePath());
					//发送recordmessage
					sendHandlerMessage(RECORD_ID);
					Log.e("notification", "record method execute");	
				}
			}.start();
			
		}else if (intent.getAction().equals(ACTION_BUTTON_STOP)) {
			/**
			 * compareto()
			 * enum值相同返回0
			 * 否则返回-1
			 * */
			try {
				if (State.RECORDING.compareTo(mExtAudioRecorder.getState()) == 0) {
					//执行stop method
					Log.e("notification", "stopRecord method execute");
					mExtAudioRecorder = RecordPrepare.RecordStop(mExtAudioRecorder);
					//发送stopmessage
					sendHandlerMessage(STOP_ID);
				}
			} catch (Exception e) {
				// TODO: handle exception 没有录制，直接按 停止录制 按钮 会出现异常
				Log.e("error state", "prepare() method called on illegal state");
			}

			System.out.println("mExtAudioRecorder = " + mExtAudioRecorder);
		}else if (intent.getAction().equals(ACTION_BUTTON_CHANGE)) {
			//执行change method
			RecordPrepare.changeVoice(BaseUtil.getSavePath() + "source.wav", BaseUtil.getSavePath() + "change.wav",
					VoiceChangeActivity.tempoDelta, VoiceChangeActivity.pitchDelta, VoiceChangeActivity.rateDelta);
			//发送change message
			sendHandlerMessage(CHANGE_ID);
			Log.e("notification", "change voice method execute");
		}
	}
	public void sendHandlerMessage(int what){
		//获得共享的handler变量
		handlerShare = new HandlerShare();
		mHandler = handlerShare.getHandler();
		//发送message
		mHandler.sendEmptyMessage(what);
	}
}
