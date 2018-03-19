package edu.guillera19up.pong_guillera19;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

/**
 * This class implements the Animator Interface and the OnClickListener. It contains the
 * majority of the code for this Pong game. The coordinates used in this class were obtained
 * through guess and check and calculation methods by inputting numbers, running the program,
 * and then adjusting the coordinates as needed.
 *
 * @Author: Avery Guillermo
 * Created by Avery Guillermo on 3/17/18.
 *
 */
public class AnimatorImplementation implements Animator, View.OnClickListener {

    //instance variables
    ArrayList<Ball> balls; //array list to hold the balls
    private int paddleSize; // 0 means beginner, 1 means intermediate, 2 means expert
    private int topOfPaddleCoord; // the top of the PaddleCoord
    private int bottomOfPaddleCoord; // the bottom of the PaddleCoord

    /**
     * This is the constructor for this class. It initializes the balls ArrayList and
     * adds a ball into the ArrayList, since one of the assignment specifications is that when the
     * app starts, a ball should shoot out in a random direction and random speed
     *
     */
    public AnimatorImplementation(){
        balls = new ArrayList<>();
        balls.add(new Ball());
    }

    /**
     * Interval between animation frames: .05 seconds (i.e., about 50 times
     * per second).
     *
     * @return the time interval between frames, in milliseconds.
     */
    @Override
    public int interval() {
        return 20;
    }


    /**
     * The background color: black to match the hot pink walls.
     *
     * @return the background color onto which we will draw the image.
     */
    @Override
    public int backgroundColor() {
        return Color.rgb(0, 0, 0);
    }


    /**
     * This method is called once every clock tick to draw the next animation-frame. This updates
     * the animation's data to reflect the passage of time. In this method, the three walls are
     * drawn, the paddle is drawn depending on the Paddle Size difficulty, and the balls are
     * repeatedly drawn.
     *
     * @param g the graphics object on which to draw
     */
    @Override
    public void tick(Canvas g) {

        //draw the walls
        Paint pinkPaint = new Paint();
        pinkPaint.setColor(Color.rgb(255,105,180));
        //draw the first top wall
        g.drawRect(160f, 40f, 1908f, 100f, pinkPaint);
        //draw the second bottom wall
        g.drawRect(160f, 961f, 1908f, 1021f, pinkPaint);
        //draw the right side wall
        g.drawRect(1848f, 100f, 1908f, 1021f, pinkPaint);

        //draw the paddle depending on the difficulty that the user selects
        if(paddleSize == 0) {
            drawBeginnerPaddle(g);
        }
        else if (paddleSize == 1) {
            drawIntermediatePaddle(g);
        }
        else if(paddleSize == 2){
            drawExpertPaddle(g);
        }

        //draw the pong balls by going through the array list and updating their positions
        for (Ball b : balls) {
            int xPos = (b.getxCount() * b.getVelocity());
            int yPos = (b.getyCount() * b.getVelocity());

            //check if the center of the ball touches the lower or upper wall
            if ((yPos > (961 - 10 - b.getRadius())) || (yPos < (100 + 10 + b.getRadius()))) {
                b.setGoYBackwards(!b.isGoYBackwards());
            }
            //check if the center of the ball touches the right wall
            if (xPos > 1848 - 10 - b.getRadius()) {
                b.setGoXBackwards(!b.isGoXBackwards());
            }

            //check if the center of the ball touches the left paddle
            if(xPos < (120 + 10 + b.getRadius()) && xPos >120+30) {
                if ((yPos <= bottomOfPaddleCoord) && (yPos >= topOfPaddleCoord)) {
                    b.setGoXBackwards(!b.isGoXBackwards());
                }
            }

            //check if the ball travels passed the paddle and off the screen
            if(xPos < 0) {
                balls.remove(b);
                break;
            }

            //update the ball positions depending on the direction they are traveling
            if (b.isGoXBackwards()) {
                b.setxCount((b.getxCount())-1);
            }
            else {
                b.setxCount((b.getxCount())+1);
            }
            if (b.isGoYBackwards()) {
                b.setyCount((b.getyCount())-1);
            }
            else {
                b.setyCount((b.getyCount())+1);
            }

            //Paint the balls on the canvas
            xPos = (b.getxCount() * b.getVelocity());
            yPos = (b.getyCount() * b.getVelocity());
            Paint redPaint = new Paint();
            redPaint.setColor(Color.RED);
            g.drawCircle(xPos, yPos, b.getRadius(), redPaint);
        }

    }

    /*
     * This is one of the required methods for implementing the View.OnClickListener. This class
     * changes the instance variables depending on which button was pressed.
     *
     * @param v: the button that was pressed
     */
    @Override
    public void onClick(View v) {
        Button buttonWasClicked = (Button) v;
        String buttonLabel = (String) buttonWasClicked.getText();

        //check which button was pressed and update the instance variable paddleSize accordingly
        if (buttonLabel.equalsIgnoreCase("Beginner")){
            this.paddleSize = 0;
        }
        else if (buttonLabel.equalsIgnoreCase("Intermediate")){
            this.paddleSize = 1;
        }
        else if (buttonLabel.equalsIgnoreCase("Expert")){
            this.paddleSize = 2;
        }
        else if (buttonLabel.equalsIgnoreCase(("Add Ball"))){
            balls.add(new Ball());
        }
    }

    /*
     * This is a helper method to help draw the paddle. This method draws the beginner paddle
     * by drawing a blue Trapezoid. The coordinates used were obtained by guess and check methods.
     *
     * @param g: The canvas on which to draw.
     */
    public void drawBeginnerPaddle(Canvas g){
        Paint bluePaint = new Paint();
        bluePaint.setColor(Color.rgb(0, 191,255));
        Path beginnerTrapezoid = new Path();
        beginnerTrapezoid.moveTo(80f, 260);
        beginnerTrapezoid.lineTo(120f, 230f);
        beginnerTrapezoid.lineTo(120f,831f);
        beginnerTrapezoid.lineTo(80f, 801f);
        g.drawPath(beginnerTrapezoid, bluePaint);
        this.topOfPaddleCoord = 230;
        this.bottomOfPaddleCoord = 831;
    }

    /*
    * This is a helper method to help draw the paddle. This method draws the intermediate paddle
     * by drawing a green Trapezoid. The coordinates used were obtained by guess and check methods.
    *
    * @param g: The canvas on which to draw.
    */
    public void drawIntermediatePaddle(Canvas g){
        Paint neonGreenPaint = new Paint();
        neonGreenPaint.setColor(Color.rgb(13, 253,85));
        Path IntermediateTrapezoid = new Path();
        IntermediateTrapezoid.moveTo(80f, 370f);
        IntermediateTrapezoid.lineTo(120f, 320f);
        IntermediateTrapezoid.lineTo(120f,741f);
        IntermediateTrapezoid.lineTo(80f, 691f);
        g.drawPath(IntermediateTrapezoid, neonGreenPaint);
        this.topOfPaddleCoord = 320;
        this.bottomOfPaddleCoord = 741;
    }

    /*
    * This is a helper method to help draw the paddle. This method draws the expert paddle
    * by drawing a purple Trapezoid. The coordinates used were obtained by guess and check methods.
    *
    * @param g: The canvas on which to draw.
    */
    public void drawExpertPaddle(Canvas g){
        Paint neonPurplePaint = new Paint();
        neonPurplePaint.setColor(Color.rgb(205,13,253));
        Path ExpertTrapezoid = new Path();
        ExpertTrapezoid.moveTo(80f, 470);
        ExpertTrapezoid.lineTo(120f, 440f);
        ExpertTrapezoid.lineTo(120f,621f);
        ExpertTrapezoid.lineTo(80f, 591f);
        g.drawPath(ExpertTrapezoid, neonPurplePaint);
        this.topOfPaddleCoord = 440;
        this.bottomOfPaddleCoord = 621;
    }


    /**
     * Tells that we never pause.
     *
     * @return indication of whether to pause
     */
    @Override
    public boolean doPause() {
        return false;
    }

    /**
     * Tells that we never stop the animation.
     *
     * @return indication of whether to quit.
     */
    @Override
    public boolean doQuit() {
        return false;
    }


    /**
     * This method is not used yet and will be used in Part B of this assignment.
     */
    @Override
    public void onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            //do nothing for part A of this assignment. Part B will use this method.
        }

    }

}
