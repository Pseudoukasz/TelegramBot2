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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button button, button2, button3, sendMessageButton, setChatDescriptionButton, sendPollButton, addPollOptionButton, sendPoll2;
    int optionCount = 1;
    EditText token, messageEditText, descriptionEditText;
    TextInputLayout pollOptionsLayout;
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
                EditText pollOptionEdit = new EditText(MainActivity.this);
                pollOptionEdit.setVisibility(View.VISIBLE);
                pollOptionEdit.setHint("Option " + optionCount);
                //pollOptionEdit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                pollOptionEdit.setClickable(true);
                pollOptionEdit.setFocusable(true);

                pollOptionsLayout.addView(pollOptionEdit, optionCount);
                //pollOptionsLayout.addView(pollOptionEdit);
                optionCount++;

            }
        });
        //pollOptionsLayout.set
        /*button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "click3", Toast.LENGTH_SHORT).show();
            }
        });*/
        //responseListView.setOnItemClickListener(new View.OnClickListener());

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
                                        //Toast.makeText(MainActivity.this, "forward msg " + updateModel.getText() + item.getItemId(), Toast.LENGTH_SHORT).show();

                                        return false;
                                    }
                                });
                                //Toast.makeText(MainActivity.this, "forward msg " + updateModel.getText(), Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.pinToTop:
                                telegramApiService.pinToTopMessage("pinChatMessage", token.getText().toString(), updateModel.getChatId(),updateModel.getMessageId(), new TelegramApiService.StringResponseListener() {
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

        }else if (spinnerValue.equals("Make Poll")) {
            ArrayAdapter<ChatModel> chatsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allChatsList);
            chatsListSpinner.setAdapter(chatsAdapter);
            chatsListSpinner.setVisibility(View.VISIBLE);
            sendPollButton.setVisibility(View.VISIBLE);
            addPollOptionButton.setVisibility(View.VISIBLE);
            pollOptionsLayout.setVisibility(View.VISIBLE);
            responseListView.setVisibility(View.GONE);
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
        Toast.makeText(MainActivity.this, "traasd", Toast.LENGTH_LONG );
    }
}