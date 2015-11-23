package com.example.hassannahhal.simplechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by hassannahhal on 2015-11-22.
 */
public class LogIn extends Activity {

    private String id;
    private EditText idEditText;
    private Button submitButton;
    private static final String MY_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        idEditText = (EditText) findViewById(R.id.idEditText);
        submitButton = (Button) findViewById(R.id.submitIdButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempID = idEditText.getText().toString();
                if (tempID != "") {
                    id = idEditText.getText().toString();
                    Intent myIntent = new Intent(LogIn.this, ChatActivity.class);
                    myIntent.putExtra(MY_ID, id);
                    startActivity(myIntent);
                }
            }
        });


    }
}
