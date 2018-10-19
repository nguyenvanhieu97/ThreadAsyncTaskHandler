package com.cris.nvh.threadasynctaskhandler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
	public static final int SPAN_COUNT = 2;
	private ArrayList<String> mPaths;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		RecyclerView recyclerView;
		RecyclerView.Adapter adapter;
		RecyclerView.LayoutManager layoutManager;
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		layoutManager = new GridLayoutManager(this, SPAN_COUNT);
		recyclerView.setLayoutManager(layoutManager);
		mPaths = (ArrayList<String>) getIntent()
				.getSerializableExtra(SplashActivity.PATH);
		adapter = new Adapter(mPaths);
		recyclerView.setAdapter(adapter);
	}
}
