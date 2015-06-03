package com.example.richtextview;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.richtextview.ImageManager.ImgLoadCallback;
import com.nostra13.universalimageloader.core.assist.FailReason;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.Html;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class RichTextView extends TextView {

	private String path = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/richtextview/";

	private String mRichText;
	
	public RichTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public RichTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public RichTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param bitmap
	 * @param fileName
	 * @throws IOException
	 */
	private void save2File(Bitmap bitmap, String fileName) throws IOException {
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(path + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

	/**
	 * 
	 */
	final private Html.ImageGetter httpImgGetter = new Html.ImageGetter() {

		@Override
		public Drawable getDrawable(String source) {
			// TODO Auto-generated method stub
			Drawable drawable = null;
			// 判断SD卡里面是否存在图片文件
			if (new File(source).exists()) {
				// 获取本地文件返回Drawable
				drawable = Drawable.createFromPath(source);
				// 设置图片边界
				//获取控件的左右padding
				int paddingLeft = getPaddingLeft();
				int paddingRight = getPaddingRight();
				int drawableWidth = drawable.getIntrinsicWidth();
				int drawableHeight = drawable.getIntrinsicHeight();
				DisplayMetrics dm = getResources().getDisplayMetrics();
				int showWidth = dm.widthPixels -(paddingLeft+paddingRight);
				int showHeight = (int) (((float)showWidth/(float)drawableWidth) * drawableHeight);
				drawable.setBounds(0, 0,showWidth ,showHeight);
				return drawable;
			} else {
				// 启动新线程下载
				String filePath = path + String.valueOf(source.hashCode());
				if (new File(filePath).exists()) {
					// 获取本地文件返回Drawable
					drawable = Drawable.createFromPath(filePath);
					//获取控件的左右padding
					int paddingLeft = getPaddingLeft();
					int paddingRight = getPaddingRight();
					int drawableWidth = drawable.getIntrinsicWidth();
					int drawableHeight = drawable.getIntrinsicHeight();
					DisplayMetrics dm = getResources().getDisplayMetrics();
					int showWidth = dm.widthPixels -(paddingLeft+paddingRight);
					int showHeight = (int) (((float)showWidth/(float)drawableWidth) * drawableHeight);
					drawable.setBounds(0, 0,showWidth ,showHeight);
					return drawable;
				} else {
					ImageManager manager = ImageManager.getInstance();
					manager.setmImgLoadCallback(new ImgLoadCallback() {

						@Override
						public void OnError(FailReason failReason) {
							// TODO Auto-generated method stub
						}

						@Override
						public void OnComplete(String imageUri,
								Bitmap loadedImage) {
							// TODO Auto-generated method stub
							try {
								save2File(loadedImage,String.valueOf(imageUri.hashCode()));
								setText(Html.fromHtml(mRichText, httpImgGetter, null));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					manager.loadImg(source);
				}
				return drawable;
			}
		}
	};
	
	public void setRichText(String text){
		this.mRichText = text;
		this.mRichText = mRichText.replaceAll("[\\n\\r]", "<br>");
		setText(Html.fromHtml(mRichText, httpImgGetter, null));
	}

}
