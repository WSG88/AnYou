package com.anyou.yx;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see);
        String json = App.getApplication().queryDB();
        TextView tv = findViewById(R.id.tv);
        tv.setText(json);
    }
}
