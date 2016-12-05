# Cpre339Final
CPRE339_Final_Project
<br>By: Wen-Chi Hsu.
<br>Android Game application.
<br>Last Modified: 12/05/2016

### Goal:
   Expected to perform from high-level architecture design to detailed software development and/or implementation, and applying what we have learned into software development practice. 

### Idea:
   The game will be developed in the 2D scrolling style similar like Raiden Fighter https://www.youtube.com/watch?v=CsD-FNjPOmk , which the player is required to move it up and down, be able to obstacles the object and doget various kinds of enemies along the way.
   
### Doucmentation:
<ul>
  <li>Deliverable-2 (contains new updated C&C and module decomposition diagram)</li>
  <li>Deliverable-3 (contains new update class and usecas diagram)</li>
  <li>Demo-Video (Briefly describe the design implementation / code sample / actual game play)</li>
  <li>Screenshot (the actual game play overview)</li>
  <li>Handwrite script (chartecter and object design )</li>
</ul> 
### Software Architecture and Design Techniques Use:

Implement defensive programming
      TODO

Code smells
      TODO
      
Inter-processes/threads communication
      TODO
      
      ======
Design patterns <br>

  <p>   Those are the snip shop of the code for indicated the implementation of the Class Diagram design.
     Detail of src code: https://github.com/piaobojj/Cpre339Final/tree/master/app/src/main/java/project/cpre339final
     Detail of Class Diagram: https://github.com/piaobojj/Cpre339Final/blob/master/Deliverable-4/Cpre339%20Class%20Diagram(1).pdf </p>
      
    /* This class has extend abstract SurfaceView and has the class of MainGameThread and GameBackground object 
       which reference from the class UML diagram design. */  
    public class GameView extends SurfaceView implements SurfaceHolder.Callback{
       
       //reference variable
       private MainGameThread mainGameThread;
       //Some pictures can not be stretched on the drawable-nodpi, it will not be enlarged to the original size.
       private Resources res;

       /* Variable for Background ---------------------------------------------------------------*/
       //Set background image is 856X480 png image
       private Background background;
       public static final int width = 1223;
       public static final int height = 480;
       // Background moving scrolling speed
       public static final int scrollingSpeed = -6;
       
       <br>
       
       /*  This is the moving object class that 
        *  will hold image and the coordinates. */
     
      public abstract class MovingObject {

      /*Every single object will have x,y,width and height, so we need
       attributes coordinates of the object we made*/
       }
       
       /*To be able to re-create the moving animation, we called this sprite, we need to load up each frame as a separate image
      and display them at regular intervals one after each other. or load up the big image containing all the frames
      and use the methods provided by android cut them into a piece and display only the relevant frame.*/

      public class SpriteAnimation { *****For more detail **** }
       
       
     


### Game Screenshot:

####Menu Page

![alt text](https://github.com/piaobojj/Cpre339Final/blob/master/Screenshot/screen1.png "Logo Title Text 1")

####Game Start

![alt text](https://github.com/piaobojj/Cpre339Final/blob/master/Screenshot/screen2.png "Logo Title Text 1")

####Game Play - Avoid the object

![alt text](https://github.com/piaobojj/Cpre339Final/blob/master/Screenshot/screen3.png "Logo Title Text 1")

####Game Over

![alt text](https://github.com/piaobojj/Cpre339Final/blob/master/Screenshot/screen4.png "Logo Title Text 1")



