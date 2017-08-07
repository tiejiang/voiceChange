/**
 * Ӧ�õ��������Լ�֪ͨ���Ĳ���
 * �ṩ���沼�ֲ�����¼������ѡ�񡢿ؼ�������
 * 
 * */
package com.example.changeyourvioce;

import java.util.ArrayList;
import java.util.HashMap;

import org.tecunhuman.ExtAudioRecorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import ca.uol.aig.fftpack.RealDoubleFFT;

import com.example.changeyouvioce.util.GridViewResource;
import com.example.changeyouvioce.util.PictureResource;

public class VoiceChangeActivity extends Activity implements OnClickListener {
	private Button  btnRecord, btnmore, btnChange, btnAdjust;
	private ImageButton btnPlay;
	private GridView gridView;
	private SimpleAdapter mSimpleAdapter;
	private ArrayList<HashMap<String, Object>> listItem;//gridview��list
	private HashMap<String, Object> map;//gridview��map
	private final static int GRIDVIEW_ITEM_NUM = 18;
	private final static int NOTIFICATION_ID = 2617;
	private static ExtAudioRecorder mExtAudioRecorder = null;
	private int flag = 0;//¼����ť״̬
	private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();//������������
	public static float tempoDelta = 0;
	public static float pitchDelta = 0;
	public static float rateDelta = 0;
	private NotificationManager mNotificationManager;
	private Notification mNotification;
	private RemoteViews mRemoteViews;
	public static boolean isRecord;
	public HandlerShare handlerShare;
	private GridViewResource mGridViewResource;
	private Bitmap[] mBitmap;
	//�ֶ����ڱ�������ģ��
	private TextView TextView01;
	private TextView TextView02, TextView03;
	
	/*record audiovisualization module*/
	private Button button; 
	private ImageView imageView; 
	private int frequency = 20000; 
	private int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO; 
	private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT; 
	private RealDoubleFFT transformer; 
	private int blockSize = 256; 
	private boolean started = false; 
	private Canvas canvas; 
	private Paint paint; 
	private Bitmap bitmap;
	
	
	public Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case NotificationTool.PLAY_ID:
				mRemoteViews = new RemoteViews(getPackageName(), R.layout.notifi_content);
				mRemoteViews.setImageViewResource(R.id.playbtn, R.drawable.notirecordingbtn);
				//����֪ͨ������ť״̬��
				showNotification();
//				mRemoteViews = new RemoteViews(getPackageName(), R.layout.notifi_content);
				mRemoteViews.setImageViewResource(R.id.playbtn, R.drawable.notiplaybtn);
				showNotification();
				break;
			case NotificationTool.RECORD_ID:
				Log.e("deal message", "handling ");
				mRemoteViews = new RemoteViews(getPackageName(), R.layout.notifi_content);
				mRemoteViews.setImageViewResource(R.id.recordbtn, R.drawable.notirecordingbtn);//�ı�recordͻ��--����
				//����֪ͨ������ť״̬��
				showNotification();
				break;
			case NotificationTool.STOP_ID:
				mRemoteViews = new RemoteViews(getPackageName(), R.layout.notifi_content);
				mRemoteViews.setImageViewResource(R.id.recordbtn, R.drawable.notirecordbtn);//�ı�recordͼ��--��ԭ
				/**
				 * 0--ImageView�ɼ�
				 * 4--ImageView���ɼ�
				 * 8--ImageView���ɼ��Ҳ�ռ�ò��ֿռ�
				 * */
//				mRemoteViews.setViewVisibility(R.id.stopbtn, 8);//�ı�stopͼ��--����
				//����֪ͨ������ť״̬��
				showNotification();
				break;
			case NotificationTool.CHANGE_ID:
				mRemoteViews = new RemoteViews(getPackageName(), R.layout.notifi_content);
				mRemoteViews.setViewVisibility(R.id.stopbtn, 0);//�ı�stopͼ��--��ʾ
				mRemoteViews.setImageViewResource(R.id.stopbtn, R.drawable.notistopbtn);//�ı�stopͼ��--��ԭ
				
				//����֪ͨ������ť״̬��
				showNotification();
				break;
			}
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//������Ӧ��
		BaseUtil.ImportPackage();
		//�ҵ����
		btnPlay = (ImageButton)findViewById(R.id.btnPlay);
		
		btnRecord = (Button)findViewById(R.id.btnRecord);
		btnChange = (Button)findViewById(R.id.btnChange);
		btnmore = (Button)findViewById(R.id.btnmore);
		btnAdjust = (Button)findViewById(R.id.btnAdjust);
		
		//Ϊ���ע�����¼�
		btnRecord.setOnClickListener(this);
		btnmore.setOnClickListener(this);
		btnAdjust.setOnClickListener(this);
		btnPlay.setOnTouchListener(new playTouchListener());
		btnChange.setOnClickListener(this);
		//���ϵͳNotification service
		mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		/**
		 *��assets����ȡͼƬ��ʱ��ʹ�� 
		 * */
//		mGridViewResource = new GridViewResource(this);
//		mBitmap = mGridViewResource.fillBitmapArray();
		
		gridView = (GridView)findViewById(R.id.gridView);
		listItem = new ArrayList<HashMap<String,Object>>();
		for (int i = 0; i < GRIDVIEW_ITEM_NUM; i++) {
			map = new HashMap<String, Object>();
			map.put("image", PictureResource.pictureArray[i]);
			listItem.add(map);
		}
		mSimpleAdapter = new SimpleAdapter(getApplicationContext(), listItem, 
				R.layout.activity_main_item, new String[]{"image"}, new int[]{R.id.itemImage});
		gridView.setAdapter(mSimpleAdapter);
		gridView.setOnItemClickListener(new ItemClickListener());
		
		//֪ͨ�����Զ��岼��
		mRemoteViews = new RemoteViews(getPackageName(), R.layout.notifi_content);
		//�õ�share��handler
		handlerShare = new HandlerShare();
		
	}
	
	//audionvisualization implentation
	private class RecordAudio extends AsyncTask<Void, double[], Void> { 
		@Override 
		protected Void doInBackground(Void... params) { 
			int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding); 
			AudioRecord audioRecord = new AudioRecord( 
			MediaRecorder.AudioSource.MIC, frequency, 
			channelConfiguration, audioEncoding, bufferSize); 
			short[] buffer = new short[blockSize]; 
			double[] toTransform = new double[blockSize]; 
			audioRecord.startRecording(); 
			while (started) { 
				//��record������ ����buffer�У���������Ϊ����write���ܻ�ȽϺ���Щ�� 
				int bufferResult = audioRecord.read(buffer, 0, blockSize); 
				for (int i = 0; i < bufferResult; i++) { 
					//"<i>" -------> "[i]"
					toTransform[i] = (double) buffer[i] / Short.MAX_VALUE; 
				} 
				transformer.ft(toTransform); 
				publishProgress(toTransform); 
			} 
			audioRecord.stop(); 
		return null; 
		} 
		@Override 
		protected void onProgressUpdate(double[]... values) { 
			super.onProgressUpdate(values); 
			canvas.drawColor(Color.BLACK); 
			for (int i = 0; i < values[0].length; i++) { 
				int x=i; 
				//"<i>" -------> "[i]"
				int downy=(int)(100-(values[0][i])*10); 
				int upy=100; 
				canvas.drawLine(x, downy, x, upy, paint); 
			} 
			imageView.invalidate(); 
		} 
	}
	//gridview�ĵ���¼������߼�
	class ItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			Log.e("position", String.valueOf(position));
			ChangeVoiceParamPrepare mChange = new ChangeVoiceParamPrepare();
			mList = mChange.setParamToArray();
			tempoDelta = (float) mList.get(position).get("tempoDelta");
			pitchDelta = (float) mList.get(position).get("pitchDelta");
			rateDelta = (float) mList.get(position).get("rateDelta");
			
//			System.out.println("tempoDelta:" + tempoDelta +"\n" + "pitchDelta:" + pitchDelta + "\n" + "rateDelta:" + rateDelta);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		case R.id.btnPlay:
//			//play����
//			btnPlay.setBackgroundResource(R.drawable.actiplayingbtn);
//			MusicPlay.playmusic(BaseUtil.getSavePath() + "change.wav");
//			btnPlay.setBackgroundResource(R.drawable.actiplaybtn);
//			break; 

		case R.id.btnRecord:
			//Record�����������¼��֮ǰ�ȼ�������䣡
			if (mExtAudioRecorder == null){
				mExtAudioRecorder = ExtAudioRecorder.getInstanse(false);
				Log.e("��ʼ��soundtouch", "��ʼ���ɹ���");
			}else {
				Log.e("��ʼ��soundtouch", "��ʼ��ʧ�ܣ�");

			}
			if (flag == 0) {
				btnRecord.setTextColor(Color.GRAY);
				btnRecord.setBackgroundResource(R.drawable.actirecordingbtn);
				//recording �� view
				LayoutInflater layoutInflater = LayoutInflater.from(this);
				final View actionView = layoutInflater.inflate(R.layout.action, null);
				showRoundActionBar(actionView);
				//�򿪿��ӻ���view��ʾ
				started=true; 
				new RecordAudio().execute(); 
				
				new Thread(){
					public void run(){
						RecordPrepare.prepare(mExtAudioRecorder, BaseUtil.getSavePath());
						Log.e("ִ��¼��", "¼���У�");
						flag = 1;
					}
				}.start();
			}
//			if (flag == 1) {
//				btnRecord.setTextColor(Color.RED);
//				btnRecord.setBackgroundResource(R.drawable.actirecordbtn);
//				btnRecord.setText("");
//				new Thread(){
//					public void run(){
//						mExtAudioRecorder = RecordPrepare.RecordStop(mExtAudioRecorder);
//						Log.e("ֹͣ¼��", "��ֹ");
////						System.out.println("mExtAudioRecorder = " + mExtAudioRecorder) ;
//						flag = 0;
//					}
//				}.start();
//			}
			break;
		case R.id.btnChange:
//			//change voice����
			RecordPrepare.changeVoice(BaseUtil.getSavePath() + "source.wav", BaseUtil.getSavePath() + "change.wav",
					tempoDelta, pitchDelta, rateDelta);
			Toast.makeText(this, "change success!", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnmore:
			//popup the help view
			Dialog dialog = new AlertDialog.Builder(this)
			.setTitle("����")
			.setIcon(R.drawable.help)
			.setMessage(R.string.helpmessage)
			.setPositiveButton("ȷ��", null)
			.create();
			dialog.show();
			break;
		case R.id.btnAdjust:
			//������������ ��view
			LayoutInflater layoutInflater = LayoutInflater.from(this);
			final View adjustView = layoutInflater.inflate(R.layout.paramchange, null);
			showAdjustView(adjustView);
			break;
		}
	}
	//���ŵĵ���¼�
	public class playTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			MusicPlay.playmusic(BaseUtil.getSavePath() + "change.wav");
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				btnPlay.setBackgroundResource(R.drawable.actiplayingbtn);
			}else if (event.getAction() == MotionEvent.ACTION_UP) {
				btnPlay.setBackgroundResource(R.drawable.actiplaybtn);
			}
			return false;
		}
	}
	//��¼���С� -- ���ӻ�Ƶ��
	private void showRoundActionBar(final View view){
		//init audiovisualization module
		imageView = (ImageView)view.findViewById(R.id.fft_imageView); 
		transformer = new RealDoubleFFT(blockSize); 
		bitmap = Bitmap.createBitmap(256, 100, Bitmap.Config.ARGB_8888); 
		canvas = new Canvas(bitmap); 
		paint = new Paint(); 
		
		paint.setColor(Color.GREEN); 
		imageView.setImageBitmap(bitmap);
		AlertDialog builder = new AlertDialog.Builder(this)
		.setCancelable(false)
		.setView(view)
		.setPositiveButton("ֹͣ¼��", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mExtAudioRecorder = RecordPrepare.RecordStop(mExtAudioRecorder);
				btnRecord.setBackgroundResource(R.drawable.actirecordbtn);
				flag = 0;
			}
			
		}).create();
		builder.show();
	}
	//���������ĵ���
	private void showAdjustView(View view){
		SeekBar tempoSeekBar, pitchSeekBar, rateSeekBar;
		
		tempoSeekBar = (SeekBar)view.findViewById(R.id.tempoSeekBar);
		pitchSeekBar = (SeekBar)view.findViewById(R.id.pitchSeekBar);
		rateSeekBar = (SeekBar)view.findViewById(R.id.rateSeekBar);
		
		TextView01 = (TextView)view.findViewById(R.id.TextView01);
		TextView02 = (TextView)view.findViewById(R.id.TextView02);
		TextView03 = (TextView)view.findViewById(R.id.TextView03);
		
		tempoSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				TextView01.setText(String.valueOf(progress));
				tempoDelta = (float)progress;
			}
		});
		pitchSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				TextView02.setText(String.valueOf(progress));
				pitchDelta = (float)progress;
			}
		});
		rateSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				rateDelta = (float)progress;
				TextView03.setText(String.valueOf(progress));
			}
		});
		
		AlertDialog adjustBuilder = new AlertDialog.Builder(this)
		.setCancelable(false)
		.setView(view)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				RecordPrepare.changeVoice(BaseUtil.getSavePath() + "source.wav", BaseUtil.getSavePath() + "change.wav",
						tempoDelta, pitchDelta, rateDelta);
				System.out.println("�Զ����������" + tempoDelta + pitchDelta + rateDelta);
			}
		})
		.setNegativeButton("ȡ��", null)
		.create();
		adjustBuilder.show();
	}
	//��֪ͨ����ʾ�Զ����view
	public void showNotification(){
		//֪ͨ��ͼ��
		mRemoteViews.setImageViewResource(R.id.logobtn, R.drawable.notilogo);
		//����汾�ŵ��ڣ�3��0������ô����ʾ��ť  
        if(BaseTools.getSystemVersion() <= 9){  
            mRemoteViews.setViewVisibility(R.id.but5t5onnot5i, View.GONE);  
        }else{  
            mRemoteViews.setViewVisibility(R.id.but5t5onnot5i, View.VISIBLE);  
        } 
		//���play��ť
		Intent intentPlay = new Intent(NotificationTool.ACTION_BUTTON_PLAY);
		PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(this, 1, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
		mRemoteViews.setOnClickPendingIntent(R.id.playbtn, pendingIntentPlay);
		
		//���ù���handler����
		handlerShare.setHandler(mHandler);
		Log.e("handler set", "set");
		//���record
		Intent intentRecord = new Intent(NotificationTool.ACTION_BUTTON_RECORD);
		PendingIntent pendingIntentRecord = PendingIntent.getBroadcast(this, 2, intentRecord, PendingIntent.FLAG_CANCEL_CURRENT);
		mRemoteViews.setOnClickPendingIntent(R.id.recordbtn, pendingIntentRecord);
		System.out.println("isRecord = " + NotificationTool.isRecord);
//		if (NotificationTool.isRecord) {
//			mRemoteViews.setImageViewResource(R.id.recordbtn, R.drawable.ic_launcher);
//		}
		Log.e("excute", "step--one");
		//���stop
		Intent intentStop = new Intent(NotificationTool.ACTION_BUTTON_STOP);
		PendingIntent pendingIntentStop = PendingIntent.getBroadcast(this, 4, intentStop, PendingIntent.FLAG_UPDATE_CURRENT);
		mRemoteViews.setOnClickPendingIntent(R.id.stopbtn, pendingIntentStop);
		Log.e("excute", "step--two");

		//���change
		Intent intentChange = new Intent(NotificationTool.ACTION_BUTTON_CHANGE);
		PendingIntent pendingIntentChange = PendingIntent.getBroadcast(this, 3, intentChange, PendingIntent.FLAG_UPDATE_CURRENT);
		mRemoteViews.setOnClickPendingIntent(R.id.changebtn, pendingIntentChange);
		
		Log.e("excute", "step--three");

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setContent(mRemoteViews)
				.setSmallIcon(R.drawable.ic_launcher) 
				.setTicker("take care!")
				.setContentIntent(PendingIntent.getActivity(VoiceChangeActivity.this, 0, new Intent().setClass(getApplicationContext(), VoiceChangeActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
				.setOngoing(false)
				.setAutoCancel(true);
		
		Log.e("excute", "step--four");	
		
		mNotification = builder.build();
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;
		//��֪ͨ����ʾ
		mNotificationManager.notify(NOTIFICATION_ID, mNotification);
		Log.e("excute", "step--five");
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//��ʾ֪ͨ��
		showNotification();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		started = false;
		super.onDestroy();
	}
	//��Ӳ˵�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(Menu.NONE, R.id.exit, 0, "�˳�");
		return super.onCreateOptionsMenu(menu);
	}
	//�˵�������˳����¼�
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Log.e("menu", "ִ�в˵��¼�");
		switch (item.getItemId()) {
		case R.id.exit:
			mNotificationManager.cancel(NOTIFICATION_ID);//ȡ��֪ͨ
			finish();//�˳�ϵͳ
			System.exit(0);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
