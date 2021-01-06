package com.example.triviaapp;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {
    private List<Question> mQuestionList;
    private int mNextQuestionIndex;
    private int mNumberOfGamesPlayed;
    private int mGamesWon;
    private int mGamesLost;

    public QuestionBank(List<Question> questionList){
        mQuestionList = new ArrayList<>(questionList);

        //shuffle the list
        Collections.shuffle(mQuestionList);

        mNextQuestionIndex = 0;
    }

    public Question getNextQuestion(){

        return mQuestionList.get(mNextQuestionIndex);
    }

    public void removeQuestion(Question question){
            mQuestionList.remove(question);

    }

    public Question getCurrentQuestion(){
        return mQuestionList.get(0);
    }

    public int getSize(){
        return mQuestionList.size();
    }

    public static QuestionBank getQuestionBankFromJSON(String json){
        Gson gson = new Gson();
        return new Gson ().fromJson (json, QuestionBank.class);
    }

    public static String getJSONFromQuestionBank(QuestionBank obj){
        return new Gson() .toJson(obj);
    }

    public  String getJSONFromString(){
        return new Gson ().toJson (this);
    }

    public int getNumberOfGamesPlayed() {
        return  mNumberOfGamesPlayed;
    }

    public void setmNumberOfGamesPlayed(int totalGamesPlayed){
        mNumberOfGamesPlayed = totalGamesPlayed;
    }

    public void resetStats(){
        mNumberOfGamesPlayed = 0;
        mGamesWon = 0;
        mGamesLost = 0;
    }

    public void setmNumberGamesWon(int gamesWon) {
        mGamesWon = gamesWon;
    }

    public int getmGamesWon() {
        return mGamesWon;
    }

    public void setmNumberGamesLost(int gamesLost) {
        mGamesLost = gamesLost;
    }

    public int getmGamesLost() {
        return mGamesLost;
    }
}
