/**
 * 文件存储、路径等操作类
 * */
package com.example.changeyourvioce;

import java.io.File;

import android.os.Environment;

public class BaseUtil {
	/**
	 * sd卡的根目录
	 */
	private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
	/**
	 * 手机的缓存根目录
	 */
	private static String mDataRootPath = null;
	/**
	 * 保存sound的目录名
	 */
	private final static String FOLDER_NAME = "/soundChange/";
	/**
	 * 导入两个变声所需包文件
	 * */
	public static void ImportPackage(){
		System.loadLibrary("soundtouch");
		System.loadLibrary("soundstretch");
	}
	/**
	 * 获取储存Image的目录
	 * @return
	 */
	private static String getStorageDirectory(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
				mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
	}
	/**
	 *创建文件夹并 获得存储路径
	 * */
	public static String getSavePath(){
		//拿到文件存储位置：外置/内置存储卡
		String path = getStorageDirectory();
		//创建文件、文件夹
		File folderFile = new File(path);
		if(!folderFile.exists()){
			folderFile.mkdirs();
		}
		return path;
	}
	
	
}
