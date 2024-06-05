package org.thu.currencyconverter;

import android.content.Context;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CurrencyUpdateRunnable implements Runnable{
    private boolean isRefreshing = false;
    private final OkHttpClient client = new OkHttpClient();
    String queryString = "https://www.floatrates.com/daily/eur.json";
    ExchangeRateDatabase obj = new ExchangeRateDatabase();
    String[] currencyList = obj.getCurrencies();
    CurrencyListAdapter adapter;
    Spinner inputspinner;
    Spinner outputspinner;
    ArrayList<CurrencyListEntry> tempList = new ArrayList<>();
    Context context;

    public CurrencyUpdateRunnable(Context context, Spinner inputspinner, Spinner outputspinner) {
        this.context = context;
        this.inputspinner = inputspinner;
        this.outputspinner = outputspinner;
    }
    @Override
    public void run() {
        // logging to check thread start and finish time
        Log.d("thread", "Started");
        // checking if an update is already in progress
        if (isRefreshing) {
            return; // exit without starting a new refresh
        }
        isRefreshing = true;
        updateCurrencies();

        ((MainActivity) context).runOnUiThread(() -> {
            inputspinner.setAdapter(adapter);
            outputspinner.setAdapter(adapter);
            Toast toast = Toast.makeText(context, "Currencies Update Finished!", Toast.LENGTH_SHORT);
            toast.show();
        });
        isRefreshing = false;
        Log.d("thread", "finished!");
    }

        public void updateCurrencies() {
            try {
                Request request = new Request.Builder().url(queryString).build();
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONObject root = new JSONObject(responseBody);

                for (String currency : currencyList) {
                    // Converting the currency to lowercase to match the JSON keys
                    String currencyKey = currency.toLowerCase();

                    // Checking if the JSON object contains the key for the currency
                    if (root.has(currencyKey)) {
                        JSONObject searchResults = root.getJSONObject(currencyKey);
                        double searchEntry = searchResults.getDouble("rate");
                        obj.setExchangeRate(currency, searchEntry);
                    } else {
                        System.out.println("Currency " + currency + " not found in JSON.");
                    }
                }
                tempList = new ArrayList<>();
                // Filling the arrays and setting adapter
                for (String currency : currencyList) {
                    tempList.add(new CurrencyListEntry(currency, obj.getExchangeRate(currency)));
                }
                CurrencyListEntry[] tempArray = new CurrencyListEntry[tempList.size()];
                tempList.toArray(tempArray);
                adapter = new CurrencyListAdapter(tempArray);

            } catch (IOException exception) {
                Log.e("Refresher", "Can't refresh from DB");
                exception.printStackTrace();
            } catch (JSONException exception) {
                Log.e("Parse JSON", "Error parsing JSON.");
                exception.printStackTrace();
            }
        }
}


