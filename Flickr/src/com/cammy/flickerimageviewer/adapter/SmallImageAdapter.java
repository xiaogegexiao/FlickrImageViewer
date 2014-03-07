package com.cammy.flickerimageviewer.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cammy.flickerimageviewer.FlickrImageViewerApplication;
import com.cammy.flickerimageviewer.R;
import com.cammy.flickerimageviewer.model.ImageResult;
/**
 * Small Image Viewer Adapter
 * @author Xiao
 *
 */
public class SmallImageAdapter extends BaseAdapter {
	/**
	 * the width/height ratio for image viewer item.
	 */
	public static float itemRatio =  1.314285714285714f;
	
	private Context context;
	private List<ImageResult> results;
	private ImageLoader loader;
	private int parentWidth;
	private int parentHeight;
	
	public SmallImageAdapter(Context context, List<ImageResult> results) {
		this.context = context;
		this.results = results;
		loader = ((FlickrImageViewerApplication) context
				.getApplicationContext()).getImageLoader();
		this.parentHeight = 0;
		this.parentWidth = 0;
	}
	
	/**
	 * update ui layout params
	 * @param parentWidth the width of the small image gallery
	 * @param parentHeight the height of the small image gallery
	 */
	public void setSize(int parentWidth, int parentHeight) {
		this.parentHeight = parentHeight;
		this.parentWidth = parentWidth;
	}

	public void setResult(List<ImageResult> results) {
		this.results = results;
	}

	@Override
	public int getCount() {
		if (results == null)
			return 0;
		return results.size();
	}

	@Override
	public Object getItem(int position) {
		if (results == null)
			return null;
		return results.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * return every small image view from img.xml
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.img, null,
					false);
			ViewHolder holder = new ViewHolder();
			holder.resultImage = (NetworkImageView) view
					.findViewById(R.id.iv_img);
			LayoutParams params = new Gallery.LayoutParams(
					(int) ((float) parentHeight * itemRatio), parentHeight);
			holder.resultImage.setLayoutParams(params);
			holder.resultImage.setBackgroundResource(R.drawable.small_bg);
			view.setTag(holder);
		}
		ImageResult selectedResult = (ImageResult) getItem(position);
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.resultImage.setImageResource(R.drawable.default_large);
		holder.resultImage.setImageUrl(
				selectedResult.getMedia().getMedia_url(), loader);
		
		return view;
	}

	static class ViewHolder {
		NetworkImageView resultImage;
	}
}
