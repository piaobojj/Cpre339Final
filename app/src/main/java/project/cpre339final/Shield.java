package project.cpre339final;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/*This is shield that prevent the first 3 hit*/
public class Shield extends MovingObject{

    //radius of shield
    public int rad;
    //Setting the X  and Y coordinate of the shield object
    public Shield(int x , int y){

        rad = 20;
        //passing the constructor
        super.x = x;
        super.y = y;
    }
    //set up the shield of speed
    public void updateState(){
        x -=10;
    }

    public void drawShield (Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
    }
}

