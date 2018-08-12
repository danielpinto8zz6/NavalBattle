package io.github.danielpinto8zz6.navalbattle;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

import static io.github.danielpinto8zz6.navalbattle.Constants.PORT;

public class MainActivity extends AppCompatActivity implements Serializable, NavigationView.OnNavigationItemSelectedListener {
    private Animation fabOpenAnimation;
    private Animation fabCloseAnimation;
    private boolean isFabMenuOpen = false;
    private LinearLayout layoutCreateServer;
    private LinearLayout layoutJoinServer;
    private LinearLayout layoutAgainstDevice;
    private FloatingActionButton fab;

    private BufferedReader input;
    private PrintWriter output;
    private ServerSocket serverSocket = null;
    private Socket socketGame = null;
    private Handler procMsg = null;
    private ProgressDialog pd = null;

    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String username = prefs.getString("key_username", "");

        View header = navigationView.getHeaderView(0);

        final ImageView imageView = (ImageView) header.findViewById(R.id.drawer_avatar);

        String avatarBase64 = prefs.getString("avatar", "");

        final TextView textViewUsername = (TextView) header.findViewById(R.id.drawer_username);

        //Setup a shared preference listener for hpwAddress and restart transport
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("username")) {
                    String username = prefs.getString("key_username", "");
                    textViewUsername.setText(username);
                }
                if (key.equals("avatar")) {
                    String avatarBase64 = prefs.getString("avatar", "");
                    imageView.setImageBitmap(Utils.decodeBase64(avatarBase64));
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(listener);

        if (username.length() > 0)
            textViewUsername.setText(username);

        if (avatarBase64.length() > 0)
            imageView.setImageBitmap(Utils.decodeBase64(avatarBase64));

        final TextView textViewSummary = (TextView) header.findViewById(R.id.drawer_summary);
        textViewSummary.setText(Utils.getLocalIpAddress());

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
                showWaitConnectionDialog();
            }
        });


        joinServerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputIpDialog();
            }
        });

        againstComputerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setup = new Intent(getApplicationContext(), ShipSetupActivity.class);
                startActivity(setup);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isFabMenuOpen)
            collapseFabMenu();
        else {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.sure_want_to_exit)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showInputIpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Text");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
                    if (!resultingTxt.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i = 0; i < splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }

        };
        input.setFilters(filters);

        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void showWaitConnectionDialog() {
        String ip = Utils.getLocalIpAddress();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.wait_connection) + "\n(" + ip
                + ")");
        pd.setTitle(R.string.serverdlg_title);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                    }
                    serverSocket = null;
                }
            }
        });
        pd.show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);
                    socketGame = serverSocket.accept();
                    serverSocket.close();
                    serverSocket = null;
                    commThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    socketGame = null;
                }
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        if (socketGame == null)
                            finish();
                    }
                });
            }
        });
        t.start();
    }

    Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(
                        socketGame.getInputStream()));
                output = new PrintWriter(socketGame.getOutputStream());
                while (!Thread.currentThread().isInterrupted()) {
                    String read = input.readLine();
                    final int move = Integer.parseInt(read);
                    Log.d("Naval Battle", "Received: " + move);
                    procMsg.post(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            } catch (Exception e) {
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Toast.makeText(getApplicationContext(),
                                R.string.game_finished, Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        }
    });
}
