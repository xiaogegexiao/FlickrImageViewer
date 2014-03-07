package com.cammy.flickerimageviewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cammy.flickerimageviewer.adapter.LargeImageAdapter;
import com.cammy.flickerimageviewer.adapter.SmallImageAdapter;
import com.cammy.flickerimageviewer.model.ImageResult;
import com.cammy.flickerimageviewer.model.SearchClass;
import com.google.gson.Gson;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {
	private static final String TAG = "FlickerImageView";
	private static final float[] height_percentage = { 0.8390804597701149f,
			0.1609195402298851f };

	private int screenWidth;
	private int screenHeight;
	private int actionBarHeight = 0;

	private Gallery gl_largeimgs;
	private Gallery gl_thumbimgs;

	private ProgressBar progressBar;

	private LargeImageAdapter imageAdapter;
	private SmallImageAdapter smallImageAdapter;
	private List<ImageResult> imageResults;
	private RequestQueue queue;
	private Gson gson;
	private JsonObjectRequest fetchRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		queue = ((FlickrImageViewerApplication) this.getApplicationContext())
				.getQueue();
		findViews();
		updateLayout();
		refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			refresh();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void findViews() {
		imageResults = new ArrayList<ImageResult>();

		gl_largeimgs = (Gallery) findViewById(R.id.gl_largeimgs);
		gl_thumbimgs = (Gallery) findViewById(R.id.gl_thumbimgs);

		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		imageAdapter = new LargeImageAdapter(this, imageResults);
		smallImageAdapter = new SmallImageAdapter(this, imageResults);

		gl_largeimgs.setAdapter(imageAdapter);
		gl_thumbimgs.setAdapter(smallImageAdapter);
		gl_largeimgs.setOnItemSelectedListener(largeImageOnSelectedListener);
		gl_thumbimgs.setOnItemSelectedListener(smallImageOnSelectedListener);
		
		ViewTreeObserver vto = gl_largeimgs.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(mGlobalLayoutListener);
	}
	
	private OnItemSelectedListener largeImageOnSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			gl_thumbimgs.setSelection(arg2);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	
	private OnItemSelectedListener smallImageOnSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			gl_largeimgs.setSelection(arg2);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	
	private OnGlobalLayoutListener mGlobalLayoutListener = new OnGlobalLayoutListener() {
		@Override
		public void onGlobalLayout() {
			if (actionBarHeight == 0) {
				updateLayout();
			}
		}
	};

	private void updateLayout() {
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindowManager().getDefaultDisplay().getHeight();

		screenWidth = getResources().getDisplayMetrics().widthPixels;
		screenHeight = getResources().getDisplayMetrics().heightPixels
				- getActionBar().getHeight();

		actionBarHeight = getActionBar().getHeight();

		LayoutParams params = gl_largeimgs.getLayoutParams();
		params.width = screenWidth;
		params.height = (int) ((float) screenHeight * height_percentage[0]);
		gl_largeimgs.setLayoutParams(params);
		imageAdapter.setSize(params.width, params.height);
		imageAdapter.notifyDataSetChanged();

		params = gl_thumbimgs.getLayoutParams();
		params.height = (int) ((float) screenHeight * height_percentage[1]);
		int itemWidth = (int) (SmallImageAdapter.itemRatio
				* (float) screenHeight * height_percentage[1]);
		params.width = (screenWidth - itemWidth) * 2 + itemWidth;
		((RelativeLayout.LayoutParams) params).leftMargin = -(screenWidth - itemWidth);
		gl_thumbimgs.setLayoutParams(params);
		smallImageAdapter.setSize(params.width, params.height);
		smallImageAdapter.notifyDataSetChanged();
	}

	private void refresh() {
		progressBar.setVisibility(View.VISIBLE);
		fetchRequest = new JsonObjectRequest(Request.Method.GET, getResources()
				.getString(R.string.image_fetch_url), null,
				new ResponseListener(), new ErrorListener());
		queue.add(fetchRequest);
	};

	private class ResponseListener implements Response.Listener<JSONObject> {
		@Override
		public void onResponse(JSONObject response) {
			progressBar.setVisibility(View.GONE);
			setUpResults(response);
		}
	}

	private class ErrorListener implements Response.ErrorListener {
		@Override
		public void onErrorResponse(VolleyError error) {
			progressBar.setVisibility(View.GONE);
			Toast.makeText(getApplicationContext(), "Error, Please Try Again",
					Toast.LENGTH_LONG).show();
		}
	}

	private void setUpResults(JSONObject response) {
		imageResults.clear();
		imageAdapter.notifyDataSetChanged();
		smallImageAdapter.notifyDataSetChanged();
		gson = new Gson();
		SearchClass searchClass = gson.fromJson(response.toString(),
				SearchClass.class);
		// make sure there is data in the response
		if (searchClass.getItems() != null) {
			ImageResult[] results = searchClass.getItems();
			List<ImageResult> tempList = Arrays.asList(results);
			imageResults.addAll(tempList);
			imageAdapter.notifyDataSetChanged();
			smallImageAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		updateLayout();
	}
}
