package project.cpre339final;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * This is explosion class when dodge man kill by enemies
 * The animation of explosion will appear
 */
public class Boom {

    // Graphical objects handled separately from the memory bitmap, aka animation
    private Bitmap bitmap; //the animation sequence know as sprite sheet
    //begin position for x and y coordinate
    private int x;
    private int y;
    private int width; // the width of the sprite to calculate the cut out rectangle
    private int height;// the height of the sprite
    private int row; //row for image
    private SpriteAnimation animation = new SpriteAnimation(); //give the object we create some animation


    public Boom(Bitmap res, int x_coordinate, int y_coordinate, int spriteWidth, int spriteHeight, int frameNr)
    {
        //begin position for x and y coordinate
        this.x = x_coordinate;
        this.y = y_coordinate;
        this.width = spriteWidth;
        this.height = spriteHeight;
        /*Here we create the bitmap array to store all different sprite from the explosion image*/
        Bitmap[] imageOfExplosion = new Bitmap[frameNr];
        bitmap = res;
        //take the image and pass into array, col and row at this time for image
        for(int i = 0; i<imageOfExplosion.length; i++)
        {
            if(i%5==0&&i>0)row++;
            imageOfExplosion[i] = Bitmap.createBitmap(bitmap, (i-(5*row))*width, row*height, width, height);
        }
        animation.setFrames(imageOfExplosion);
        animation.setJumpDelay(10);
    }

    //Draw the explosion but only animating once
    public void drawBoom(Canvas canvas)
    {
        //only animating once
        if(!animation.playedOnce())
        {
            canvas.drawBitmap(animation.getDrawingImage(),x,y,null);
        }

    }
    //Only update state  once
    public void updateBoomState()
    {
        if(!animation.playedOnce())
        {
            animation.updateState();
        }
    }
    public int getHeight(){return height;}
}

