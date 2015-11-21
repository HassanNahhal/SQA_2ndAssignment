package com.example.hassannahhal.simplechat;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by hassannahhal on 2015-11-19.
 */

@ParseClassName("ChatMessages")
public class Message extends ParseObject {

    private static final String PARSE_TO = "To";
    private static final String PARSE_PRIVATE = "Private";
    private static final String PARSE_ID = "Id";
    private static final String PARSE_MESSAGE_TEXT = "MessageText";

    public String getUserId() {
        String id = "";
        try {
            id = getString(PARSE_ID);
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
        return getString(PARSE_MESSAGE_TEXT);
    }

    public String getReceiverId() {
        String id = "";
        try {
            id = getString(PARSE_TO);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (id == null) {
                id = "";
            }
        }
        return id;
    }

    public void setId(String userId) {
        put(PARSE_ID, userId);
    }

    public void setMessageText(String MessageText) {
        put(PARSE_MESSAGE_TEXT, MessageText);
    }

    public void setPrivateKey(boolean isPrivate) {
        put(PARSE_PRIVATE, isPrivate);
    }

    public void setToUser(String To) {
        put(PARSE_TO, To);
    }
}

