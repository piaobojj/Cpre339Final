package project.cpre339final;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

import static android.R.attr.x;
import static android.R.attr.y;


public class Enemy2 extends MovingObject{

    // Graphical objects handled separately from the memory bitmap, aka animation
    private Bitmap bitmap; //the animation sequence know as sprite sheet
    //the speed of enemy go
    private int speedOfEnemy2;
    //randomly appear on the screen for enemy
    private Random rand = new Random();
    //Animation
    private SpriteAnimation animation = new SpriteAnimation(); //give the object we create some animation
    //get score
    private int gameScore;

    public Enemy2(Bitmap res, int x_coordinate, int y_coordinate, int spriteWidth, int spriteHeight, int frameNr, int score){

        //begin position for x and y coordinate
        x =  x_coordinate;
        y =  y_coordinate;
        //the sprite of width and height
        width = spriteWidth;
        height = spriteHeight;
        //game score tracking
        gameScore = score;

        //range of speed for enemy move @ adjustable
        speedOfEnemy2 = 6 + (int) (rand.nextDouble() * gameScore / 40);

        //cap the missile speed
        if (speedOfEnemy2 > 50 ) {
            speedOfEnemy2 = 50;
        }

        //the animation sequence know as sprite sheet
        Bitmap [] imageOfEnemy2 = new Bitmap[frameNr];
        bitmap = res;
        //sign in each element of the enemy array image to the bitmap array.
        for(int i = 0; i<imageOfEnemy2.length;i++){
            imageOfEnemy2[i] = Bitmap.createBitmap(bitmap,0,0, width, height);
        }
        animation.setFrames(imageOfEnemy2);
        //if enemy is faster the delay is going to be turn less so the enemy is going faster (look like)
        animation.setJumpDelay(200-speedOfEnemy2);

    }

    /*Since our character going on right
        * The enemy should go left to kill the hero
        * */
    public void updateEnemy2State(){
        //going left on x coordinate
        x -= speedOfEnemy2 ;
        //update the animation state
        animation.updateState();
    }

    //Let's draw the enemy
    public void drawEnemy2 (Canvas canvas){
        try{
            canvas.drawBitmap(animation.getDrawingImage(),x,y,null);
        }catch (Exception e){}
    }

    //fix later
    @Override
    public int getWidth(){
        //offset slightly for more realistic collision detection
        return width - 10;
    }

}


