package project.cpre339final;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import static android.R.attr.x;
import static android.R.attr.y;

/*This is game character class */
public class dodgeMan extends MovingObject{

    // Graphical objects handled separately from the memory bitmap, aka animation
    private Bitmap bitmap; //the animation sequence know as sprite sheet
    //moving motion
    private boolean goingUp;   //acceleration upper down
    private boolean playing;  // is player playing ?
    //time tracking
    private int gameScore;   //tracking the game score
    private long beginTime; // whenever the game starter, calculate increment the score
    //Animation
    private SpriteAnimation animation = new SpriteAnimation(); //give the object we create some animation

    /*
        * Initial the character start position, draw the image sources into the frames, running the frames to do the animation
        * @params res:  png file containing all the frames
        * @params w: the width of the sprite to calculate the cut out rectangle
        * @params h: the height of the sprite
        * @params frameNr: number of frames in animation
        * */
    public dodgeMan(Bitmap res, int spriteWidth, int spriteHeight, int frameNr){

        x = 200; //begin position for x coordinate
        y = GameView.height/2; // begin position for y coordinate,
        vy = 0; //velocity value on the Y axis
        width = spriteWidth;
        height = spriteHeight;
        gameScore = 0; //set it to zero
        /*Here we create the bitmap array to store all different sprite from the character image*/
        //the image object array that take which is 3
        Bitmap[] characterImage  = new Bitmap[frameNr];
        bitmap = res;

        //take the image and pass into array
        for(int i=0;i<characterImage.length;i++){
            characterImage[i] = Bitmap.createBitmap(bitmap,i*width,0,width,height);
        }

        //pass this character image png array to animation
        animation.setFrames(characterImage);
        //create the delay: timestamp of the last frame change in the walking sequence.
        animation.setJumpDelay(10);
        //set the time to millis sceconds
        beginTime = System.currentTimeMillis();

    }

    //this is action up when we press up
    public void setGoingUp(boolean isUp) {
        goingUp = isUp;
    }

    /* Get the current score*/
    public int getScore(){
        return gameScore;
    }

    /* Determine the player status*/
    public boolean getPlayerPlaying(){
        return playing;
    }

    /* Setting the player play*/
    public void setPlayerPlaying(boolean isPlaying){
        playing = isPlaying;
    }

    /* Reset the velocity value on the Y axis*/
    public void resetVelY(){
        vy = 0;
    }

    /* Reset the game score*/
    public void resetScore(){
        gameScore = 0;
    }

    /*Update the character motion*/
    public void updateState(){
        //update the character animation so it moving itself
        animation.updateState();
        // how long it took to draw the game once to execute cycle in Millisecond
        long timeMillisDiff;
        timeMillisDiff = ( System.currentTimeMillis() - beginTime);
        //When the time over 200 millis, increment the score by one, and reset the time
        if(timeMillisDiff>200) {
            gameScore++;
            beginTime = System.currentTimeMillis();
        }
        //when player press down the screen
        if(goingUp){
            vy -= 1; //velocity value on the Y axis
        }else{
            //acceleration
            vy += 1;
        }

        //The speed of our charter moving
        if(vy>5)vy = 1;
        if(vy<-5)vy = -1;

        y += vy*2;

        //This is board gate so the charter won't go all the way down and fade out from the screen
        //so every time when y = 0 , it won't decrement
        if (y < 0) y = 0;
        if (y > (GameView.height - 100)) y = GameView.height - 100;

    }

    /*That is all. We set  as to where to draw the cut out image. It is at Charter position (X and Y set in the constructor).*/
    public void displayDraw(Canvas canvas){
        // where to draw the sprite
        canvas.drawBitmap(animation.getDrawingImage(),x,y,null);
    }


}

