package com.example.triviaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;
import static lib.DialogUtils.showInfoDialog;

public class MainActivity extends AppCompatActivity {
    private Snackbar mSnackBar;


    private TextView questionTextView, feedbackTextView;
    private Button option1Button, option2Button, option3Button, option4Button;
    private Question currentQuestion;
    private QuestionBank questionBank;
    private int score;
    private String mKEY_AUTO_SAVE;
    private boolean mUseAutoSave;
    private final String mKEY_GAME = "GAME";





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
        setupFields();
        setupToolbar();
        setupFAB();
        setButtonListeners();
        //restoreAppSettingsFromPrefs();


    }

    private void setupFields() {
        mKEY_AUTO_SAVE = getString(R.string.auto_save_key);
        questionTextView = findViewById(R.id.question);
        feedbackTextView = findViewById(R.id.feedbackMessage);
        option1Button = findViewById(R.id.option1);
        option2Button = findViewById(R.id.option2);
        option3Button = findViewById(R.id.option3);
        option4Button = findViewById(R.id.option4);

        questionBank = createQuestionBank();

        currentQuestion = questionBank.getNextQuestion();
        this.showQuestion(currentQuestion);

    }

    private void setButtonListeners() {
        option1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexOfOptionSelected = 0;

                validateChoice(indexOfOptionSelected);
            }
        });

        option2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexOfOptionSelected = 1;

                validateChoice(indexOfOptionSelected);
            }
        });

        option3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexOfOptionSelected = 2;

               validateChoice(indexOfOptionSelected);
            }
        });

        option4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexOfOptionSelected = 3;

                validateChoice(indexOfOptionSelected);

            }
        });

    }

    private void validateChoice(int indexOfOptionSelected) {
        if(indexOfOptionSelected == currentQuestion.getAnswerIndex()){
            //answer is correct!
            feedbackTextView.setText(R.string.correct_message);

            questionBank.removeQuestion(currentQuestion);

            if(questionBank.getSize() != 0) {
                currentQuestion = questionBank.getNextQuestion();
                this.showQuestion(currentQuestion);
            }
            else{
                showInfoDialog(this, "GAME OVER","GREAT JOB! YOU WON! You can restart the game or exit the application");
                //TODO: disable buttons
            }


        }
        else{
            feedbackTextView.setText(R.string.incorrect_answer_message);
        }
    }


    private void showQuestion(final Question currentQuestion) {
        questionTextView.setText(currentQuestion.getQuestion());
        option1Button.setText(currentQuestion.getChoiceList().get(0));
        option2Button.setText(currentQuestion.getChoiceList().get(1));
        option3Button.setText(currentQuestion.getChoiceList().get(2));
        option4Button.setText(currentQuestion.getChoiceList().get(3));
    }

    private QuestionBank createQuestionBank() {
        Question question1 = new Question("What color is the sky?", Arrays.asList("Yellow", "Blue", "Pink", "Black"), 1);
        Question question2 = new Question("Who won the presidental election of 2020?", Arrays.asList("Trump", "Oprah", "Dr. Katz", "Biden"), 3);
        Question question3 = new Question("What is the first letter", Arrays.asList("A", "B","C", "D"), 0);
        Question question4 = new Question("What sound does a cow make", Arrays.asList("meow", "woof", "moo", "hello!"), 2);

        return new QuestionBank(Arrays.asList(question1,question2,question3,question4));
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
                showInfoDialog(MainActivity.this,
                        getString(R.string.info_title), getString(R.string.rules));
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
       // saveToSharedPref();
        super.onStop();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_toggle_auto_save).setChecked(mPrefUseAutoSave);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

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
            case R.id.new_game:
                startNewGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showStatistics() {
        Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
        startActivity(intent);
    }



        private void startNewGame() {
           // startGameAndSetBoard(new PMGame(), null, R.string.welcome_new_game);
            questionBank = createQuestionBank();
            currentQuestion = questionBank.getNextQuestion();
            this.showQuestion(currentQuestion);

        }


    private void showAbout() {
        showInfoDialog(this, "Trivia App", "Welcome to our Trivia App!");
    }

    private void toggleMenuItem(MenuItem item) {
        item.setChecked(!item.isChecked());
    }











































    private void restoreOrSetFromPreferences_AllAppAndGameSettings() {
        SharedPreferences sp = getDefaultSharedPreferences(this);
        mUseAutoSave = sp.getBoolean(mKEY_AUTO_SAVE, true);
    }
    private void restoreFromPreferences_SavedGameIfAutoSaveWasSetOn() {
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        if (defaultSharedPreferences.getBoolean(mKEY_AUTO_SAVE,true)) {
            String gameString = defaultSharedPreferences.getString(mKEY_GAME, null);
            if (gameString!=null) {
                //currentQuestion = QuestionBank.getGameFromJSON(gameString);
                //updateUI();
                System.out.println("to be done");
            }
        }
    }

   private void restoreAppSettingsFromPrefs() {
        // Since this is for reading only, no editor is needed unlike in saveRestoreState
        SharedPreferences preferences = getSharedPreferences(mKeyPrefsName, MODE_PRIVATE);

        // restore AutoSave preference value
        mPrefUseAutoSave = preferences.getBoolean(mKeyAutoSave, true);
    }

    private void saveGameAndBoardToSharedPrefsIfAutoSaveIsOn(SharedPreferences.Editor editor) {
        System.out.println("placeholder");
    }

    private void saveSettingsToSharedPrefs(SharedPreferences.Editor editor) {
        // save "autoSave" preference
        editor.putBoolean(mKeyAutoSave, mPrefUseAutoSave);
    }

    private void saveOrDeleteGameInSharedPrefs(){
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();

        //Save current game or remove any prior game to/from default shared preferences
        if(mUseAutoSave)
            //editor.putString(mKEY_GAME, questionBank.getJSONFromCurrentGame());
            System.out.println();
        else
            editor.remove(mKEY_GAME);

        editor.apply();
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



}

