package com.example.notiz;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivityListener implements View.OnClickListener {


	public static final String LOG_TAG = MainActivityListener.class.getSimpleName();
	MainActivity mainActivity;
	List<Notiz> notizList = new ArrayList<>();
	private NotizDataSource dataSource;

	public MainActivityListener(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		dataSource = new NotizDataSource(mainActivity);
		ListView listViewNotiz = mainActivity.findViewById(R.id.lvAllNote);
		initializeShoppingMemoListView(listViewNotiz);

		Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
		dataSource.open();

		Log.d(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden.");
		showAllEntries();

		Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
		dataSource.close();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnAddNote) {

			String note = mainActivity.txtNotizEintrag.getText().toString();
			Notiz noteObj = new Notiz(1, note,"",false);

			if(TextUtils.isEmpty(note)) {
				mainActivity.txtNotizEintrag.setError("darf nicht leer sein");
			}

			notizList.add(noteObj);
			showAllEntries();

		}
	}

	private void showAllEntries() {
		ListView listViewNotiz = mainActivity.findViewById(R.id.lvAllNote);
		ArrayAdapter<Notiz> shoppingMemoArrayAdapter = (ArrayAdapter<Notiz>) listViewNotiz.getAdapter();
		shoppingMemoArrayAdapter.clear();
		shoppingMemoArrayAdapter.addAll(notizList);
		shoppingMemoArrayAdapter.notifyDataSetChanged();
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

				if(note.setisChecked()) {
					note1.setPaintFlags(note1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
					note1.setTextColor(Color.rgb(175, 175, 175));
				} else {
					note1.setPaintFlags(note1.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
					note1.setTextColor(Color.DKGRAY);
				}

			}
		});


	}


	public class ViewHolder {
		Button button;
		EditText note;
	}


}






