package com.example.triviaapp;

import android.content.Intent;
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

import static lib.DialogUtils.showInfoDialog;

public class MainActivity extends AppCompatActivity {



    private TextView questionTextView, feedbackTextView;
    private Button option1Button, option2Button, option3Button, option4Button;
    private Question currentQuestion;
    private QuestionBank questionBank;
    private int score, gamesWon, totalGamesPlayed, gamesLost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupFields();
        setupToolbar();
        setupFAB();
        setButtonListeners();



    }

    private void setupFields() {
        questionTextView = findViewById(R.id.question);
        feedbackTextView = findViewById(R.id.feedbackMessage);
        option1Button = findViewById(R.id.option1);
        option2Button = findViewById(R.id.option2);
        option3Button = findViewById(R.id.option3);
        option4Button = findViewById(R.id.option4);

        questionBank = createQuestionBank();

        currentQuestion = questionBank.getNextQuestion();
        this.showQuestion(currentQuestion);

         score = 0;
         gamesWon = 0;
         totalGamesPlayed = 0;
         gamesLost = 0;

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
                showInfoDialog(this, "YOU WON!!!","GREAT JOB! You can restart the game or exit the application");
                disableButtons();
                gamesWon++;
                totalGamesPlayed++;
            }

        }
        else{
            feedbackTextView.setText(R.string.incorrect_answer_message);

            disableButtons();

            gamesLost++;
            totalGamesPlayed++;
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
        super.onStop();
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
              //  mPrefUseAutoSave = item.isChecked();
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
        gamesLost = 0;
        gamesWon = 0;
        totalGamesPlayed = 0;
        //reset stats activity
    }

    private void showStatistics() {
        Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
        startActivity(intent);
    }



        private void startNewGame() {
            enableButtons();

            questionBank = createQuestionBank();
            currentQuestion = questionBank.getNextQuestion();
            this.showQuestion(currentQuestion);

        }

    private void enableButtons() {
        option1Button.setEnabled(true);
        option2Button.setEnabled(true);
        option3Button.setEnabled(true);
        option4Button.setEnabled(true);
    }


    private void showAbout() {
        showInfoDialog(this, "Trivia App", "Welcome to our Trivia App!");
    }

    private void toggleMenuItem(MenuItem item) {
        item.setChecked(!item.isChecked());
    }





}

