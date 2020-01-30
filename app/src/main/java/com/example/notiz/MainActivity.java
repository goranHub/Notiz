package com.example.notiz;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {


	MainActivityListener mainActivityListener;

	public EditText txtNotizEintrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		Toolbar toolbar = findViewById(R.id.toolbar_main_activity);
//		setSupportActionBar(toolbar);
//		ActionBar actionBar = getSupportActionBar();
//		actionBar.setTitle(R.string.app_name);




		Button btnAdd = findViewById(R.id.btnAddNote);
		Button btnDelete = findViewById(R.id.btnDelete);
		txtNotizEintrag = findViewById(R.id.txtNotizEintrag);

		mainActivityListener = new MainActivityListener(this);

		btnAdd.setOnClickListener(mainActivityListener);
		btnDelete.setOnClickListener(mainActivityListener);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);
		mainActivityListener.onSaveInstanceState(outState, outPersistentState);
	}

	@Override
	protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mainActivityListener.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mainActivityListener.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i("eee", "dd" + mainActivityListener);
		return mainActivityListener.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		return mainActivityListener.onOptionsItemSelected(item);
	}

}
