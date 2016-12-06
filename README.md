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

#### Code smells <br>

      When I started code the object class in the very beiging development periods, Classes usually start 
      small. But over time, they get bloated as the program grows and for easy testing I usually just put 
      all the require variable and method toghter. To resolve the long class and long method, 
      <br>
     Extract all the object class which need the animation and moving detecting. <br> Split to the abstract 
     class which we got two class <MovingObject Class> <SpriteAnimation>. 
     
     
      
      
#### Threads communication <br>
      
      /* This is the game view class, all the actual chemistry happens here so the
      * result of interaction with the View produces an image.*/
  
      public class GameView extends SurfaceView implements SurfaceHolder.Callback{

       //reference thread variable
       private MainGameThread mainGameThread;

        /*
        * To be able to "moving", need to use complex threads through the thread continues
        * to change the image of the coordinates and paste the pictures or image over and over again.
        * */
       public void surfaceCreated(SurfaceHolder holder){
       
        //create the game loop thread
        mainGameThread = new MainGameThread(getHolder(),this);

        //Set the running flag to true and we start up the thread
        mainGameThread.setRunning(true);
        mainGameThread.start();
       }
       
         /*
        * This method is called directly before the surface is destroyed.
        * It is not the place to set the running flag but in ensures that 
        * the thread shuts down cleanly.
        * Block the thread and wait for it to destroyed.
        * */
       public void surfaceDestroyed(SurfaceHolder holder){

           boolean retry = true;
           //to prevent the infinite loop we setup the counter
           int counter = 0;
           while(retry && counter < 1000){

               try {
                   counter ++;
                   mainGameThread.setRunning(false);
                   mainGameThread.join();
                   retry = false;
                   //Set null so garbage collector can pick up the object
                   mainGameThread = null;
               }catch(InterruptedException e) {e.printStackTrace();}
               // try again shutting down the thread
           }
       }
       
       
      }
      
      
#### Design patterns <br>

  <p>   Those are the snip shop of the code for indicated the implementation of the Class Diagram design.<br>
     Detail of src code: https://github.com/piaobojj/Cpre339Final/tree/master/app/src/main/java/project/cpre339final
     <br>Detail of Class Diagram: https://github.com/piaobojj/Cpre339Final/blob/master/Deliverable-4/Cpre339%20Class%20Diagram(1).pdf </p>
      
      
       <br>
       ----------------------------------Abstract Class------------------------------------------
       /*  This is the moving object class that 
        *  will hold image and the coordinates. */
     
      public abstract class MovingObject {

      /*Every single object will have x,y,width and height, so we need
       attributes coordinates of the object we made*/
       }
       
       /* Bitmap*/
       public bitmap class provide by java library
       
       -----------------------------------Animation Class----------------------------------------
       /*  This is the re-create the moving animation class that 
        *  load up each frame as a separate image. */

      public class SpriteAnimation {  }
      
      ------------------------------------Class that are implment and extends--------------------
       
       /*This is enemy character class */
      public class Enemy extends MovingObject{

         //the animation sequence know as sprite sheet
         private Bitmap bitmap;
         //give the object we create some animation
         private SpriteAnimation animation = new SpriteAnimation(); 
      
       }
       
       /*This is game character class */
      public class dodgeMan extends MovingObject{
      
         //the animation sequence know as sprite sheet
         private Bitmap bitmap;
         //give the object we create some animation
         private SpriteAnimation animation = new SpriteAnimation(); 
          
       }
       
       /*This is class control the expolsion  */
       public class Boom {
       
         //the animation sequence know as sprite sheet
         private Bitmap bitmap;
         //give the object we create some animation
         private SpriteAnimation animation = new SpriteAnimation(); 
       
       }
     
        -----------------------------------------------------------------------------------------

### Game Screenshot:

####Menu Page

![alt text](https://github.com/piaobojj/Cpre339Final/blob/master/Screenshot/screen1.png "Logo Title Text 1")

####Game Start

![alt text](https://github.com/piaobojj/Cpre339Final/blob/master/Screenshot/screen2.png "Logo Title Text 1")

####Game Play - Avoid the object

![alt text](https://github.com/piaobojj/Cpre339Final/blob/master/Screenshot/screen3.png "Logo Title Text 1")

####Game Over

![alt text](https://github.com/piaobojj/Cpre339Final/blob/master/Screenshot/screen4.png "Logo Title Text 1")



