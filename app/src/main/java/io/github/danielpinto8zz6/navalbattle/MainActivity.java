package io.github.danielpinto8zz6.navalbattle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {
    private Animation fabOpenAnimation;
    private Animation fabCloseAnimation;
    private boolean isFabMenuOpen = false;
    private LinearLayout layoutCreateServer;
    private LinearLayout layoutJoinServer;
    private LinearLayout layoutAgainstDevice;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        layoutCreateServer = (LinearLayout) this.findViewById(R.id.create_server_layout);
        layoutJoinServer = (LinearLayout) this.findViewById(R.id.join_server_layout);
        layoutAgainstDevice = (LinearLayout) this.findViewById(R.id.against_device_layout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFabMenuOpen) {
                    collapseFabMenu();
                } else {
                    expandFabMenu();
                }
            }
        });

        FloatingActionButton createServerFab = findViewById(R.id.create_server);
        FloatingActionButton joinServerFab = findViewById(R.id.join_server);
        FloatingActionButton againstComputerFab = findViewById(R.id.against_device);

        createServerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        joinServerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setup = new Intent(getApplicationContext(), ShipSetupActivity.class);
                startActivity(setup);
            }
        });

        againstComputerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent game = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(game);
            }
        });

        fabOpenAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_open);

        fabCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_close);
    }

    private void expandFabMenu() {
        layoutCreateServer.setVisibility(View.VISIBLE);
        layoutCreateServer.startAnimation(fabOpenAnimation);

        layoutJoinServer.setVisibility(View.VISIBLE);
        layoutJoinServer.startAnimation(fabOpenAnimation);

        layoutAgainstDevice.setVisibility(View.VISIBLE);
        layoutAgainstDevice.startAnimation(fabOpenAnimation);

        //Change settings icon to 'X' icon
        fab.setImageResource(R.drawable.ic_close);

        isFabMenuOpen = true;
    }

    private void collapseFabMenu() {
        layoutCreateServer.setVisibility(View.INVISIBLE);
        layoutCreateServer.startAnimation(fabCloseAnimation);

        layoutJoinServer.setVisibility(View.INVISIBLE);
        layoutJoinServer.startAnimation(fabCloseAnimation);

        layoutAgainstDevice.setVisibility(View.INVISIBLE);
        layoutAgainstDevice.startAnimation(fabCloseAnimation);

        fab.setImageResource(R.drawable.ic_play);
        isFabMenuOpen = false;
    }

    @Override
    public void onBackPressed() {

        if (isFabMenuOpen)
            collapseFabMenu();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
