/**
 * �������������á���װ
 * 
 * */
package com.example.changeyourvioce;

import java.util.ArrayList;
import java.util.HashMap;

public class ChangeVoiceParamPrepare {
	//����
	public final static float[] tempoDelta = new float[]{
		4f, 1f, 1f, 1f, 1f, 1f, 24f, 4f, 4f,
		4f, 14f, 24f, 34f, 10f, 24f, 34f, 24f, 24f} ;
	//Ƶ�ʣ����ߣ�
	public final static float[] pitchDelta = new float[] {
		-8f, -7f, 12f, 11f, 13f, -5f, -4f, 5f, 10f,
		-8f, -6f, -10f, -10f, -11f, -9f, -11f, -12f, -12f
	};
	//����
	public final static float[] rateDelta = new float[]{
		1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 
		-10f, -8f, 1f, 12f, 1f, 19f, 1f, 1f, 1f
	};
	//��װ�������
	public ArrayList<HashMap<String, Object>> setParamToArray(){
		ArrayList<HashMap<String, Object>> mArrayList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < tempoDelta.length; i++) {
			HashMap<String, Object> mMap = new HashMap<String, Object>();
			mMap.put("tempoDelta", tempoDelta[i]);
			mMap.put("pitchDelta", pitchDelta[i]);
			mMap.put("rateDelta", rateDelta[i]);
			mArrayList.add(mMap);
		}
		return mArrayList;
	}
}
