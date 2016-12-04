package project.cpre339final;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.MediaPlayer;

/*
 * Activity as a table that holds a sheet of paper (the View) enabling us to draw something.
 * the actual chemistry happens on the paper so the
  * result of our interaction with the View produces an image.
* */

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

    /* Variable for Character -------------------------------------------------------------------*/
    // instantiated the character (Dodge Man)
    private dodgeMan dodge_man;
    // dodge man HP
    private int playerHealth = 3;

    /* Variable for enemies ----------------------------------------------------------------------*/
    // define the start time for enemies in the game, when should they show up
    private long enemiesBeginTime;
    private long enemies2BeginTime;
    private long healthShieldBeginTime;
    // instantiated the enemy since is going be a lot the enemies, we put into the array list
    private ArrayList<Enemy> enemies;
    private ArrayList<Enemy2> enemy2s;
    // instantiated the health
    private ArrayList<HealthShield> healthShields;
    // randomly pop enemy
    private Random rand = new Random();

    /*Variable for boom occurs ------------------------------------------------------------------*/
    //instantiated the explosion class
    private Boom boom;

    /*Variable for game setting ------------------------------------------------------------------*/
    //check if the new game is create
    private boolean createdNewGame;
    //check if the game is reset
    private boolean resetGame;
    // reset the beging time
    private long resetBeginTime;
    //remove the character when the game end
    private boolean removeCharacter;
    private boolean started;

    /*Variable used for recreating sound effect for game*/
    //hold the sound file
    private SoundPool sounds;
    //hold info to play boom sound
    private int boomSound;

    /*Variable background music */
    MediaPlayer bgMusic;

    /*Variable score track*/
    private int bestScore;
    private int scoreTracking;

    public GameView(Context context){
        super(context);
        /* Install a SurfaceHolder.Callback so we get notified when the
                    underlying surface is created and destroyed.
                    when the call back call we can interact the events
                    such as mouse down up or event key press
                */
        getHolder().addCallback(this);

        //make game mode setFocusable so it can handle events
        setFocusable(true);
        //inject the background music for the game
        bgMusic = MediaPlayer.create(this.getContext(), R.raw.bgmusic2);
        bgMusic.setLooping(true);
    }

    /*Create the new reset all the function*/
    public void newGameCreate() {

        scoreTracking = dodge_man.getScore()*4;
        //get the best score
        if( scoreTracking >  bestScore){
            bestScore = scoreTracking;
        }
        enemies.clear(); //remove enemies
        enemy2s.clear();//clear enemies 2
        healthShields.clear(); //clear
        playerHealth = 3; //full hp
        dodge_man.resetScore();
        dodge_man.resetVelY(); //reset the acceleration
        dodge_man.setYDirection(height / 2); //reset the position
        createdNewGame = true;
        removeCharacter = false; //so after certain time of period want out hero to come back
    }

    /*Handle the background music*/
    public void playBackgroundMusic(){
        bgMusic.start();
    }

    /*
        * To be able to "moving", need to use complex threads through the thread continues to change the image of the coordinates
        * and paste the pictures or image over and over again.
        * */
    @Override
    public void surfaceCreated(SurfaceHolder holder){

        res = getResources();
        //Instantiated  background image resources
        background = new Background(BitmapFactory.decodeResource(res, R.drawable.bg1));
        //Instantiated  character image resources
        dodge_man = new dodgeMan(BitmapFactory.decodeResource(res, R.drawable.dodgeman),80,66,4);
        //Instantiated  enemy image resources
        enemies = new ArrayList<Enemy>();
        enemy2s = new ArrayList<Enemy2>();
        //Instantiated  health image resources
        healthShields = new ArrayList<HealthShield>();
        //init the time
        enemiesBeginTime = System.currentTimeMillis();
        enemies2BeginTime = System.currentTimeMillis();
        healthShieldBeginTime = System.currentTimeMillis();
        //create the game loop thread
        mainGameThread = new MainGameThread(getHolder(),this);

        //Set the running flag to true and we start up the thread
        mainGameThread.setRunning(true);
        mainGameThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    /*
        * This method is called directly before the surface is destroyed.
        * It is not the place to set the running flag but in ensures that the thread shuts down cleanly.
        * Block the thread and wait for it to destroyed.
        * */
    @Override
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

    /*This is override method for touch event on the screen
        * Going to handle the listener on surface on the touch screen
        * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            //if this is the first time press down , which mean player is not playing yet
            if(!dodge_man.getPlayerPlaying() && createdNewGame && resetGame){
                //then play the game
                dodge_man.setPlayerPlaying(true);
                //the character is going to fly up
                dodge_man.setGoingUp(true);
                //else if the already on the game
            }if(dodge_man.getPlayerPlaying()){
                //the character is going to fly up
                dodge_man.setGoingUp(true);
                //if the game is not started, started the game
                if(!started)started = true;
                //don't reset while playing the game
                resetGame = false;
            }
            return true;
        }
        //when the player press is release the finger on the screen
        if(event.getAction() == MotionEvent.ACTION_UP){
            //character is not longer fly up
            dodge_man.setGoingUp(false);
            return true;
        }
        return super.onTouchEvent(event);

    }

    /*Checking two moving object is collision or not
        * Using intersects method of Rect holds four integer coordinates for a rectangle
        * Check each position top,down,right, left.
        *  */
    public boolean collision (MovingObject a, MovingObject b){
        if(Rect.intersects(a.getSourceRectangle(), b.getSourceRectangle())){
            return true;
        }
        return false;
    }



    /*updates the state of every object in the game*/
    public void updateState() {
        //so if dodge_man is playing by user then started moving the background and character
        if(dodge_man.getPlayerPlaying()) {
            //play the background music
            playBackgroundMusic();
            background.backgroundUpdate();
            dodge_man.updateState();

            // how long it took to draw the enemy once to execute cycle in Millisecond  ------------------------------------------------------------------------------------------
            long timeMillisDiffEnemy;
            timeMillisDiffEnemy = ( System.currentTimeMillis() - enemiesBeginTime);
            //as the score goes higher and higher the speed of enemy go faster and faster, less then delay time
            if(timeMillisDiffEnemy > (1500 - dodge_man.getScore() / 4)){

                enemies.add(new Enemy(BitmapFactory.decodeResource(getResources(),R.drawable.fishenemy),
                        width + 10,(int)(rand.nextDouble()*(height)-30), 55, 25, 10, dodge_man.getScore()));

                //rest enemies timer
                enemiesBeginTime = System.currentTimeMillis();
            }

            // how long it took to draw the enemy once to execute cycle in Millisecond  ------------------------------------------------------------------------------------------
            long timeMillisDiffEnemy2 = ( System.currentTimeMillis() - enemies2BeginTime);
            //as the score goes higher and higher the speed of enemy go faster and faster, less then delay time
            if(timeMillisDiffEnemy2 > (4000 - dodge_man.getScore() / 4)){

                enemy2s.add(new Enemy2(BitmapFactory.decodeResource(getResources(),R.drawable.space),
                        width + 5,(int)(rand.nextDouble()*(height)), 165, 40, dodge_man.getScore(),1));
                //rest enemies timer
                enemies2BeginTime = System.currentTimeMillis();
            }

            // how long it took to draw the health  once to execute cycle in Millisecond  -------------------------------HEALTH------------------------------------------
            long timeMillisDiffHealth = ( System.currentTimeMillis() - healthShieldBeginTime);
            if(timeMillisDiffHealth > (9000 - dodge_man.getScore() / 4)){

                healthShields.add(new HealthShield(BitmapFactory.decodeResource(getResources(), R.drawable.health),
                        width + 20, (int) (rand.nextDouble() * (height)), 53, 40, dodge_man.getScore(), 1));
                //rest health timer
                healthShieldBeginTime = System.currentTimeMillis();
            }

            //loop through every enemies and check collision and remove --------------------------------------------------------------------------------------------------------------
            for(int i = 0; i<enemies.size();i++){

                enemies.get(i).updateEnemyState();
                //if the dodge_man hit the enemies which mean damage by enemies, health level going down 1
                if(collision(enemies.get(i), dodge_man)){
                    //remove enemies
                    enemies.remove(i);
                    //health level going down 1
                    playerHealth--;
                    //if health go down than 1 player stop
                    if(playerHealth<1) {
                        //game over stop playing
                        sounds.play(boomSound, 1.0f, 1.0f, 0, 0, 1.5f);
                        dodge_man.setPlayerPlaying(false);
                    }
                    break;
                }
                //so if enemies is way off the screen remove it
                if (enemies.get(i).getXDirection() <- 100){
                    enemies.remove(i);
                    break;
                }
            }

            //loop through every enemies and check collision and remove --------------------------------------------------------------------------------------------------------------
            for(int i = 0; i<enemy2s.size();i++){

                enemy2s.get(i).updateEnemy2State();
                //if the dodge_man hit the enemies which mean damage by enemies, health level going down 1
                if(collision(enemy2s.get(i), dodge_man)){
                    //remove enemies
                    enemy2s.remove(i);
                    //health level going down 2
                    playerHealth = playerHealth - 2;
                    //if health go down than 1 player stop
                    if(playerHealth<1) {
                        //game over stop playing
                        sounds.play(boomSound, 1.0f, 1.0f, 0, 0, 1.5f);
                        dodge_man.setPlayerPlaying(false);
                    }
                    break;
                }
                //so if enemies is way off the screen remove it
                if (enemy2s.get(i).getXDirection() <- 100){
                    enemy2s.remove(i);
                    break;
                }
            }

            //loop through every health and check collision and remove -------------------------------------HEALTH---------------------------------------------------
            for(int i = 0; i<healthShields.size();i++){

                healthShields.get(i).updateHealthState();
                //if the dodge_man hit the enemies which mean damage by enemies, health level going down 1
                if(collision(healthShields.get(i), dodge_man)){
                    //remove enemies
                    healthShields.remove(i);
                    //if health go down than 1 player stop
                    if(playerHealth<= 2 && playerHealth != 3) {
                        //health level going down 2
                        playerHealth ++;
                    }
                    break;
                }
                //so if health is way off the screen remove it
                if (healthShields.get(i).getXDirection() <- 100){
                    healthShields.remove(i);
                    break;
                }
            }

        }else{
            //Otherwise,  if the player is not playing
            dodge_man.resetVelY();
            //if the reset is not reset then reset ....
            if(!resetGame){
                createdNewGame = false;
                //reset the time
                resetBeginTime = System.currentTimeMillis();
                //hide the character when the game end
                removeCharacter = true;
                resetGame = true;
                //explosion happen here BOOM BOOM BOOM !! ------------------------------------------------------------------------------------!!
                boom = new Boom(BitmapFactory.decodeResource(getResources(),R.drawable.boom),
                        dodge_man.getXDirection(),
                        dodge_man.getYDirection()-30, 100,100, 25);
                sounds = new SoundPool(10,AudioManager.STREAM_MUSIC,0);
                boomSound = sounds.load(getContext(), R.raw.boom,1);
            }
            //update the boom
            boom.updateBoomState();

            // how long it took to remove the object once to execute cycle in Millisecond  --------------------Game  Over Wait  Time----------------------------
            long timeMillisDiffGameOver;
            timeMillisDiffGameOver = ( System.currentTimeMillis() - resetBeginTime);
            //how long is this game is going to play after 2800 crete the new game
            if(timeMillisDiffGameOver > 2800 && !createdNewGame){
                newGameCreate();
            }
        }
    }

    /*renders the objects into an image which is draw it displayed onto the screen.*/
    public void displayDraw (Canvas canvas){

        //To make the background scale all the way (fit into the screen)
        final float scaleFactorX = getWidth()/(width*1.f);
        final float scaleFactorY = getHeight() / (height*1.f);
        if(canvas!=null) {
            //create the save state before it scale
            final int saveState = canvas.save();
            //then scale
            canvas.scale(scaleFactorX, scaleFactorY);
            //after scale the background we draw the background
            background.draw(canvas);
            //draw our hero and it only draw when the removeCharacter is false
            if(!removeCharacter) {
                dodge_man.displayDraw(canvas);
            }
            //draw the enemy
            for(Enemy e: enemies){
                e.drawEnemy(canvas);
            }
            for(Enemy2 e2: enemy2s){
                e2.drawEnemy2(canvas);
            }
            //draw the health
            for(HealthShield h: healthShields){
                h.drawHealth(canvas);
            }
            //health bar
            drawHealthBar(canvas);
            //draw text
            drawText(canvas);

            //draw the boom when collision happen
            if(started){
                boom.drawBoom(canvas);
            }
            canvas.restoreToCount(saveState);
        }

    }

    //Create the Health Bar
    public void drawHealthBar (Canvas canvas){
        //create appear when player playing
        Paint paint = new Paint();
        Paint paint1 = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        //outer circle
        paint1.setColor(Color.BLUE);
        paint1.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width - 600, height - 40, 31, paint1);
        canvas.drawCircle(width - 600, height - 40 , 32, paint1);
        //Determine the hp
        if(playerHealth == 2){
            canvas.drawCircle(width - 600, height - 40 , 20, paint);
            paint.setTextSize(18);
            paint.setColor(Color.BLACK);
            canvas.drawText("2", width - 605, height - 42, paint);
        }else if (playerHealth == 1){
            canvas.drawCircle(width - 600, height - 40 , 10, paint);
            paint.setTextSize(18);
            paint.setColor(Color.BLACK);
            canvas.drawText("1", width - 605, height - 42, paint);
        }else if (playerHealth == 0){
            canvas.drawCircle(width - 600, height - 40 , 0, paint);
            paint.setTextSize(18);
            paint.setColor(Color.BLACK);
            canvas.drawText("0", width - 605, height - 42, paint);
        }else if(playerHealth == 3){
            canvas.drawCircle(width - 600, height - 40, 30, paint);
            paint.setTextSize(18);
            paint.setColor(Color.BLACK);
            canvas.drawText("3", width - 605, height - 42, paint);
        }
        paint.setTextSize(18);
        paint.setColor(Color.BLACK);
        canvas.drawText("HP", width - 609, height - 28, paint);

    }


    //create the start menu
    public void drawText (Canvas canvas){

        //create appear when player playing
        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setStrokeMiter(10);
        paint.setTextSize(35);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC));
        canvas.drawText("SCORE: " + (dodge_man.getScore() * 4), 10, height - 12, paint);
        canvas.drawText("BEST SCORE: " + bestScore, width - 1000, height - 12, paint);

        //on the main menu
        if(!dodge_man.getPlayerPlaying() && createdNewGame && resetGame)
        {
            Paint paint1 = new Paint();
            Paint paint2 = new Paint();
            Paint paint3 = new Paint();
            paint2.setColor(Color.RED);
            paint3.setColor(Color.DKGRAY);
            paint2.setStyle(Paint.Style.FILL_AND_STROKE);
            paint3.setStyle(Paint.Style.FILL);
            paint2.setTextSize(55);
            paint3.setTextSize(57);
            //canvas.drawText("DODGE MAN", width / 2 - 130, height / 2 - 50, paint2);
            //canvas.drawText("DODGE MAN", width / 2 - 133, height / 2 - 50, paint3);

            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC));
            canvas.drawText("PRESS TO START", width / 2 - 120, height / 2 - 3, paint1);

            paint1.setTextSize(18);
            //canvas.drawText("PRESS DOWN AND HOlD TO GO UP", width/2-120, height/2 + 27, paint1);
            //canvas.drawText("RELEASE TO GO DOWN", width / 2 - 120, height / 2 + 45, paint1);
            //canvas.drawText("EARN HEATH SHIELD TO INCREASE HP", width/2-120, height/2 + 62, paint1);
            //canvas.drawText("2 TYPE OF ENEMY: BIG ENEMY DAMAGE BY 2 AND " +
                   // "SMALL ENEMY DAMAGE BY 1", width/2-120, height/2 + 80, paint1);
        }

    }




}

