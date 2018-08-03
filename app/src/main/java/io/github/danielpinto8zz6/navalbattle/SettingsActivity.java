package io.github.danielpinto8zz6.navalbattle;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;

public class SettingsActivity extends AppCompatPreferenceActivity implements Serializable {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof EditTextPreference) {
                if (preference.getKey().equals("key_username")) {
                    // update the changed gallery name to summary filed
                    preference.setSummary(stringValue);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * Email client intent to send support mail
     * Appends the necessary device information to email body
     * useful when providing support
     */
    public static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"danielpinto8zz6@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_username)));

            // feedback preference click listener
            Preference myPref = findPreference(getString(R.string.key_send_feedback));
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    sendFeedback(getActivity());
                    return true;
                }
            });

            PackageManager pm = getActivity().getApplicationContext().getPackageManager();

            Preference takePhoto = findPreference("key_take_photo");

            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                takePhoto.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    public boolean onPreferenceClick(Preference preference) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                    Manifest.permission.CAMERA)) {
                                Snackbar.make(getView(), "You need to accept permission in order to take photo.", Snackbar.LENGTH_LONG).show();

                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        Constants.MY_PERMISSIONS_REQUEST_CAMERA);
                            } else {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        Constants.MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivityForResult(intent, Constants.REQUEST_IMAGE_CAPTURE);
                            }
                            return true;
                        }
                        return false;
                    }
                });
            } else {
                PreferenceScreen screen = getPreferenceScreen();
                screen.removePreference(takePhoto);
            }

            Preference avatar = findPreference(getString(R.string.key_avatar));

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

            String base64 = settings.getString("avatar", "");

            if (base64.length() > 0) {
                Bitmap bitmap = Utils.decodeBase64(base64);

                Drawable drawableAvatar = new BitmapDrawable(getResources(), bitmap);

                avatar.setIcon(drawableAvatar);
            }

            avatar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    // Here, thisActivity is the current activity
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Snackbar.make(getView(), "You need to accept permission in order to load an avatar.", Snackbar.LENGTH_LONG).show();

                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        } else {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        startActivityForResult(intent, Constants.REQUEST_PICK_AVATAR);
                        return true;
                    }

                    return false;
                }
            });
        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                Preference avatar = findPreference(getString(R.string.key_avatar));
                avatar.setIcon(new BitmapDrawable(getResources(), imageBitmap));

                saveImageIntoPreferences(imageBitmap);

            } else if (requestCode == Constants.REQUEST_PICK_AVATAR && resultCode == RESULT_OK) {

                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                String picturePath = c.getString(c.getColumnIndex(filePath[0]));

                c.close();

                if (picturePath == null) {
                    Toast.makeText(getActivity(), "Error loading avatar!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                saveImageIntoPreferences(picturePath);
            }
        }

        private void saveImageIntoPreferences(String picturePath) {
            String encodedBase64;

            Bitmap resizedBitmap = Utils.getCircleBitmap(Bitmap.createScaledBitmap(
                    BitmapFactory.decodeFile(picturePath), 200, 200, false));

            Preference avatar = findPreference(getString(R.string.key_avatar));

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();

            encodedBase64 = Utils.encodeTobase64(resizedBitmap);

            editor.putString("avatar", encodedBase64);
            avatar.setIcon(new BitmapDrawable(getResources(), Utils.decodeBase64(encodedBase64)));

            editor.commit();

            Toast.makeText(getActivity(), "Avatar Loaded", Toast.LENGTH_LONG).show();
        }

        private void saveImageIntoPreferences(Bitmap picture) {
            Toast.makeText(getActivity(), "I got here!",
                    Toast.LENGTH_LONG).show();

            String encodedBase64;

            Bitmap resizedBitmap = Utils.getCircleBitmap(Bitmap.createScaledBitmap(picture, 200, 200, false));

            Preference avatar = findPreference(getString(R.string.key_avatar));

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();

            encodedBase64 = Utils.encodeTobase64(resizedBitmap);

            editor.putString("avatar", encodedBase64);
            avatar.setIcon(new BitmapDrawable(getResources(), Utils.decodeBase64(encodedBase64)));

            editor.commit();

            Toast.makeText(getActivity(), "Avatar Loaded", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(intent, Constants.REQUEST_PICK_AVATAR);
                }
            } else if (requestCode == Constants.MY_PERMISSIONS_REQUEST_CAMERA) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(intent, Constants.REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        }
    }
}