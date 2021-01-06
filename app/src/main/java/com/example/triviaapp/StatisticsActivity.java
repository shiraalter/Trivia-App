package com.example.triviaapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity {

    private TextView tvDataGamesPlayed,
            tvDataPlayer1Wins, tvDataPlayer1Losses;

    private QuestionBank mQuestionBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setupToolbar();
        setupFAB();
        setUpViews();
        getIncomingData();
        processAndOutputIncomingData();
    }

    private void setUpViews() {
        tvDataGamesPlayed = findViewById(R.id.tv_data_games_played);
        tvDataPlayer1Wins = findViewById(R.id.tv_data_player1_wins);
        tvDataPlayer1Losses = findViewById(R.id.tv_data_player1_losses);
        }


    private void getIncomingData() {
        Intent intent = getIntent();
        String gameJSON = intent.getStringExtra("Game");
        mQuestionBank = QuestionBank.getQuestionBankFromJSON(gameJSON);
    }

    private void processAndOutputIncomingData() {
        int numberOfGamesPlayed = mQuestionBank.getNumberOfGamesPlayed();
        int numberOfGamesWon = mQuestionBank.getmGamesWon();
        int numberOfGamesLost = mQuestionBank.getmGamesLost();
        tvDataGamesPlayed.setText(String.valueOf(numberOfGamesPlayed));     // don't forget String.valueOf()
        tvDataPlayer1Wins.setText(String.valueOf(numberOfGamesWon));
        tvDataPlayer1Losses.setText(String.valueOf(numberOfGamesLost));
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

}