package com.example.telegrambot2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button button, button2, button3;
    EditText method;
    ListView response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        method = findViewById(R.id.method);
        response = findViewById(R.id.response);
        final TelegramApiService telegramApiService = new TelegramApiService(MainActivity.this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                telegramApiService.getCityId(method.getText().toString(), new TelegramApiService.ValleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "wrong", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String cityId) {
                        Toast.makeText(MainActivity.this, "Returned id = " + cityId, Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telegramApiService.getCityForecastById(method.getText().toString(), new TelegramApiService.ForecastByIdResponse() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "wrong", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        //Toast.makeText(MainActivity.this, "Returned id = " + weatherReportModels.toString(), Toast.LENGTH_LONG).show();
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
                        response.setAdapter(arrayAdapter);
                    }
                });
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "click3", Toast.LENGTH_SHORT).show();
            }
        });

    }
}