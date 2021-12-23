package com.example.telegrambot2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.telegrambot2.MainActivity;
import com.example.telegrambot2.Model.ChatModel;
import com.example.telegrambot2.Model.UpdateModel;
import com.example.telegrambot2.R;
import com.example.telegrambot2.TelegramApiService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdateAdapter extends ArrayAdapter<UpdateModel> {

    final TelegramApiService telegramApiService;
    final List<ChatModel> chatModelsList;
    final String token;

    public UpdateAdapter(Context context, List<UpdateModel> updates, List<ChatModel> chatModelsList, String token) {
        super(context, 0, updates);
        this.telegramApiService = new TelegramApiService(context);
        this.chatModelsList = chatModelsList;
        this.token = token;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UpdateModel updateModel = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.update_item, parent, false);
        }
        TextView chatNameDisplay = (TextView) convertView.findViewById(R.id.chatNameDisplay);
        TextView messageDisplay = (TextView) convertView.findViewById(R.id.messageDisplay);
        TextView messageDateDisplay = (TextView) convertView.findViewById(R.id.messageDateDisplay);
        TextView updateItemMessageId = (TextView) convertView.findViewById(R.id.updateItemMessageId);

        Timestamp timestamp = new Timestamp(updateModel.getDateTime() * 1000);
        Date updateDate = new Date(timestamp.getTime());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(updateDate);
        chatNameDisplay.setText(updateModel.getChatTitle());
        messageDisplay.setText(updateModel.getText());
        messageDateDisplay.setText(date);
        convertView.setTag(updateModel);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateModel updateModel = (UpdateModel) view.getTag();
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.remove:
                                telegramApiService.deleteMessage("deleteMessage", token, updateModel.getChatId(), updateModel.getMessageId(), new TelegramApiService.StringResponseListener() {
                                    @Override
                                    public void onError(String message) {
                                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onResponse(String message) {
                                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return true;
                            case R.id.forward:
                                PopupMenu chatMenu = new PopupMenu(getContext(), view);
                                for (int i = 0; i < chatModelsList.size(); i++) {
                                    chatMenu.getMenu().add(i, i, i, chatModelsList.get(i).getTitle());
                                }
                                chatMenu.show();
                                chatMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        telegramApiService.forwardMessage("forwardMessage", token, updateModel.getChatId(), chatModelsList.get(item.getItemId()).getChatId(), updateModel.getMessageId(), new TelegramApiService.StringResponseListener() {
                                            @Override
                                            public void onError(String message) {
                                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                            }
                                            @Override
                                            public void onResponse(String message) {
                                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        return false;
                                    }
                                });
                                return true;
                            case R.id.pinToTop:
                                telegramApiService.pinToTopMessage("pinChatMessage", token, updateModel.getChatId(), updateModel.getMessageId(), new TelegramApiService.StringResponseListener() {
                                    @Override
                                    public void onError(String message) {
                                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onResponse(String message) {
                                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return true;
                        }
                        Toast.makeText(getContext(), item.getItemId(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }
        });

        return convertView;
    }
    
}
