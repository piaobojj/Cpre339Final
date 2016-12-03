package project.cpre339final;

import android.graphics.Bitmap;

/*To be able to re-create the moving animation, we called this sprite, we need to load up each frame as a separate image
and display them at regular intervals one after each other. or load up the big image containing all the frames
and use the methods provided by android cut them into a piece and display only the relevant frame.*/

public class SpriteAnimation {

    private Bitmap[] frames; // the animation sequence
    private int currentFrame; // the current frame
    private long beginTime; //frame start time
    private long jumpDelay; //lag time for frame, timestamp of the last frame change in the walking sequence.
    private boolean animationOnce; //for explosion just animation once

    /*Set png file containing all the frames.*/
    public void setFrames(Bitmap[] frames){

        this.frames = frames;//containing all the png file.
        currentFrame = 0; //init the frame
        beginTime = System.currentTimeMillis();
    }

    /*Set timestamp of the last frame change in the walking sequence.*/
    public void setJumpDelay (long delay){
        jumpDelay = delay;
    }

    public void setFrame(int i){currentFrame = i;}

    /* This is all magic animation come from
         * Every moving object will have an update method of their own as an animated object and object needs to look good.
         * Because the period of the game update cycle and moving object one might be (in this case is) different we pass the actual
         * game time as a variable so we know when we need to display the next frame.
         * For example the game runs very fast and the update is called every 30 milliseconds and we need to update the frame every 200ms.
         * */
    public void updateState(){
        // how long it took to draw the game once to execute cycle in Millisecond
        long timeMillisDiff = ( System.currentTimeMillis() - beginTime);
        //if lag time for frame of the last frame change in the walking sequence.
        if(timeMillisDiff>jumpDelay){
            //go to next frame
            currentFrame++;
            //set the begin time
            beginTime = System.currentTimeMillis();
        }
        //when the current frame is equal end the sequence
        if(currentFrame == frames.length){
            //inti the current frame
            currentFrame = 0;
            //only animation once
            animationOnce = true;
        }
    }

    /*All moving object is going to be drawing determined by this method*/
    public Bitmap getDrawingImage(){
        return frames[currentFrame];
    }

    public int getFrame(){return currentFrame;}
    public boolean playedOnce(){return  animationOnce;}

}

