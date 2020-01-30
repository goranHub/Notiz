package com.example.notiz;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * @author ll4
 * @date 1/28/2020
 */
public class MyAdapters {

	MainActivity mainActivity;
	int i = 0;

	public MyAdapters(List<Notiz> emptyListForInitialization, ListView listViewNotiz, MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		initializeShoppingMemoListView(emptyListForInitialization, listViewNotiz);
	}


	public void initializeShoppingMemoListView(List<Notiz> data, final ListView listViewNotiz) {


		//Erstellen des ArrayAdapters für unsere ListView
		ArrayAdapter<Notiz> shoppingMemoArrayAdapter = new ArrayAdapter<Notiz>(mainActivity,
				android.R.layout.simple_list_item_multiple_choice,
				data) {

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
				note1.setText("FERTIG");

				TextView note2 = view.findViewById(R.id.editTextPrduct);
				note2.setText(note.getNote1());


				i++;
				if(i % 2 == 0) {
					//prekrizeno
					note1.setPaintFlags(note1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


				} else {
					// ne prekrizeno
					note1.setText("NOCH NICHT FERTIG");
					note1.setPaintFlags(note1.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
				}
			}
		});
	}

	public class ViewHolder {
		Button button;
		EditText note;
	}
}
