package com.example.cs301_hw2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar numberSquaresBar = (SeekBar) findViewById(R.id.numberSquaresBar);
        Button resetButton = (Button) findViewById(R.id.resetButton);
        BoardSurfaceView boardView = (BoardSurfaceView) findViewById(R.id.boardSurfaceView);

        boardView.setOnTouchListener(boardView);
        resetButton.setOnClickListener(boardView);
        numberSquaresBar.setOnSeekBarChangeListener(boardView);
    }
}