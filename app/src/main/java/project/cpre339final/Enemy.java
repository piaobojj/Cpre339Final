package project.cpre339final;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Enemy class: while the player playing the game, the score increase the difficultly of the game increase.
 * Enemy speed go faster and amounts of enemy increase too.
 */
public class Enemy extends MovingObject{

    // Graphical objects handled separately from the memory bitmap, aka animation
    private Bitmap bitmap; //the animation sequence know as sprite sheet
    //the speed of enemy go
    private int speedOfEnemy;
    //randomly appear on the screen for enemy
    private Random rand = new Random();
    //Animation
    private SpriteAnimation animation = new SpriteAnimation(); //give the object we create some animation
    //get score
    private int gameScore;

    public Enemy(Bitmap res, int x_coordinate, int y_coordinate, int spriteWidth, int spriteHeight, int frameNr, int score){

        //begin position for x and y coordinate
        x =  x_coordinate;
        y =  y_coordinate;
        //the sprite of width and height
        width = spriteWidth;
        height = spriteHeight;
        //game score tracking
        gameScore = score;

        //range of speed for enemy move @ adjustable
        speedOfEnemy = 6 + (int) (rand.nextDouble() * gameScore / 40);

        //cap the missile speed
        if (speedOfEnemy > 50 ) {
            speedOfEnemy = 50;
        }

        //the animation sequence know as sprite sheet
        Bitmap [] imageOfEnemy = new Bitmap[frameNr];
        bitmap = res;
        //sign in each element of the enemy array image to the bitmap array.
        for(int i = 0; i<imageOfEnemy.length;i++){
            imageOfEnemy[i] = Bitmap.createBitmap(bitmap,0,i*height, width, height);
        }
        animation.setFrames(imageOfEnemy);
        //if enemy is faster the delay is going to be turn less so the enemy is going faster (look like)
        animation.setJumpDelay(100-speedOfEnemy);

    }

    /*Since our character going on right
        * The enemy should go left to kill the hero
        * */
    public void updateEnemyState(){
        //going left on x coordinate
        x -= speedOfEnemy ;
        //update the animation state
        animation.updateState();
    }

    //Let's draw the enemy
    public void drawEnemy (Canvas canvas){
        try{
            canvas.drawBitmap(animation.getDrawingImage(),x,y,null);
        }catch (Exception e){}
    }

    //fix later
    @Override
    public int getWidth(){
        //offset slightly for more realistic collision detection
        return width - 15;
    }

}

