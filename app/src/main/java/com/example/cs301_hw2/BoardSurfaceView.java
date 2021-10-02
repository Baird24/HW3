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
 * 2.  the seekbar on the side allows you to change teh size of the puzzle from 2X2 to 10X10
 * unlisted 1. Added a seekbar to change the size of each tile
 * unlisted 2. Added a seekbar to change teh space between each tile
 *
 */

package com.example.cs301_hw2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.Collections;

public class BoardSurfaceView extends SurfaceView implements View.OnTouchListener, View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {


    //declare instance variables
    private int rectDim;
    private int rectSize;
    private double spacing;

    private Paint bluePaint;
    private Paint redPaint;
    private Paint greenPaint;

    private ArrayList<Integer> tiles;

    private boolean down;
    private int blankSpace;
    private boolean solved;

    /**
     * Constructor
     *
     * Initializes the variables that will be used and give a base state
     */
    public BoardSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //enable to draw
        setWillNotDraw(false);

        //set base rectangle values that can be adjusted to change the rectangles
        rectDim = 4;
        rectSize = 150;
        spacing = 0.25;

        //set paints
        bluePaint = new Paint();
        bluePaint.setColor(Color.BLUE);
        redPaint = new Paint();
        redPaint.setColor(Color.RED);
        greenPaint = new Paint();
        greenPaint.setColor(Color.GREEN);

        //set redpaint for writing
        redPaint.setTextSize(40f);

        //assign the tiles int oan arraylist
        tiles = new ArrayList<Integer>(rectDim* rectDim);

        //assign tiles
        for (int i = 0; i < (rectDim * rectDim); i++){
            tiles.add(i);
        }

        //shuffle tiles
        Collections.shuffle(tiles);

        //set tiles up as win for testing
        /*
        for (int i = 0; i < 15; i++){
            Collections.swap(tiles, i, i+1);
        }
        */

        //set variables for later use
        down = false;
        blankSpace = tiles.indexOf(0);
        solved = false;
    }

    /**
     * OnDraw draws the screen's surfaceview to the current state
     *
     * @param can
     */
    protected void onDraw (Canvas can){
        //if it is solved make the background green
        if(solved){
            can.drawRect(0,0, getWidth(), getHeight(), greenPaint);
        }


        //draw Board
        for (int i = 0; i < rectDim; i++){
            for (int j = 0; j < rectDim; j++){
                //uses following paterns:
                //left top right bottom paint
                //text: text, x, y, paint

                //Draw each blue tile
                can.drawRect((j * rectSize), (i * rectSize),
                        (int)((j + 1) * rectSize - (rectSize * spacing)),
                        (int)((i + 1) * rectSize - (rectSize * spacing)), bluePaint);
                if((tiles.get((rectDim * i) + j)) != 0) {
                    //draw each number on  hte tiles except 0
                    can.drawText((tiles.get((rectDim * i) + j)).toString(),
                            (int) ((j * rectSize) + ((rectSize - (rectSize * spacing)) * 0.35)),
                            (int) ((i * rectSize) + ((rectSize - (rectSize * spacing)) * 0.65)), redPaint);
                }
            }
        }


    }


    /**
     * randomizes the board when the reset button is clicked
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        //if the reset button is pushed
        if (view.getId() == R.id.resetButton){
            //shuffle the tiles and make it not solved
            Collections.shuffle(tiles);
            solved = false;
            invalidate();
        }
    }


    /**
     * Registers touch events on the surfaceview to move the tiles
     *
     * @param view
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        //find coords of the touch and the
        int clickX = (int)event.getX();
        int clickY = (int)event.getY();

        //find coordinates and location of blank space:
        blankSpace = tiles.indexOf(0);
        int blankY = blankSpace / rectDim;
        int blankX = blankSpace % rectDim;

        //if touched down
        if (!down){
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN){
                //if the click is within the blank rect
                if (inRect(clickX, clickY, blankX, blankY)){

                    //the touch is used and is currently down
                    down = true;
                    return true;
                }
            }
        }

        //if the touch is already down and is now letting go
        if (down){
            if (event.getActionMasked() == MotionEvent.ACTION_UP){
                //pressed the button down
                down = false;

                //find the new values for the adjacent tiles
                int upX = blankX;
                int upY = blankY - 1;

                int downX = blankX;
                int downY = blankY + 1;

                int leftX = blankX - 1;
                int leftY = blankY;

                int rightX = blankX + 1;
                int rightY = blankY;


                //check the above square
                if (upY > -1 && inRect(clickX, clickY, upX, upY)){
                    Collections.swap(tiles, blankSpace, blankSpace - rectDim);
                }

                //check the below square
                else if (downY < rectDim && inRect(clickX, clickY, downX, downY)){
                    Collections.swap(tiles, blankSpace, blankSpace + rectDim);
                }

                //check the left square
                else if (leftX > -1 && inRect(clickX, clickY, leftX, leftY)){
                    Collections.swap(tiles, blankSpace, blankSpace - 1);
                }

                //check the right square
                else if (rightX < rectDim && inRect(clickX, clickY, rightX, rightY)){
                    Collections.swap(tiles, blankSpace, blankSpace + 1);
                }

                //check if the puzzle is solved
                solved = isSolved();
                invalidate();

                //used action
                return true;
            }
        }
        //did not use action
        return false;
    }

    /**
     * checks if a given touch is within the rectangle of a given position
     *
     * @param clickX x coord of the click
     * @param clickY y coord of the click
     * @param blankX x position of the blank space
     * @param blankY y position of the blank space
     * @return if the click in within the
     */
    private boolean inRect(int clickX, int clickY, int blankX, int blankY){
        if (//in x bounds
                clickX >= (blankX * rectSize) &&
                        clickX <= ((int)((blankX + 1) * rectSize - (rectSize * spacing))) &&
                //in y bounds
                clickY >= (blankY * rectSize) &&
                    clickY <= ((int)((blankY + 1) * rectSize - (rectSize * spacing)))
        ){
            //is in the rect
            return true;
        }
        //is not in the rect
        return false;
    }


    /**
     * Checks if the game ahs been solved
     *
     * @return if the game has been successfully completed
     */
    private boolean isSolved(){
        for(int i = 1; i < ((rectDim * rectDim) - 1); i++) {
            //if each the numbers are in order from 1 to n from the first to the second to last element
            if ((tiles.get(i) != i + 1)) {
                //is not in order
                return false;
            }
        }
        //is in order
        return true;
    }


    /**
     * Updates the game when the seekbar is moved
     *
     * @param seekBar
     * @param progress
     * @param b
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if(seekBar.getId() == R.id.numberSquaresBar) {
            //set new dimensions and clear the board
            rectDim = progress;
            tiles.clear();
            //add the new numbers in
            for (int i = 0; i < (rectDim * rectDim); i++) {
                tiles.add(i);
            }
            //shuffle the cards and reset the board
            Collections.shuffle(tiles);
            solved = false;
            invalidate();
        }


        else if (seekBar.getId() == R.id.rectSize){
            //changes the size of each rectangle
            rectSize = progress;
            invalidate();
        }
        else if (seekBar.getId() == R.id.spaceSize){
            //changes the space between each rectangle
            spacing = (progress / 100.0);
            invalidate();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {     }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {      }
}
