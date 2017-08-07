/**
 * ��assets���м���ͼ����Դ
 * �����Ż������е�gridview ʹ��baseadapter��ʱ��ʹ�����ַ�ʽ����ͼƬ��Դ
 * */
package com.example.changeyouvioce.util;
/**��ʱδʹ��**/
import java.io.IOException;
import java.io.InputStream;

import com.example.changeyourvioce.VoiceChangeActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class GridViewResource {
	private Context context;
	private final static int MAX_RESOURCE_LENGTH = 18;
	public Bitmap[] mBitmapArray = null;
	
	public GridViewResource(Context context){
		this.context = context;
	}
	//��ͼ����Դװ�ص�bitmap����
	public Bitmap[] fillBitmapArray(){
		String Name = "gridtn"; 
		mBitmapArray = new Bitmap[MAX_RESOURCE_LENGTH];
		for (int i = 0; i < MAX_RESOURCE_LENGTH; i++) {
			int nameNum = i;
			String fileName = Name + String.valueOf(nameNum);
			Log.e("fileName", fileName);
			mBitmapArray[i] = loadFromAsset(context, fileName);
		}
		return mBitmapArray;
	}
	//��assets����ͼ����Դ
	private Bitmap loadFromAsset(Context context,String fileName){
		Bitmap mBitmap = null;
		Log.e("loadFromAsset", "execute");

		AssetManager am = context.getResources().getAssets();
		Log.e("loadFromAsset", "execute ??");

		try {
			InputStream is = am.open(fileName);
			mBitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mBitmap;
	}
	
}
