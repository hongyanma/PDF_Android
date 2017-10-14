package com.com.tool;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	
	private static Toast toast = null;  
	
	public static void show(Context context, String text){
		
		toast = Toast.makeText(context, text, 100);
		
		toast.show();
	}

}
