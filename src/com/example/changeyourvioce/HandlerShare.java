/**
 * ����handler���ݵ���
 * */
package com.example.changeyourvioce;


import android.os.Handler;

public class HandlerShare {
	public static Handler handler = null;
	//get handler
	public Handler getHandler() {
		return handler;
		
	}
	//set handler
	public void setHandler(Handler handler) {
		HandlerShare.handler = handler;
	}
			
}
