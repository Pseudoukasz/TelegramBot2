package com.example.telegrambot2.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.telegrambot2.Model.ChatModel;
import com.example.telegrambot2.Model.ChatShowModel;
import com.example.telegrambot2.R;
import com.example.telegrambot2.TelegramApiService;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatModel> {
    final TelegramApiService telegramApiService;
    //final List<ChatModel> chatModelList;

    public ChatAdapter(Context context, List<ChatModel> chatModels) {
        super(context, 0, chatModels);
        this.telegramApiService = new TelegramApiService(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatModel chatModel = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_item, parent, false);
        }
        TextView chatNameDisplay = (TextView) convertView.findViewById(R.id.chatListNameDisplay);
        TextView chatIdDisplay = (TextView) convertView.findViewById(R.id.chatIdDisplay);
        TextView chatLinkDisplay = (TextView) convertView.findViewById(R.id.chatLinkDisplay);
        Button copyLinkButton = (Button) convertView.findViewById(R.id.copyChatLink);

        chatNameDisplay.setText("Chat name: " + chatModel.getTitle());
        chatIdDisplay.setText("Chat Id: " + chatModel.getChatId());
        chatLinkDisplay.setText(chatModel.getInviteLink());
        copyLinkButton.setTag(chatModel);
        convertView.setTag(chatModel);


        copyLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatModel clickedChatModel = (ChatModel) copyLinkButton.getTag();
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("link", clickedChatModel.getInviteLink());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(), "Link Copied", Toast.LENGTH_SHORT).show();
            }
        });




        return convertView;
    }

}
