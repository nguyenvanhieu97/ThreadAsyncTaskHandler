package com.cris.nvh.threadasynctaskhandler;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
	public static final String PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
	public static final int MESSAGE = 238;
	public static final int REQUEST_CODE = 1;
	public static final int INCREASE = 1;
	public static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;
	public static final String PERMISSION_NOT_GRANTED = "Permission is not granted";
	public static final String PATH = "FILE_PATH";

	private ArrayList<String> mPaths;
	private ImageView mImageView;
	private ProgressBar mProgressBar;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// check message in message queue
			if (msg.what == MESSAGE) {
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				intent.putExtra(PATH, (Serializable) mPaths);
				startActivity(intent);
				SplashActivity.this.finish();
			} else return;
		}
	};

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		mImageView = (ImageView) findViewById(R.id.youtube_image);
		mImageView.setImageResource(R.drawable.youtube);
		mProgressBar = (ProgressBar) findViewById(R.id.progress);
		requestPermission();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions,
	                                       int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
				// create a worker thread to load image from gallery
				new LoadImageAsyncTask(mHandler).execute(getApplicationContext());
			} else {
				Toast.makeText(this, PERMISSION_NOT_GRANTED, Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void requestPermission() {
		if (ContextCompat.checkSelfPermission(this, PERMISSION) != PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{PERMISSION}, REQUEST_CODE);
		} else {
			// create a worker thread to load image from gallery
			new LoadImageAsyncTask(mHandler).execute(getApplicationContext());
		}
	}

	public class LoadImageAsyncTask extends AsyncTask<Context, Integer, Void> {
		public Handler handler;

		public LoadImageAsyncTask(Handler handler) {
			this.handler = handler;
		}

		@Override
		protected Void doInBackground(Context... contexts) {
			mPaths = getAllImagePaths(contexts);
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			mProgressBar.setProgress(values[0]);
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			// send message to message queue of thread
			handler.sendEmptyMessage(MESSAGE);
		}

		public ArrayList<String> getAllImagePaths(Context[] context) {
			Cursor cursor;
			int column_index;
			String absolutePathOfImage;
			ArrayList<String> listOfAllImages = new ArrayList<>();

			//projection specifies which column is choose
			String[] projection = {MediaStore.MediaColumns.DATA};
			Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

			cursor = context[0]
					.getContentResolver()
					.query(uri, projection, null, null, null);
			if (cursor == null) {
				return listOfAllImages;
			}
			mProgressBar.setMax(cursor.getCount());
			cursor.moveToFirst();
			column_index = cursor.getColumnIndex(projection[0]);
			while (!cursor.isAfterLast()) {
				absolutePathOfImage = cursor.getString(column_index);
				listOfAllImages.add(absolutePathOfImage);
				publishProgress(cursor.getPosition() + INCREASE);
				cursor.moveToNext();
			}
			cursor.close();
			return listOfAllImages;
		}

	}
}
