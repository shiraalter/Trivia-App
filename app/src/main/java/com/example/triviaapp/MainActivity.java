package com.example.triviaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;
import static lib.DialogUtils.showInfoDialog;

public class MainActivity extends AppCompatActivity {
    private Snackbar mSnackBar;
    // Preference booleans; indicates if these respective settings currently enabled/disabled
    private boolean mPrefUseAutoSave;

    // Name of Preference file on device
    private final String mKeyPrefsName = "PREFS";
    // Preference Keys: values are already in strings.xml and will be assigned to these in onCreate
    private String mKeyAutoSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupFAB();
        restoreAppSettingsFromPrefs();

        mSnackBar =
                Snackbar.make(findViewById(android.R.id.content), "Welcome!",
                        Snackbar.LENGTH_LONG);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //creates menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStop() {
        saveToSharedPref();
        super.onStop();
    }
    private void saveToSharedPref() {
        // Create a SP reference to the prefs file on the device whose name matches mKeyPrefsName
        // If the file on the device does not yet exist, then it will be created
        SharedPreferences preferences = getSharedPreferences(mKeyPrefsName, MODE_PRIVATE);

        // Create an Editor object to write changes to the preferences object above
        SharedPreferences.Editor editor = preferences.edit();

        // clear whatever was set last time
        editor.clear();

        // save the settings (Show Errors and Use AutoSave)
        saveSettingsToSharedPrefs(editor);

        // if autoSave is on then save the board
        saveGameAndBoardToSharedPrefsIfAutoSaveIsOn(editor);

        // apply the changes to the XML file in the device's storage
        editor.apply();
    }

    private void saveGameAndBoardToSharedPrefsIfAutoSaveIsOn(SharedPreferences.Editor editor) {
        System.out.println("placeholder");
    }

    private void saveSettingsToSharedPrefs(SharedPreferences.Editor editor) {
         // save "autoSave" preference
        editor.putBoolean(mKeyAutoSave, mPrefUseAutoSave);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        dismissSnackBarIfShown();

        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_about:
                showAbout();
                return true;
            case R.id.action_toggle_auto_save:
                toggleMenuItem(item);
                mPrefUseAutoSave = item.isChecked();
                return true;
            case R.id.action_statistics:
                showStatistics();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showStatistics() {
        Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
        startActivity(intent);
    }

    private void dismissSnackBarIfShown() {
        if (mSnackBar.isShown())
            mSnackBar.dismiss();
    }

    public void startNewGame(@SuppressWarnings("UnusedParameters") MenuItem item) {
        startNewGame();
    }

        private void startNewGame() {
           // startGameAndSetBoard(new PMGame(), null, R.string.welcome_new_game);
            System.out.println("you clicked new game!");
        }


    private void showAbout() {
        showInfoDialog(this, "Trivia App", "Welcome to our Trivia App!");
    }

    private void toggleMenuItem(MenuItem item) {
        item.setChecked(!item.isChecked());
    }

    private void restoreAppSettingsFromPrefs() {
        // Since this is for reading only, no editor is needed unlike in saveRestoreState
        SharedPreferences preferences = getSharedPreferences(mKeyPrefsName, MODE_PRIVATE);

        // restore AutoSave preference value
        mPrefUseAutoSave = preferences.getBoolean(mKeyAutoSave, true);
    }



}

