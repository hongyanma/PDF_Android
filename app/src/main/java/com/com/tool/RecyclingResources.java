package com.com.tool;

import android.graphics.Bitmap;

public class RecyclingResources {
	
	/**
     * ���ܣ�������Դ���ͷ��ڴ�
     * @param bitmap
     */
	public void recycleBitmap(Bitmap bitmap) {
		if(bitmap != null && bitmap.isRecycled()){
			bitmap.recycle();
			bitmap = null;
			System.gc();//����ϵͳ����
    	}
	}
}
