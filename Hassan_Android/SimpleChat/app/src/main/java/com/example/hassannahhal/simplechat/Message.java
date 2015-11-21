package com.example.hassannahhal.simplechat;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by hassannahhal on 2015-11-19.
 */

@ParseClassName("ChatMessages")
public class Message extends ParseObject {

    public String getUserId() {
        String id = "";
        try {
            id = getString("Id");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (id == null) {
                id = "";
            }
        }

        return id;
    }

    public String getMessageText() {
        return getString("MessageText");
    }

    public String getReceiverId() {
        return getString("To");
    }

    public void setId(String userId) {
        put("Id", userId);
    }

    public void setMessageText(String MessageText) {
        put("MessageText", MessageText);
    }

    public void setPrivateKey(boolean isPrivate) {
        put("Private", isPrivate);
    }

    public void setToUser(String To) {
        put("To", To);
    }
}

