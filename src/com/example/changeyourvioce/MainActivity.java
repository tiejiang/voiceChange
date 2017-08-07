/**
 * welcome界面，设置每次进入程序都会出现不同的图案效果
 * */
package com.example.changeyourvioce;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.Window;
import java.util.Random;

import com.cxshow.initCXAD;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;

public class MainActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(new LoginSurfaceView(this));
		//调用插屏
		initCXAD.showAdCode(this);
	}

	public class LoginSurfaceView extends SurfaceView implements Runnable, Callback {
		private SurfaceHolder holder;
		private Canvas canvas;
		private Paint paint;
		private Thread mDrawThread;
	    private  Random rand;
		
		public LoginSurfaceView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			holder = this.getHolder();
			holder.addCallback(this);
			paint = new Paint();
			setFocusable(true);
			rand = new Random();
			paint.setAntiAlias(true);
		}

		@Override
		public void run() {
//			while (threadState) {
				Log.e("thread", "thread run");
				// TODO Auto-generated method stub
				 //clear the buffer with color
				try {
					canvas = holder.lockCanvas();
					if (canvas != null) {
						canvas.drawColor(Color.BLACK);
						//draw random circles
						for (int n=0; n<10; n++) {
							Log.e("thread", "loop run");
							//make a random color
							int r = rand.nextInt(256);
							int g = rand.nextInt(256);
							int b = rand.nextInt(256);
//							paint.setColor(Color.rgb(r, g, b));
							paint.setColor(Color.rgb(47, 79, 79));;
							Log.e("thread", "loop run ~~~");
							//make a random position and radius
							int x = rand.nextInt(canvas.getWidth());
							int y = rand.nextInt(canvas.getHeight());
							int radius = rand.nextInt(100) + 20;
							System.out.println("x= " + x + "\n" + "y= " + y);
					    
							//draw one circle
							canvas.drawCircle( x, y, radius, paint);
						}
			    	}
				} catch (Exception e) {
//					 TODO: handle exception
				}finally{
					if (canvas != null) {
						holder.unlockCanvasAndPost(canvas);
					}
				}
//			}
			
		}
		public boolean onTouchEvent(MotionEvent event){
			if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
				Log.e("onTouchHappen", "DOWN AND MOVE");
			}else if (event.getAction() == MotionEvent.ACTION_UP) {
				Log.e("onTouchHappen", "UP");
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), VoiceChangeActivity.class);
				startActivity(intent);
				finish();
			}
			return true;
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			mDrawThread = new Thread(this);
			mDrawThread.start();
			
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
		}

	}

}
