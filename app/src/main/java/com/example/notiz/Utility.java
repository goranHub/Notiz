package com.example.notiz;


import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Utility {


	public static final String TAG = Utility.class.getSimpleName();

	public static final int JSON_PARSING_METHOD = 0;
	public static final int XML_PARSING_METHOD = 1;
	public static final String FILENAME_QUOTE_DATA = "Zitatdaten.txt";

	private static final String URL = "https://www.codeyourapp.de/tools/query.php?";

	public static String requestQuotesFromServer(int quotesCount, int parsingMethod) {

		//Zusammenbauen der Anfrage-URL
		String requestUrl = URL + "count=" + quotesCount + "&mode=" + parsingMethod;
		Log.i(TAG, requestUrl);

		String quotesString = null;
		HttpURLConnection httpURLConnection = null;
		try {
			//aufbau der Verbindung zum Webserver - Timeout nach 9000ms
			java.net.URL url = new URL(requestUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(9000);
			httpURLConnection.setReadTimeout(9000);

			//Anfordern der Daten und Umwandeln dieser in eine Zeichenkette
			InputStream stream = new BufferedInputStream(httpURLConnection.getInputStream());
			quotesString = convertStreamToString(stream);
		} catch (MalformedURLException e) {
			Log.e(TAG, "MalformUrlException: " + e.getMessage());
		} catch (ProtocolException e) {
			Log.e(TAG, "MalformUrlException: " + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "MalformUrlException: " + e.getMessage());
		} catch (Exception e) {
			Log.e(TAG, "MalformUrlException: " + e.getMessage());
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		return quotesString;
	}

	private static String convertStreamToString(InputStream stream) {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder stringBuilder = new StringBuilder();

		String line;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line).append('\n');
			}
		} catch (IOException e) {
			Log.e("MainActivity: ", "IOException: " + e.getMessage());
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				Log.e("MainActivity: ", "IOException: " + e.getMessage());
			}
		}
		return stringBuilder.toString();
	}

	public static List<Notiz> createQuotesFromJSONString(String jsonString) {
		List<Notiz> receiveQuoteList = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);

			//Anfordern des JSON-Array Knotens mit den Qutes-Objekten
			JSONArray quotes = jsonObject.getJSONArray("quotes");

			//Durchlaufen des Quotes-Arrays und Auslesen der Daten jedes Quote-Objekts
			for (int i= 0; i < quotes.length(); i++) {
				JSONObject quote = quotes.getJSONObject(i);
				String imageId = quote.getString("id");
				String quoteAuthor = quote.getString("author");
				String quoteText = quote.getString("text");
				receiveQuoteList.add(new Notiz(1, quoteText, quoteAuthor, false));
//				receiveQuoteList.add(new Quote(quoteText, quoteAuthor, imageId));
			}

		} catch (JSONException e) {
			Log.e (TAG, "JSONException: " + e.getMessage());
		}

		return receiveQuoteList;
	}

	public static List<Notiz> createQuotesFromXMLString(String xmlString) {

		List<Notiz> receiveQuoteList = new ArrayList<>();

		Document doc;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlString));
			doc = db.parse(is);

			//Anfordern aller Quote-Elemente als NodeList des XML-Documents
			NodeList allQuotesNodeList = doc.getElementsByTagName("quote");

			//Durchlaufen der NodeList und Auslesen der Daten jedes Quote-Elements
			for (int i = 0; i < allQuotesNodeList.getLength(); i++) {
				Node quoteNode = allQuotesNodeList.item(i);
				NodeList quoteChildNodes = quoteNode.getChildNodes();

				String imageId = quoteChildNodes.item(0).getTextContent(); //ID
				String quoteAutor = quoteChildNodes.item(1).getTextContent(); //Autor
				String quoteText = quoteChildNodes.item(2).getTextContent(); //Text

				receiveQuoteList.add(new Notiz(1, quoteText, quoteAutor, false));
			}
		} catch (ParserConfigurationException e) {
			Log.e(TAG, "ParserConfigurationException: " + e.getMessage());
		} catch (SAXException e) {
			Log.e (TAG, "SAXException: " + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "IOException: " + e.getMessage());
		}

		return receiveQuoteList;
	}

	public static String createJSONStringFromQuoteList(List<Notiz> quoteList) {
		JSONObject quoteData = new JSONObject();
		JSONArray quoteArray = new JSONArray();

		try {
			for (Notiz qouteObject:quoteList) {
				JSONObject quote = new JSONObject();
				quote.put("id", qouteObject.getId());
				quote.put("note1", qouteObject.getNote1());
				quote.put("note2", qouteObject.getNote2());

				quoteArray.put(quote);
			}
			quoteData.put("quotes", quoteArray);

		} catch (JSONException e) {
			Log.e(TAG, "JSONException: " + e.getMessage());
		}
		String jsonString = quoteData.toString();
		Log.i(TAG, "Aus QuoteList generierter JSON-String: " + jsonString);
		return jsonString;
	}

	public static void saveQuoteListInFile(Context context, List<Notiz> quoteList) {
		String jsonString = createJSONStringFromQuoteList(quoteList);
		FileOutputStream fileOutputStream = null;

		try {
			fileOutputStream = context.openFileOutput(FILENAME_QUOTE_DATA, Context.MODE_PRIVATE);
			fileOutputStream.write(jsonString.getBytes());
		}  catch (FileNotFoundException e) {
			Log.e(TAG, "FileNotFoundException: " + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "IOException: " + e.getMessage());
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					Log.e(TAG, "IOException: " +e.getMessage());
				}
			}
		}
	}

	public static List<Notiz> restoreQuoteListFromFile(Context context) {
		String jsonString = "";
		FileInputStream fileInputStream = null;

		try {
			fileInputStream = context.openFileInput(FILENAME_QUOTE_DATA);
			InputStream stream = new BufferedInputStream(fileInputStream);

			jsonString = convertStreamToString(stream);
			Log.i(TAG, "JSON-String aus Datei gelesen: " + jsonString);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "FileNotFoundException: " + e.getMessage());
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					Log.e(TAG, "IOException: " +e.getMessage());
				}
			}
		}
		return createQuotesFromJSONString(jsonString);
	}
}
