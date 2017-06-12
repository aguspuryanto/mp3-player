package com.aguspuryanto.laguazmp3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by AGUS on 10/06/2017.
 */

public class SingleItemView extends Activity {
    // Declare variable
    String phones;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from single_item_view.xml
        setContentView(R.layout.single_item_view);

        // Locate the TextView in single_item_view.xml
        TextView txtPhones = (TextView) findViewById(R.id.text);

        // Get string from intent passed from MainActivity.java
        Intent i = getIntent();
        phones = i.getStringExtra("phones");

        // Set the string into TextView
        txtPhones.setText(phones);

    }
}
