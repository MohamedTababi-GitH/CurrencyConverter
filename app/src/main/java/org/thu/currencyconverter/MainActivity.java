package org.thu.currencyconverter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        // Get the currency list from the ExchangeRateDatabase
        ExchangeRateDatabase obj = new ExchangeRateDatabase();
        String[] currencyList = obj.getCurrencies();

        // Spinners
        Spinner inputspinner = (Spinner) findViewById(R.id.input_spinner);
        Spinner outputspinner = (Spinner) findViewById(R.id.output_spinner);

        // Button
        Button convertbtn = (Button)  findViewById(R.id.convert_btn);

        // Create an ArrayAdapter using the string array and a default spinner layout
      /*  ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                currencyList
        );*/


        /*ListView listView = (ListView)findViewById(R.id.currency_list_view);
        listView.setAdapter(adapter2);
*/
        TextView inputVal = (TextView) findViewById(R.id.input_val);
        TextView outputVal = (TextView) findViewById(R.id.output_val);

        ArrayList<CurrencyListEntry> tempList = new ArrayList<>();
        for (String currency : currencyList) {
            tempList.add(new CurrencyListEntry(currency, obj.getExchangeRate(currency)));
        }

        CurrencyListEntry[] tempArray = new CurrencyListEntry[tempList.size()];
        tempList.toArray(tempArray);

        CurrencyListAdapter adapter2 = new CurrencyListAdapter(tempArray);

        // Set adapters to spinners
        inputspinner.setAdapter(adapter2);
        outputspinner.setAdapter(adapter2);



        // Button onclick method convert
        convertbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {
                Object inputselectedItem = inputspinner.getSelectedItem();
                Object outputselectedItem = outputspinner.getSelectedItem();

                CurrencyListEntry currencyEntry = (CurrencyListEntry) inputselectedItem;
                CurrencyListEntry outputEntry = (CurrencyListEntry) outputselectedItem;

                String inputCurrencyName = currencyEntry.getName();
                String outputCurrencyName = outputEntry.getName();
                Log.d("hi",inputCurrencyName);
               if (inputVal != null && !inputVal.getText().toString().isEmpty()){
                    double value = Double.parseDouble(inputVal.getText().toString());
                    double result = obj.convert(value, inputCurrencyName,outputCurrencyName);
                    outputVal.setText(String.format("%.2f", result));
                }
            }
        }
        );

        // Specify the layout to use when the list of choices appears.0
        //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_menu_entry:
                Log.i("AppBarExample", "Yes, you clicked!");
                return true;;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}