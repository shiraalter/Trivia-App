package com.example.triviaapp;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {
    private List<Question> mQuestionList;
    private int mNextQuestionIndex;

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

    public String getJSONFromString(){
        return new Gson ().toJson (this);
    }



}
