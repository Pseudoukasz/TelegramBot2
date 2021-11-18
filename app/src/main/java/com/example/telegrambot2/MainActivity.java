package com.example.telegrambot2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button button, button2, button3;
    EditText token;
    ListView responseListView;
    Spinner spinnerEndpointList;
    final TelegramApiService telegramApiService = new TelegramApiService(MainActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        token = findViewById(R.id.method);
        responseListView = findViewById(R.id.response);
        spinnerEndpointList = findViewById(R.id.endpoint_list);
        spinnerEndpointList.setOnItemSelectedListener(this);

        String[] endpointsList = getResources().getStringArray(R.array.endpoint_list);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, endpointsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEndpointList.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                telegramApiService.getCityId(token.getText().toString(), new TelegramApiService.ValleyResponseListener() {
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
                telegramApiService.getCityForecastById(token.getText().toString(), new TelegramApiService.ForecastByIdResponse() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "wrong", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        //Toast.makeText(MainActivity.this, "Returned id = " + weatherReportModels.toString(), Toast.LENGTH_LONG).show();
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
                        responseListView.setAdapter(arrayAdapter);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Toast.makeText(MainActivity.this, "selected = " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
        String spinnerValue = parent.getItemAtPosition(position).toString();
        if (spinnerValue.equals("Check connection")) {
            telegramApiService.checkConnection("getBotInfo", token.getText().toString(), new TelegramApiService.BotInfoInterface() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "wrong, " + message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(List<String> response) {
                    //Toast.makeText(MainActivity.this, "response = " + response, Toast.LENGTH_LONG).show();
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, response);
                    responseListView.setAdapter(arrayAdapter);
                }
            });
        } else if (spinnerValue.equals("Get Updates")) {
            telegramApiService.getUpdates("getUpdates", token.getText().toString(), new TelegramApiService.UpdateList() {
                @Override
                public void onError(String message) {
                    //Toast.makeText(MainActivity.this, "wrong, " + message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(List<String> updatesList) {
                    //Toast.makeText(MainActivity.this, "response = " + updatesList, Toast.LENGTH_LONG).show();
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, updatesList);
                    responseListView.setAdapter(arrayAdapter);
                }
            });

        } else if (spinnerValue.equals("Get All Channels")) {
            telegramApiService.getUpdates("getAllChats", token.getText().toString(), new TelegramApiService.UpdateList() {
                @Override
                public void onError(String message) {
                   // Toast.makeText(MainActivity.this, "wrong, " + message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(List<String> allChats) {
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, allChats);
                    responseListView.setAdapter(arrayAdapter);
                }
            });

        } /*else if (spinnerValue.equals("Sand Message")) {
            telegramApiService.checkConnection("getUpdates", token.getText().toString(), new TelegramApiService.ValleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "wrong, " + message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(String response) {
                    Toast.makeText(MainActivity.this, "response = " + response, Toast.LENGTH_LONG).show();
                }
            });

        } else if (spinnerValue.equals("Forward Message")) {
            telegramApiService.checkConnection("forwardMessage", token.getText().toString(), new TelegramApiService.ValleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "wrong, " + message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(String response) {
                    Toast.makeText(MainActivity.this, "response = " + response, Toast.LENGTH_LONG).show();
                }
            });

        } else if (spinnerValue.equals("Delete Message")) {
            telegramApiService.checkConnection("deleteMessage", token.getText().toString(), new TelegramApiService.ValleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "wrong, " + message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(String response) {
                    Toast.makeText(MainActivity.this, "response = " + response, Toast.LENGTH_LONG).show();
                }
            });

        } else if (spinnerValue.equals("Create Invite Link")) {
            telegramApiService.checkConnection("sendInvite", token.getText().toString(), new TelegramApiService.ValleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "wrong, " + message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(String response) {
                    Toast.makeText(MainActivity.this, "response = " + response, Toast.LENGTH_LONG).show();
                }
            });

        } else if (spinnerValue.equals("Make Poll")) {
            telegramApiService.checkConnection("makePoll", token.getText().toString(), new TelegramApiService.ValleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "wrong, " + message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(String response) {
                    Toast.makeText(MainActivity.this, "response = " + response, Toast.LENGTH_LONG).show();
                }
            });

        }*/

       /* telegramApiService.getCityForecastById(method.getText().toString(), new TelegramApiService.ForecastByIdResponse() {
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
        });*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(MainActivity.this, "nothing selected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}