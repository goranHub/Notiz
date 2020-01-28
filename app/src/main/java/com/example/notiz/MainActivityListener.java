package com.example.notiz;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivityListener implements View.OnClickListener, AdapterView.OnItemLongClickListener {

	private static final String LISTVIEW_DATA = "Notizen";
	private List<Notiz> noteList = new ArrayList<>();
	private static final String LOG_TAG = MainActivityListener.class.getSimpleName();
	private MainActivity mainActivity;
	private NotizDataSource dataSource;
	private ListView listViewNotiz;
	private ListAdapter quoteArrayAdapter;
	private List<Notiz> emptyListForInitialization = new ArrayList<>();

	public MainActivityListener(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		dataSource = new NotizDataSource(mainActivity);

		listViewNotiz = mainActivity.findViewById(R.id.lvAllNote);
		MyAdapters myAdapters = new MyAdapters(emptyListForInitialization, listViewNotiz, mainActivity);

		Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
		dataSource.open();

		Log.d(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden.");
		showAllEntries();

		Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
//		dataSource.close();
	}

	@Override
	public void onClick(View v) {
		String note1 = mainActivity.txtNotizEintrag.getText().toString();

		switch(v.getId()){
			case R.id.btnDelete:{
				for(int i =0; i< dataSource.getAllShoppingMemos().size(); i++){
					dataSource.deleteShoppingMemo(dataSource.getAllShoppingMemos().get(i));
				}
				showAllEntries();
				break;
			}
			case R.id.btnAddNote:{

				if(TextUtils.isEmpty(note1)) {
					mainActivity.txtNotizEintrag.setError("darf nicht leer sein");
				}
				dataSource.createNotizMemo(note1,"xas");
				showAllEntries();
				break;
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

		String author = noteList.get(position).getNote1();

		AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
		builder.setTitle(author);
		builder.setMessage( noteList.get(position).getNote1());
		builder.setPositiveButton("Schließen", null);

		AlertDialog dialog = builder.create();
		dialog.show();
		return true;
	}



	private void showAllEntries() {
		List<Notiz> notizList = dataSource.getAllShoppingMemos();
		ArrayAdapter<Notiz> shoppingMemoArrayAdapter = (ArrayAdapter<Notiz>) listViewNotiz.getAdapter();
		shoppingMemoArrayAdapter.clear();
		shoppingMemoArrayAdapter.addAll(notizList);
		shoppingMemoArrayAdapter.notifyDataSetChanged();
	}


	private void showAllEntriesData() {
		emptyListForInitialization.clear();
		NotizMaker notizMaker = new NotizMaker();
		List<Notiz> notizList = notizMaker.maker();
		ArrayAdapter<Notiz> shoppingMemoArrayAdapter = (ArrayAdapter<Notiz>) listViewNotiz.getAdapter();
		shoppingMemoArrayAdapter.clear();
		shoppingMemoArrayAdapter.addAll(notizList);
		shoppingMemoArrayAdapter.notifyDataSetChanged();
	}



	void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		if (noteList.size() > 0) {
			String jsonString = Utility.createJSONStringFromQuoteList(noteList);
			outState.putString(LISTVIEW_DATA, jsonString);
		} else {
			Log.v("SecondActivityListener:", "--> Zitateliste ist leer. Zustand wurden nicht gespeichert");
		}
	}

	void onRestoreInstanceState(Bundle savedInstanceState) {
		String jsonString = savedInstanceState.getString(LISTVIEW_DATA);

		if (jsonString != null) {
			List<Notiz> restoreQuoteList = Utility.createQuotesFromJSONString(jsonString);
			noteList.clear();
			noteList.addAll(restoreQuoteList);
			quoteArrayAdapter = listViewNotiz.getAdapter();
			quoteArrayAdapter.notify();

		} else {
			Log.v("SecondActivityListener:", "<-- Es sind keine Zitatdaten im Bundle-Objekt vorhanden.");
			Log.v("SecondActivityListener:", "<-- Der Zustand konnte nicht wiederhergestellt werden.");
		}
	}

	void onStop() {
		if (noteList.size() > 0) {
			Utility.saveQuoteListInFile(mainActivity, noteList);
			Log.v("SecondActivityListener:", "--> Zitatdaten in Datei gespeichert");
		} else {
			Log.v("SecondActivityListener:", "--> Zitateliste leer. Es wurden keine Daten gespeichert.");
		}
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		mainActivity.getMenuInflater().inflate(R.menu.menu_activity_main, menu);
		return true;
	}


	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
			case R.id.action_get_data:{
				showAllEntriesData();
			}

				break;
			case R.id.action_settings:
				Intent intentSettings = new Intent(mainActivity, SettingsActivity.class);
				mainActivity.startActivity(intentSettings);
				break;
		}
		return true;
	}

}






