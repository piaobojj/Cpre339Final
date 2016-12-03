package project.cpre339final;

import android.graphics.Canvas;
import android.support.annotation.MainThread;
import android.util.Log;
import android.view.SurfaceHolder;

/*
* Actual game loop of the game
* */
public class MainGameThread extends Thread {

    private boolean running;    //flag to hold the game state
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private final static int FPS = 30;       // Frames Per Second
    private final static int FRAME_PERIOD = 1000 / FPS; //Frames periods
    public static Canvas canvas;
    //
    private static final String TAG = MainGameThread.class.getSimpleName();

    /*
         * Exit the application when user touch the lower part of the screen.
         * If user touch it anywhere else just log the coordinates.
         * Declared the game view  and surfaceHolder variables and a constructor taking the instances as parameters.
         * Detected lock the surface when we draw and that can be done through when call back the surface Holder only.
         * */
    public MainGameThread(SurfaceHolder surfaceHolder, GameView gameView){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    /*
        * Set the loop running if true.
        * */
    public void setRunning(boolean running){
        this.running = running;

    }

    /*
        * Heart beat of the game, it keep executing something until signal it to dei.
        * It overrides the run() method and while the running flag is set to true it does an infinite loop.
        * */
    @Override
    public void run(){

        long beginTime;     // the time when the cycle begun
        long timeMillisDiff;      // how long it took to draw the game once to execute cycle in Millisecond
        int waitTime = 0;      // ms to sleep (<0 if we're behind)
        int framesJump;  // number of frames being skipped


        while (running) {

            beginTime = System.currentTimeMillis();
            canvas = null;
            //try locking the canvas for pixel editing
            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){

                    framesJump = 0;
                    this.gameView.updateState();  // update game state
                    this.gameView.displayDraw(canvas);  // render state to the screen, draws the canvas on the panel
                    timeMillisDiff = System.currentTimeMillis() - beginTime; //calculate how long did it take to draw
                    waitTime = (int)(FRAME_PERIOD - timeMillisDiff); //calculate the sleep time

                    //if waitTime is greater than zero try to send a thread for short period of time, battery saving tips
                    if(waitTime > 0){
                        try{
                            MainGameThread.sleep(waitTime);
                        }catch (InterruptedException e){}
                    }

                    //if wait Time is less than frame skip try to catch the update without drawing
                    // 5 is max of frames has skipped
                    while (waitTime < 0 && framesJump < 5){
                        this.gameView.updateState();
                        //check the next frames if skipped
                        waitTime += FRAME_PERIOD;
                        framesJump ++;
                    }

                }
            }finally {

                if(canvas != null){
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }


    }

}

