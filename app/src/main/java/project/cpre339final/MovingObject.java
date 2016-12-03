package project.cpre339final;

import android.graphics.Rect;

/*
*  Create an object that will hold image and the coordinates.
* */
public abstract class MovingObject {

    /*Every single object will have x,y,width and height, so we need
       attributes coordinates of the object we made*/

    protected int x; //the X coordinate of the object
    protected int y; //the Y coordinate of the object
    protected int width;// the width of the sprite to calculate the cut out rectangle
    protected int height;// the height of the sprite

    protected int vy; //velocity value on the Y axis


    public int getXDirection(){
        return x;
    }

    public void setXDirection(int x){
        this.x = x;
    }

    public int getYDirection(){
        return y;
    }

    public void setYDirection(int y){
        this.y = y;
    }


    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    //check the collision for object since each object is a rectangle, if so the collision is happen
    public Rect getSourceRectangle(){
        // the rectangle to be drawn from the animation bitmap
        return new Rect(x,y,x+width,y+height);
    }

}

