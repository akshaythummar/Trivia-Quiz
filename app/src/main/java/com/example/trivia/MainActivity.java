package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.data.AnswerListResponse;
import com.example.trivia.data.Repository;
import com.example.trivia.databinding.ActivityMainBinding;
import com.example.trivia.model.Question;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int currQueIndex = 0;
    List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        questionList = new Repository().getQuestion(questionArrayList -> {
            binding.tvQuestion.setText(questionArrayList.get(currQueIndex).getAnswer());
            binding.tvOutof.setText("Question : " + (currQueIndex+1) + "/" + questionList.size());
        });

        binding.btnNext.setOnClickListener(v -> {
            currQueIndex = (currQueIndex + 1) % questionList.size();
            updateQue();
        });
        binding.btnTrue.setOnClickListener(v -> {
            checkAns(true);
            updateQue();
        });
        binding.btnFalse.setOnClickListener(v -> {
            checkAns(false);
            updateQue();
        });

    }

    private void checkAns(boolean usrChoise) {
        boolean answer=questionList.get(currQueIndex).isAnswerTrue();
        int snackMsgId=0;
        if (answer==usrChoise){
            snackMsgId=R.string.correct;
            fadeAnimation();
        }
        else{
            snackMsgId=R.string.incorrect;
            shakeAnimation();
        }
        Snackbar.make(binding.cv, snackMsgId,Snackbar.LENGTH_SHORT).show();
    }

    private void updateCounter(ArrayList<Question> questionArrayList){
        binding.tvOutof.setText("Question : " + (currQueIndex+1) + "/" + questionList.size());
    }

    private void updateQue() {
        String question = questionList.get(currQueIndex).getAnswer();
        binding.tvQuestion.setText(question);
        updateCounter((ArrayList<Question>) questionList);

    }

    private void fadeAnimation(){
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cv.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.tvQuestion.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.tvQuestion.setTextColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        binding.cv.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.tvQuestion.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.tvQuestion.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}