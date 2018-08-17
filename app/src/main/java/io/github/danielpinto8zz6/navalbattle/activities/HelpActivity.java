package io.github.danielpinto8zz6.navalbattle.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import io.github.danielpinto8zz6.navalbattle.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
