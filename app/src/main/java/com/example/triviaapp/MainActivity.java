package com.example.triviaapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.triviaapp.QuestionBank.getJSONFromQuestionBank;
import static lib.DialogUtils.showInfoDialog;

public class MainActivity extends AppCompatActivity {
    private TextView questionTextView, scoreTextView;
    private Button option1Button, option2Button, option3Button, option4Button;
    private Question currentQuestion;
    private QuestionBank mQuestionBank;
    private int score, gamesWon, totalGamesPlayed, gamesLost;
    private boolean mPrefUseAutoSave;
    private String mKeyAutoSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupFields();
        setupToolbar();
        setupFAB();
        setButtonListeners();
        restoreAppSettingsFromPrefs();
        startOrResumeGame(savedInstanceState);
    }

    private void startOrResumeGame(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            if (mPrefUseAutoSave) {
                SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
                String JSON = defaultSharedPreferences.getString("Game", "");
                // restoreGame
                if(JSON.equals("")){
                    mQuestionBank = createQuestionBank();
                    currentQuestion = mQuestionBank.getNextQuestion();
                }else{
                    mQuestionBank = QuestionBank.getQuestionBankFromJSON(JSON);
                    score = defaultSharedPreferences.getInt(getString(R.string.score_save_state), 0);
                    totalGamesPlayed = defaultSharedPreferences.getInt(getString(R.string.total_games_save_state), 0);
                    gamesWon = defaultSharedPreferences.getInt(getString(R.string.games_won_save_state), 0);
                    gamesLost = defaultSharedPreferences.getInt(getString(R.string.games_lost_save_state), 0);
                    if(mQuestionBank.getSize() == 0){
                        mQuestionBank = createQuestionBank();
                        currentQuestion = mQuestionBank.getNextQuestion();
                        this.showQuestion(currentQuestion);
                    }
                    else {
                        resumeGameStateOrDisableButtons();
                    }
                }
            } else{
                mQuestionBank = createQuestionBank();
                currentQuestion = mQuestionBank.getNextQuestion();
                this.showQuestion(currentQuestion);
                score = 0;
                totalGamesPlayed = 0;
                gamesWon = 0;
                gamesLost = 0;
            }
        } else {
            mQuestionBank = QuestionBank.getQuestionBankFromJSON(savedInstanceState.getString("Game"));
            score = savedInstanceState.getInt(getString(R.string.score_save_state));
            totalGamesPlayed = savedInstanceState.getInt(getString(R.string.total_games_save_state));
            gamesWon = savedInstanceState.getInt(getString((R.string.games_won_save_state)));
            gamesLost = savedInstanceState.getInt(getString(R.string.games_lost_save_state));
            resumeGameStateOrDisableButtons();
        }
    }

    @SuppressLint("SetTextI18n")
    private void resumeGameStateOrDisableButtons() {
        if (mQuestionBank.getSize() != 0) {
            currentQuestion = mQuestionBank.getCurrentQuestion();
            this.showQuestion(currentQuestion);
            scoreTextView.setText(getString(R.string.updated_score) + score);
        } else {
            disableButtons();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setupFields() {
        mKeyAutoSave = getString(R.string.key_use_auto_save);
        questionTextView = findViewById(R.id.question);
        scoreTextView = findViewById(R.id.scoreUpdate);

        option1Button = findViewById(R.id.option1);
        option2Button = findViewById(R.id.option2);
        option3Button = findViewById(R.id.option3);
        option4Button = findViewById(R.id.option4);
    }

    private void setButtonListeners() {
        option1Button.setOnClickListener(v -> {
            int indexOfOptionSelected = 0;

            validateChoice(indexOfOptionSelected);
        });

        option2Button.setOnClickListener(v -> {
            int indexOfOptionSelected = 1;

            validateChoice(indexOfOptionSelected);
        });

        option3Button.setOnClickListener(v -> {
            int indexOfOptionSelected = 2;

            validateChoice(indexOfOptionSelected);
        });

        option4Button.setOnClickListener(v -> {
            int indexOfOptionSelected = 3;
            validateChoice(indexOfOptionSelected);
        });
    }

    @SuppressLint("SetTextI18n")
    private void validateChoice(int indexOfOptionSelected) {
        if (indexOfOptionSelected == currentQuestion.getAnswerIndex()) {
            //answer is correct!
            score += 1;
            scoreTextView.setText(getString(R.string.updated_score) + score);
            mQuestionBank.removeQuestion(currentQuestion);

            if (mQuestionBank.getSize() != 0) {
                currentQuestion = mQuestionBank.getNextQuestion();
                this.showQuestion(currentQuestion);
            } else {
                showInfoDialog(this, "YOU WON!!!", "GREAT JOB! You can restart the game or exit the application");
                disableButtons();
                gamesWon++;
                mQuestionBank.setmNumberGamesWon(gamesWon);
                totalGamesPlayed++;
                mQuestionBank.setmNumberOfGamesPlayed(totalGamesPlayed);
            }

        } else {
            showInfoDialog(this, "YOU LOST :(", "Sorry bout that. Restart game to try again!");
            disableButtons();
            int size = mQuestionBank.getSize();
            for (int i = 0; i < size; i++) {
                mQuestionBank.removeQuestion(currentQuestion);
                if (mQuestionBank.getSize() != 0) {
                    currentQuestion = mQuestionBank.getNextQuestion();
                }
            }
            gamesLost++;
            mQuestionBank.setmNumberGamesLost(gamesLost);
            totalGamesPlayed++;
            mQuestionBank.setmNumberOfGamesPlayed(totalGamesPlayed);
        }
    }

    private void disableButtons() {
        option1Button.setEnabled(false);
        option2Button.setEnabled(false);
        option3Button.setEnabled(false);
        option4Button.setEnabled(false);
    }


    private void showQuestion(final Question currentQuestion) {
        questionTextView.setText(currentQuestion.getQuestion());
        option1Button.setText(currentQuestion.getChoiceList().get(0));
        option2Button.setText(currentQuestion.getChoiceList().get(1));
        option3Button.setText(currentQuestion.getChoiceList().get(2));
        option4Button.setText(currentQuestion.getChoiceList().get(3));
    }

    private QuestionBank createQuestionBank() {
        Question question1 = new Question("What is the rarest M&M color?",
                Arrays.asList("Yellow", "Brown", "Blue", "Orange"), 1);
        Question question2 = new Question("In what year were the first Air Jordan sneakers released?",
                Arrays.asList("1967", "1975", "1998", "1984"), 3);
        Question question3 = new Question("In a game of bingo, which number is represented by the phrase “two little ducks”?",
                Arrays.asList("22", "55", "44", "11"), 0);
        Question question4 = new Question("In which European city would you find Orly airport?",
                Arrays.asList("London", "Berlin", "Paris", "Rome"), 2);
        Question question5 = new Question("Fissures, vents and plugs are all associated with which geological feature?",
                Arrays.asList("Plateaus", "Grasslands", "Volcanoes", "Mountains"), 2);
        Question question6 = new Question("Which Dutch artist painted “Girl with a Pearl Earring”?",
                Arrays.asList("van der Helst", "Vermeer", "Stevens", "Bloemaert"), 1);
        Question question7 = new Question("Which country consumes the most chocolate per capita?",
                Arrays.asList("United Kingdom", "Italy", "USA", "Switzerland"), 3);
        Question question8 = new Question("Which two U.S. states don’t observe Daylight Saving Time?",
                Arrays.asList("Arizona and Texas", "Nevada and Montana", "Arizona and Hawaii", "Alaska and Oregon"), 2);
        Question question9 = new Question("Which of Shakespeare’s plays is the longest?",
                Arrays.asList("Hamlet", "Julius Caesar", "Romeo and Juliet", "The Tempest"), 0);
        Question question10 = new Question("How many of Snow White’s seven dwarfs have names ending in the letter Y?",
                Arrays.asList("Three", "Five", "Four", "Six"), 1);
        Question question11 = new Question("What is the tallest breed of dog in the world?",
                Arrays.asList("German Shepherd", "Golden Retriever", "The Great Dane", ""), 2);
        Question question12 = new Question("How many ribs are in a human body?",
                Arrays.asList("Twenty-six", "Twenty-Two", "Twenty", "Twenty-four"), 3);
        Question question13 = new Question("What is the world’s biggest island?",
                Arrays.asList("New Guinea", "Greenland", "Borneo", "Madagascar"), 1);
        Question question14 = new Question("What is the smallest ocean in the world?",
                Arrays.asList("The Indian Ocean", "Southern Ocean", "The Atlantic Ocean", "The Arctic"), 3);
        Question question15 = new Question("What color eyes do most humans have?",
                Arrays.asList("Brown", "Green", "Blue", "Gray"), 0);
        Question question16 = new Question("What is the lowest army rank of a US soldier?",
                Arrays.asList("Corporal", "Sergeant", "Private", "Specialist"), 2);
        Question question17 = new Question("Which country produces the most coffee in the world?",
                Arrays.asList("Brazil", "Vietnam", "Columbia", "Indonesia"), 0);
        Question question18 = new Question("What is the common name for dried plums?",
                Arrays.asList("Raisins", "Prunes", "Craisins", "dates"), 1);
        Question question19 = new Question("What type of music has been shown to help plants grow better and faster?",
                Arrays.asList("Pop", "Jazz", "Classical", "Soul"), 2);
        Question question20 = new Question("Power outages in the US are mostly caused by what?",
                Arrays.asList("Hurricanes", "Blizzards", "Tornadoes", "Squirrels"), 3);

        return new QuestionBank(Arrays.asList(question1, question2, question3, question4, question5,
                question6, question7, question8, question9, question10, question11, question12,
                question13, question14, question15, question16, question17, question18, question19, question20));
    }


    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showInfoDialog(MainActivity.this,
                getString(R.string.info_title), getString(R.string.rules)));
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
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();

        // save the settings (Show Errors and Use AutoSave)
        saveSettingsToSharedPrefs(editor);
//
//        // if autoSave is on then save the board
        saveGameAndBoardToSharedPrefsIfAutoSaveIsOn(editor);

        // apply the changes to the XML file in the device's storage
        editor.apply();
    }

    private void saveGameAndBoardToSharedPrefsIfAutoSaveIsOn(SharedPreferences.Editor editor) {
        //mKeyGame, individual three ints
        editor.putString("Game", getJSONFromQuestionBank(mQuestionBank));
        editor.putInt(getString(R.string.score_save_state), score);
        editor.putInt(getString(R.string.total_games_save_state), totalGamesPlayed);
        editor.putInt(getString(R.string.games_won_save_state), gamesWon);
        editor.putInt(getString(R.string.games_lost_save_state), gamesLost);
    }

    private void saveSettingsToSharedPrefs(SharedPreferences.Editor editor) {
        // save "autoSave" preference
        editor.putBoolean(mKeyAutoSave, mPrefUseAutoSave);
    }

    private void restoreAppSettingsFromPrefs() {
        // Since this is for reading only, no editor is needed unlike in saveRestoreState
        SharedPreferences preferences = getDefaultSharedPreferences(this);

        // restore AutoSave preference value
        mPrefUseAutoSave = preferences.getBoolean(mKeyAutoSave, true);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_toggle_auto_save).setChecked(mPrefUseAutoSave);
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
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
            case R.id.reset_stats:
                resetStats();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void resetStats() {
        mQuestionBank.resetStats();
        //reset stats activity
    }

    private void showStatistics() {
        Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
        intent.putExtra("Game", mQuestionBank.getJSONFromString());
        startActivity(intent);
    }


    @SuppressLint("SetTextI18n")
    private void startNewGame() {
        enableButtons();
        score = 0;
        scoreTextView.setText(getString(R.string.updated_score) + score);
        mQuestionBank = createQuestionBank();
        currentQuestion = mQuestionBank.getNextQuestion();
        this.showQuestion(currentQuestion);

    }

    private void enableButtons() {
        option1Button.setEnabled(true);
        option2Button.setEnabled(true);
        option3Button.setEnabled(true);
        option4Button.setEnabled(true);
    }


    private void showAbout() {
        showInfoDialog(this, "Trivia App", "Welcome to our Trivia App!\n" +
                "Made by Shira Alter and Jennifer Komendant for Android Programming Project Fall 2020.");
    }

    private void toggleMenuItem(MenuItem item) {
        item.setChecked(!item.isChecked());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /*
        SAVE STATE
         */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Game", getJSONFromQuestionBank(mQuestionBank));
        outState.putInt(getString(R.string.score_save_state), score);
        outState.putInt(getString(R.string.total_games_save_state), totalGamesPlayed);
        outState.putInt(getString(R.string.games_won_save_state), gamesWon);
        outState.putInt(getString(R.string.games_lost_save_state), gamesLost);
    }
}

