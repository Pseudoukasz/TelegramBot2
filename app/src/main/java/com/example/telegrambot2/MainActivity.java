package com.example.telegrambot2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.telegrambot2.Model.ChatModel;
import com.example.telegrambot2.Model.ChatShowModel;
import com.example.telegrambot2.Model.UpdateModel;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button button, button2, button3, sendMessageButton, setChatDescriptionButton, sendPollButton, addPollOptionButton;
    int optionCount = 1;
    EditText token, messageEditText, descriptionEditText, pollQuestionEditText;
    LinearLayout pollOptionsLayout;
    ListView responseListView;
    Spinner spinnerEndpointList, chatsListSpinner;
    List<ChatModel> allChatsList = new ArrayList<>();
    List<UpdateModel> responseUpdatesList = new ArrayList<>();
    final TelegramApiService telegramApiService = new TelegramApiService(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //button = findViewById(R.id.button);
        //button2 = findViewById(R.id.button2);
        //button3 = findViewById(R.id.button3);
        chatsListSpinner = findViewById(R.id.chat_list);
        messageEditText = findViewById(R.id.message);
        token = findViewById(R.id.method);
        responseListView = findViewById(R.id.response);
        spinnerEndpointList = findViewById(R.id.endpoint_list);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        setChatDescriptionButton = findViewById(R.id.setChatDescriptionButton);
        descriptionEditText = findViewById(R.id.description);
        sendPollButton = findViewById(R.id.sendPoll);
        addPollOptionButton = findViewById(R.id.addPoolOption);
        pollOptionsLayout = findViewById(R.id.pollOptions);
        pollQuestionEditText = findViewById(R.id.pollQuestion);
        spinnerEndpointList.setOnItemSelectedListener(this);

        String[] endpointsList = getResources().getStringArray(R.array.endpoint_list);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, endpointsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEndpointList.setAdapter(adapter);

        /*button.setOnClickListener(new View.OnClickListener() {
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
        });*/
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chat = (ChatModel) chatsListSpinner.getSelectedItem();
                Toast.makeText(MainActivity.this, chat.getChatId() + ", " + chat.getTitle(), Toast.LENGTH_LONG).show();
                telegramApiService.sendMessage("sandMessage", token.getText().toString(), chat.getChatId(), messageEditText.getText().toString(), new TelegramApiService.StringResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        setChatDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chat = (ChatModel) chatsListSpinner.getSelectedItem();
                telegramApiService.setChatDescription("setChatDescription", token.getText().toString(), chat.getChatId(), descriptionEditText.getText().toString(), new TelegramApiService.StringResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
        addPollOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionCount == 6) {
                    Toast.makeText(MainActivity.this, "Option limit: 5", Toast.LENGTH_SHORT).show();
                } else {
                    EditText pollOptionEdit = new EditText(MainActivity.this);
                    pollOptionEdit.setVisibility(View.VISIBLE);
                    pollOptionEdit.setId(optionCount);
                    pollOptionEdit.setHint("Option " + optionCount);
                    pollOptionEdit.setClickable(true);
                    pollOptionEdit.setFocusable(true);
                    pollOptionsLayout.addView(pollOptionEdit);
                    optionCount++;
                }
            }
        });
        sendPollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chat = (ChatModel) chatsListSpinner.getSelectedItem();
                JSONObject options = new JSONObject();
                for (int i = 0; i < pollOptionsLayout.getChildCount(); i++) {
                    if (pollOptionsLayout.getChildAt(i) instanceof EditText && ((EditText) pollOptionsLayout.getChildAt(i)).getText().toString().length() != 0) {
                        try {
                            options.put("option" + i,  ((EditText) pollOptionsLayout.getChildAt(i)).getText().toString() );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //JSONObject options5 = new JSONObject(params);
                telegramApiService.sendPoll("makePoll", token.getText().toString(), chat.getChatId(), pollQuestionEditText.getText().toString(), options, new TelegramApiService.StringResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String message) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                        pollQuestionEditText.setText("");
                        pollOptionsLayout.removeAllViews();
                        optionCount = 1;
                    }
                });
                Toast.makeText(MainActivity.this, options.toString(), Toast.LENGTH_LONG).show();
            }
        });
        responseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = parent.getPositionForView(view);
                UpdateModel updateModel = responseUpdatesList.get(pos);
                Toast.makeText(getApplicationContext(), pos + " " + updateModel.getText(), Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.remove:
                                telegramApiService.deleteMessage("deleteMessage", token.getText().toString(), updateModel.getChatId(), updateModel.getMessageId(), new TelegramApiService.StringResponseListener() {
                                    @Override
                                    public void onError(String message) {
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onResponse(String message) {
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return true;
                            case R.id.forward:
                                PopupMenu chatMenu = new PopupMenu(MainActivity.this, view);
                                for (int i = 0; i < allChatsList.size(); i++) {
                                    chatMenu.getMenu().add(i, i, i, allChatsList.get(i).getTitle());
                                }
                                chatMenu.show();
                                chatMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        telegramApiService.forwardMessage("forwardMessage", token.getText().toString(), updateModel.getChatId(), allChatsList.get(item.getItemId()).getChatId(), updateModel.getMessageId(), new TelegramApiService.StringResponseListener() {
                                            @Override
                                            public void onError(String message) {
                                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                            }
                                            @Override
                                            public void onResponse(String message) {
                                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        return false;
                                    }
                                });
                                return true;
                            case R.id.pinToTop:
                                telegramApiService.pinToTopMessage("pinChatMessage", token.getText().toString(), updateModel.getChatId(), updateModel.getMessageId(), new TelegramApiService.StringResponseListener() {
                                    @Override
                                    public void onError(String message) {
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onResponse(String message) {
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return true;
                        }
                        Toast.makeText(MainActivity.this, item.getItemId(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        messageEditText.setVisibility(View.GONE);
        chatsListSpinner.setVisibility(View.GONE);
        sendMessageButton.setVisibility(View.GONE);
        setChatDescriptionButton.setVisibility(View.GONE);
        descriptionEditText.setVisibility(View.GONE);
        responseListView.setVisibility(View.VISIBLE);
        sendPollButton.setVisibility(View.GONE);
        addPollOptionButton.setVisibility(View.GONE);
        pollOptionsLayout.setVisibility(View.GONE);
        pollQuestionEditText.setVisibility(View.GONE);

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
                public void onResponse(List<UpdateModel> updatesList) {
                    responseUpdatesList = updatesList;
                    ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, updatesList);
                    responseListView.setAdapter(arrayAdapter);
                }
            });

        } else if (spinnerValue.equals("Get All Channels")) {
            telegramApiService.getAllChats("getAllChats", token.getText().toString(), new TelegramApiService.ChatsListInterface() {
                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(List<ChatShowModel> allChats) {
                    for (int i = 0; i < allChats.size(); i++) {
                        ChatModel chatInfo = new ChatModel();
                        chatInfo.setChatId(allChats.get(i).getChatId());
                        chatInfo.setTitle(allChats.get(i).getTitle());
                        chatInfo.setInviteLink(allChats.get(i).getInviteLink());
                        allChatsList.add(chatInfo);
                    }
                    //allChatsList = allChats;
                    //SpinnerAdapter
                    ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, allChats);
                    responseListView.setAdapter(arrayAdapter);
                }
            });

        } else if (spinnerValue.equals("Sand Message")) {
            ArrayAdapter<ChatModel> chatsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allChatsList);
            //SpinnerAdapter chatsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, chatsSpinnerList);
            chatsListSpinner.setAdapter(chatsAdapter);
            messageEditText.setVisibility(View.VISIBLE);
            chatsListSpinner.setVisibility(View.VISIBLE);
            sendMessageButton.setVisibility(View.VISIBLE);
            responseListView.setVisibility(View.GONE);


        } else if (spinnerValue.equals("Set Chat Description")) {
            ArrayAdapter<ChatModel> chatsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allChatsList);
            //SpinnerAdapter chatsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, chatsSpinnerList);
            chatsListSpinner.setAdapter(chatsAdapter);
            descriptionEditText.setVisibility(View.VISIBLE);
            chatsListSpinner.setVisibility(View.VISIBLE);
            setChatDescriptionButton.setVisibility(View.VISIBLE);
            responseListView.setVisibility(View.GONE);

        } else if (spinnerValue.equals("Make Poll")) {
            ArrayAdapter<ChatModel> chatsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allChatsList);
            chatsListSpinner.setAdapter(chatsAdapter);
            chatsListSpinner.setVisibility(View.VISIBLE);
            sendPollButton.setVisibility(View.VISIBLE);
            addPollOptionButton.setVisibility(View.VISIBLE);
            pollOptionsLayout.setVisibility(View.VISIBLE);
            responseListView.setVisibility(View.GONE);
            pollQuestionEditText.setVisibility(View.VISIBLE);
        } /*else if (spinnerValue.equals("Delete Message")) {
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


    public void doo(View view) {
        Toast.makeText(MainActivity.this, "traasd", Toast.LENGTH_LONG);
    }
}