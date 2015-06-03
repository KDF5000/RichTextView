package com.example.richtextview;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImageManager {
	public interface ImgLoadCallback {
		public void OnComplete(String imageUri, Bitmap loadedImage);

		public void OnError(FailReason failReason);
	}

	private ImgLoadCallback mImgLoadCallback;
	private static ImageManager instance = null;

	private ImageManager() {
	}

	/**
	 * 获取一个实例
	 * 
	 * @return
	 */
	public static ImageManager getInstance() {
		if (instance == null) {
			instance = new ImageManager();
		}
		return instance;
	}

	public void setmImgLoadCallback(ImgLoadCallback mImgLoadCallback) {
		this.mImgLoadCallback = mImgLoadCallback;
	}

	/**
	 * 
	 * @param imageUri
	 */
	public void loadImg(String imageUri) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheOnDisk(true)
				.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true).build();

		ImageLoader.getInstance().loadImage(imageUri, null, options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// Do whatever you want with Bitmap
						mImgLoadCallback.OnComplete(imageUri, loadedImage);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub
						super.onLoadingFailed(imageUri, view, failReason);
						mImgLoadCallback.OnError(failReason);
					}
				});
	}

	public DisplayImageOptions getDisplayImageOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_img)
				.showImageForEmptyUri(R.drawable.default_img)
				.showImageOnFail(R.drawable.default_img).cacheOnDisk(true)
				.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true).build();
		return options;
	}
	
	public Bitmap loadImageSync(String filePath){
		return ImageLoader.getInstance().loadImageSync(filePath, getDisplayImageOptions());
	}
}
