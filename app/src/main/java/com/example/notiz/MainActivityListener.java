package com.example.notiz;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivityListener implements View.OnClickListener, AdapterView.OnItemLongClickListener {

	private static final String LISTVIEW_DATA = "Notizen";
	public List<Notiz> quoteList = new ArrayList<>();
	public static final String LOG_TAG = MainActivityListener.class.getSimpleName();
	MainActivity mainActivity;
	List<Notiz> notizList = new ArrayList<>();
	private NotizDataSource dataSource;
	ListView listViewNotiz;
	ListAdapter quoteArrayAdapter;

	public MainActivityListener(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		dataSource = new NotizDataSource(mainActivity);

//		quoteList = dataSource.getAllShoppingMemos();
		listViewNotiz = mainActivity.findViewById(R.id.lvAllNote);
		initializeShoppingMemoListView(listViewNotiz);

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
				refreshListView();
				showAllEntries();
				break;
			}
			case R.id.btnAddNote:{
				Notiz noteObj = new Notiz(1, note1,"",false);

				if(TextUtils.isEmpty(note1)) {
					mainActivity.txtNotizEintrag.setError("darf nicht leer sein");
				}
				dataSource.createNotizMemo(note1,"xas");

//				refreshListView();
				showAllEntries();
				break;
			}

		}
//		if(v.getId() == R.id.btnAddNote) {
//
//			Notiz noteObj = new Notiz(1, note1,"",false);
//
//			if(TextUtils.isEmpty(note1)) {
//				mainActivity.txtNotizEintrag.setError("darf nicht leer sein");
//			}
//			dataSource.createNotizMemo(note1,"xas");
//			showAllEntries();
//
//		}
//
//		if(v.getId() == R.id.btnDelete) {
//			for(int i =0; i< dataSource.getAllShoppingMemos().size(); i++){
//				dataSource.deleteShoppingMemo(dataSource.getAllShoppingMemos().get(i));
//			}
//			showAllEntries();
//		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

		String author = quoteList.get(position).getNote1();

		AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
		builder.setTitle(author);
		builder.setMessage( quoteList.get(position).getNote1());
		builder.setPositiveButton("Schließen", null);

		AlertDialog dialog = builder.create();
		dialog.show();
		return true;
	}



	private void showAllEntries() {
		List<Notiz> notizList = dataSource.getAllShoppingMemos();
		ListView listViewNotiz = mainActivity.findViewById(R.id.lvAllNote);
		ArrayAdapter<Notiz> shoppingMemoArrayAdapter = (ArrayAdapter<Notiz>) listViewNotiz.getAdapter();
		shoppingMemoArrayAdapter.clear();
		shoppingMemoArrayAdapter.addAll(notizList);
		shoppingMemoArrayAdapter.notifyDataSetChanged();
	}



	void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		if (quoteList.size() > 0) {
			String jsonString = Utility.createJSONStringFromQuoteList(quoteList);
			outState.putString(LISTVIEW_DATA, jsonString);
		} else {
			Log.v("SecondActivityListener:", "--> Zitateliste ist leer. Zustand wurden nicht gespeichert");
		}
	}

	void onRestoreInstanceState(Bundle savedInstanceState) {
		String jsonString = savedInstanceState.getString(LISTVIEW_DATA);

		if (jsonString != null) {
			List<Notiz> restoreQuoteList = Utility.createQuotesFromJSONString(jsonString);
			quoteList.clear();
			quoteList.addAll(restoreQuoteList);
			quoteArrayAdapter = listViewNotiz.getAdapter();
			quoteArrayAdapter.notify();
//			quoteArrayAdapter.notifyDataSetChanged();
		} else {
			Log.v("SecondActivityListener:", "<-- Es sind keine Zitatdaten im Bundle-Objekt vorhanden.");
			Log.v("SecondActivityListener:", "<-- Der Zustand konnte nicht wiederhergestellt werden.");
		}
	}

	void onStop() {
		if (quoteList.size() > 0) {
			Utility.saveQuoteListInFile(mainActivity, quoteList);
			Log.v("SecondActivityListener:", "--> Zitatdaten in Datei gespeichert");
		} else {
			Log.v("SecondActivityListener:", "--> Zitateliste leer. Es wurden keine Daten gespeichert.");
		}
	}



	private void initializeShoppingMemoListView(final ListView listViewNotiz) {
		List<Notiz> emptyListForInitialization = new ArrayList<>();

		//Erstellen des ArrayAdapters für unsere ListView
		ArrayAdapter<Notiz> shoppingMemoArrayAdapter = new ArrayAdapter<Notiz>(mainActivity,
				android.R.layout.simple_list_item_multiple_choice,
				emptyListForInitialization) {

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder mainViewholder = null;
				ViewHolder viewHolder = new ViewHolder();


				if(view == null) {
					view = LayoutInflater.from(getContext()).inflate(R.layout.item_dialog, parent, false);
					viewHolder.button = view.findViewById(R.id.button);
					viewHolder.note = view.findViewById(R.id.editTextPrduct);
					view.setTag(viewHolder);
				}

				Notiz note = (Notiz) listViewNotiz.getItemAtPosition(position);
				TextView note2 = view.findViewById(R.id.editTextPrduct);
				note2.setText(note.getNote1());


				mainViewholder = (ViewHolder) view.getTag();

				final ViewHolder finalMainViewholder = mainViewholder;
				mainViewholder.button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finalMainViewholder.note.setText("FERTIG");
					}
				});
				return view;
			}
		};

		listViewNotiz.setAdapter(shoppingMemoArrayAdapter);

		listViewNotiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Notiz note = (Notiz) listViewNotiz.getItemAtPosition(position);

				TextView note1 = view.findViewById(R.id.editTextQuantity);
				note1.setText(String.valueOf(note.isChecked()));

				TextView note2 = view.findViewById(R.id.editTextPrduct);
				note2.setText(note.getNote1());

				if(note.isChecked()) {
					note1.setPaintFlags(note1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
					note1.setTextColor(Color.rgb(175, 175, 175));
				} else {
					note1.setPaintFlags(note1.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
					note1.setTextColor(Color.DKGRAY);
				}
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		mainActivity.getMenuInflater().inflate(R.menu.menu_activity_main, menu);
		return true;
	}


	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
			case R.id.action_get_data:
				refreshListView();
				break;
			case R.id.action_settings:
				Intent intentSettings = new Intent(mainActivity, SettingsActivity.class);
				mainActivity.startActivity(intentSettings);
				break;
		}
		return true;
	}

	public class ViewHolder {
		Button button;
		EditText note;
	}

	public void refreshListView() {
		int quoteCount = 0;
		int parsingMethod = Utility.JSON_PARSING_METHOD;

		//Auslesen der ausgewählten Einstellung aus der SharedPreferences
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
		String prefXmlModeKey = "preference_xmlmode_key";
		boolean isXmlModeOn = sharedPreferences.getBoolean(prefXmlModeKey, false);
		if (isXmlModeOn){
			parsingMethod = Utility.XML_PARSING_METHOD;
		}


		String prefCountKey ="preference_quotecount_key";

		quoteCount = Integer.parseInt(sharedPreferences.getString(prefCountKey,"1"));

		//Instanzieren des TaskObjektes und Starten des Tasks
		//der dafür sorgt, dass die Zitate eingelesen werden
		RequestQuoteTask requestQuoteTask = new RequestQuoteTask(mainActivity, this);
		requestQuoteTask.execute(quoteCount, parsingMethod); //10zitate mode 0
	}

}






