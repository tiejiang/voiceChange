/**
 * init changxiang AD
 * */
package com.cxshow;

import android.content.Context;

import com.cc88zsz.woaizszXManager;

public class initCXAD {
	private static final String XID = "c785b98e24254f3b8159a904016dd309";
	public static void showAdCode(Context ctx) {
		woaizszXManager.getInstance(ctx, XID, "chlId").show(-1, -1, null, 1);
	}
}
