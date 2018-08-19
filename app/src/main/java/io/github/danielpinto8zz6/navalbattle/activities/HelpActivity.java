package io.github.danielpinto8zz6.navalbattle.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import org.w3c.dom.Text;

import io.github.danielpinto8zz6.navalbattle.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = (TextView) findViewById(R.id.textview);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
