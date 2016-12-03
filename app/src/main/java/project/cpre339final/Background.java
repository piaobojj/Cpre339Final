package project.cpre339final;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/*This is background class which will move right to left on the game*/
public class Background {

    private Bitmap image;
    private int x,y,dx; //dx = vector

    public Background(Bitmap res){
        image  = res;
        dx = GameView.scrollingSpeed;
    }

    public void backgroundUpdate(){

        x += dx;
        //so if background move out the screen, reset equal to zero
        if(x<-GameView.width){
            x=0;
        }
    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(image,x,y,null);
        //draw the second image in front of this image, so the scrolling looking continues
        //if part of image is out the screen draw after that
        if(x<0){
            canvas.drawBitmap(image,x+GameView.width,y,null);
        }
    }
}

