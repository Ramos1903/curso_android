package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView timerText;
    TextView sumText;
    TextView scoreText;
    TextView resultText;
    Button goButton;
    Button playAgainButton;
    Button btn0;
    Button btn1;
    Button btn2;
    Button btn3;
    CountDownTimer timer;
    ConstraintLayout playgameLayout;
    int numberOfQuestions;
    int score;
    ArrayList<Integer>answers = new ArrayList<Integer>();
    int locationOfCorrectAnswer;

    public void playGame(View view){
        goButton.setVisibility(View.GONE);
        playgameLayout.setVisibility(View.VISIBLE);
        playAgainButton.setVisibility(View.GONE);
        resultText.setText("");
    }
    public void playAgain(View view) {
        score = 0;
        numberOfQuestions = 0;
        timerText.setText("30 s");
        scoreText.setText(Integer.toString(score) + " / " + Integer.toString(numberOfQuestions));
        newQuest();
        playAgainButton.setVisibility(View.VISIBLE);
        resultText.setText("");
        timer.start();

    }
    public void chooseAnswer(View view) {
        if (Integer.toString(locationOfCorrectAnswer).equals(view.getTag().toString())) {
            resultText.setText("Correct !");
            score++;
        } else  {
            resultText.setText("Wrong :(");
        }
        numberOfQuestions++;
        scoreText.setText( Integer.toString(score )+ " / " + Integer.toString(numberOfQuestions));
        newQuest();

    }
    public void newQuest() {

        Random rand = new Random();

        int a = rand.nextInt(21);
        int b = rand.nextInt(21);
        sumText.setText(Integer.toString(a) + " + " + Integer.toString(b));

        locationOfCorrectAnswer = rand.nextInt(4);
        answers.clear();


        for (int i = 0; i<4; i++) {
            if (i == locationOfCorrectAnswer) {
                answers.add(a+b);
            } else {
                int wrongAnswer = rand.nextInt(41);

                while (wrongAnswer == a+b) {
                    wrongAnswer = rand.nextInt(41);
                }
                answers.add(wrongAnswer);
            }

        }
        btn0.setText(Integer.toString(answers.get(0)));
        btn1.setText(Integer.toString(answers.get(1)));
        btn2.setText(Integer.toString(answers.get(2)));
        btn3.setText(Integer.toString(answers.get(3)));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = findViewById(R.id.timerText);
        sumText = findViewById(R.id.sumText);
        scoreText = findViewById(R.id.scoreText);
        resultText = findViewById(R.id.resultText);
        goButton = findViewById(R.id.goButton);
        playAgainButton = findViewById(R.id.playAgainButton);
        btn0 = findViewById(R.id.button0);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        playgameLayout = findViewById(R.id.playGameLayout);

        newQuest();
        playgameLayout.setVisibility(View.INVISIBLE);
        goButton.setVisibility(View.VISIBLE);

        timer = new CountDownTimer(30100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                playAgainButton.setVisibility(View.VISIBLE);
                resultText.setText("Done!");

            }
        }.start();

    }
}