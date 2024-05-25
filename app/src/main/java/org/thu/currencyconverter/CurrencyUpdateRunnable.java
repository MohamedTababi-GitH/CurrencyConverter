package org.thu.currencyconverter;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.ShareActionProvider;
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

    // Instance of ExchangeRateDatabase
    ExchangeRateDatabase obj = new ExchangeRateDatabase();
    String[] currencyList = obj.getCurrencies();
    CurrencyListAdapter adapter2;
    // Spinners
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
        Log.d("thread", "Started");
        if (isRefreshing) {
            Log.d("CurrencyUpdateRunnable", "Refresh already in progress. Ignoring new request.");
            return; // Exit without starting a new refresh
        }
        isRefreshing = true;

        updateCurrencies();

        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                inputspinner.setAdapter(adapter2);
                outputspinner.setAdapter(adapter2);;
                Toast toast = Toast.makeText(context, "Currencies Update Finished!", Toast.LENGTH_SHORT);
                toast.show();
            }
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
                    // Convert the currency to lowercase to match the JSON keys
                    String currencyKey = currency.toLowerCase();

                    // Check if the JSON object contains the key for the currency
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
                adapter2 = new CurrencyListAdapter(tempArray);


            } catch (IOException exception) {
                Log.e("Refresher", "Can't refresh from DB");
                exception.printStackTrace();
            } catch (JSONException exception) {
                Log.e("Parse JSON", "Error parsing JSON.");
                exception.printStackTrace();
            }

        }
    }


