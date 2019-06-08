package com.moon.xposed.test;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "这是一个Toast", Toast.LENGTH_LONG).show();
            }
        });

        StringBuilder sb = new StringBuilder();
        sb.append("型号：").append(" ").append(Build.MODEL).append("\n");
        sb.append("MANUFACTURER：").append(" ").append(Build.MANUFACTURER).append("\n");
        sb.append("DEVICE：").append(" ").append(Build.DEVICE).append("\n");
        sb.append("BOARD：").append(" ").append(Build.BOARD).append("\n");
        sb.append("BRAND：").append(" ").append(Build.BRAND).append("\n");
        TextView modelTv = findViewById(R.id.model_tv);
        modelTv.setText(sb.toString());
        TextView manufacturerTv = findViewById(R.id.manufacturer_tv);
        manufacturerTv.setText(Build.MANUFACTURER);
    }
}
