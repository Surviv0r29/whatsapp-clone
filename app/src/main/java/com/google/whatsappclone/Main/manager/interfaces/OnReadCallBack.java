package com.google.whatsappclone.Main.manager.interfaces;

import com.google.whatsappclone.model.chat.Chats;

import java.util.List;

public interface OnReadCallBack {
    void onReadSuccess(List<Chats> list);
    void onReadFailed();
}
