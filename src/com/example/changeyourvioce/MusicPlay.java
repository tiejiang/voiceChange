/**
 * 音频播放的类
 * */
package com.example.changeyourvioce;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class MusicPlay {
	private final static int MAX_THREAD_SLEEP_TIME = 2000;//延迟播放的时间
	private static MediaPlayer mediaplayer;
	public static void playmusic(final String filepath){
		Thread mMusicPlayThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mediaplayer = new MediaPlayer();
				try {
					Thread.sleep(MAX_THREAD_SLEEP_TIME);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					mediaplayer.setDataSource(filepath);
					mediaplayer.prepare();
					mediaplayer.start();
		         } catch (IllegalArgumentException e) {
		            e.printStackTrace();
		         } catch (IllegalStateException e) {
		            e.printStackTrace();
		         } catch (IOException e) {
		            e.printStackTrace();
		         }
				mediaplayer.setOnCompletionListener(new OnCompletionListener(){
		            @Override
		            public void onCompletion(MediaPlayer mp) {
		            	mediaplayer.release();
		            }
		         });
			}
		});
		mMusicPlayThread.start();
	}
	
}

