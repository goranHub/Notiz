package com.example.notiz;

import android.os.AsyncTask;

import java.util.List;

/**
 * @author ll4
 * @date 1/28/2020
 */
class RequestQuoteTask extends AsyncTask<Integer, String, List<Notiz>> {
	public RequestQuoteTask(MainActivity mainActivity, MainActivityListener mainActivityListener) {
	}

	@Override
	protected List<Notiz> doInBackground(Integer... integers) {
		return null;
	}
}
