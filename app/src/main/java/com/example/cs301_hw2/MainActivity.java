/**Author: Bruce Baird
 * Date: Oct. 1, 2021
 * Project: Homework #2, 15 Squares puzzle
 *
 * Description: code creates a game where you try to solve th e15 squares puzzle by
 * moving tiles around into the correct order by only exchanging tiles neigboring the empty tile
 * with said empty tile
 *
 * Extensions:
 * 1.  you drag the empty tile onto an adjacent tile instead of using two clicks
 * 2.  the seekbar on the side allows you to change teh size of the puzzle from 2X2 to 7X7
 *
 */

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

        //Give objects on screen variable names
        SeekBar numberSquaresBar = (SeekBar) findViewById(R.id.numberSquaresBar);
        Button resetButton = (Button) findViewById(R.id.resetButton);
        BoardSurfaceView boardView = (BoardSurfaceView) findViewById(R.id.boardSurfaceView);

        //set listeners for the different elements
        boardView.setOnTouchListener(boardView);
        resetButton.setOnClickListener(boardView);
        numberSquaresBar.setOnSeekBarChangeListener(boardView);
    }
}