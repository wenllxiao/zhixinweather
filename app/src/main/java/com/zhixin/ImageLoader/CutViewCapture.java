package com.zhixin.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import com.zhixin.utils.L;



/**
 * @author Administrator
 * @desc 截图保存
 * 
 */
public class CutViewCapture {
	private static String cachedDir = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/share_qq";
	private static String TAG = "CutViewCapture";

	/**
	 * 从内存中获取View并保存
	 * 
	 * @param view
	 */
	public static void saveBitmapInFile(View view){
		// 2.缓存bitmap至/data/data/packageName/cache/文件夹中
		File dirFile = new File(cachedDir);
		try {
			if (!dirFile.exists()) {
				dirFile.mkdir();
			}
			File myCaptureFile = new File(cachedDir + "weather.png");
			if (myCaptureFile.exists()) {
				myCaptureFile.deleteOnExit();
			}
			FileOutputStream bos = new FileOutputStream(myCaptureFile);
			Bitmap bitmap = null;
			view.setDrawingCacheEnabled(true);
			bitmap = view.getDrawingCache();
			bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
			bos.flush();
			bos.close();
			bitmap.recycle();
			bitmap=null;
		} catch (Exception e) {
			L.e(TAG, "msg:"+e.toString());
			e.printStackTrace();
		}
		view.setDrawingCacheEnabled(false);
	}

	/**
	 * 从外部文件缓存中获取bitmap
	 * @return
	 */
	public static String getBitmapFromFile() {
		return cachedDir + "weather.png";
	}
}