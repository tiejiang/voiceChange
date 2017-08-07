/**
 * �ļ��洢��·���Ȳ�����
 * */
package com.example.changeyourvioce;

import java.io.File;

import android.os.Environment;

public class BaseUtil {
	/**
	 * sd���ĸ�Ŀ¼
	 */
	private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
	/**
	 * �ֻ��Ļ����Ŀ¼
	 */
	private static String mDataRootPath = null;
	/**
	 * ����sound��Ŀ¼��
	 */
	private final static String FOLDER_NAME = "/soundChange/";
	/**
	 * ������������������ļ�
	 * */
	public static void ImportPackage(){
		System.loadLibrary("soundtouch");
		System.loadLibrary("soundstretch");
	}
	/**
	 * ��ȡ����Image��Ŀ¼
	 * @return
	 */
	private static String getStorageDirectory(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
				mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
	}
	/**
	 *�����ļ��в� ��ô洢·��
	 * */
	public static String getSavePath(){
		//�õ��ļ��洢λ�ã�����/���ô洢��
		String path = getStorageDirectory();
		//�����ļ����ļ���
		File folderFile = new File(path);
		if(!folderFile.exists()){
			folderFile.mkdirs();
		}
		return path;
	}
	
	
}
