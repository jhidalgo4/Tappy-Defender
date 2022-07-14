package edu.utep.cs.cs4381.tappydefender;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.utep.cs.cs4381.tappydefender.model.EnemyShip;
import edu.utep.cs.cs4381.tappydefender.model.PlayerShip;
import edu.utep.cs.cs4381.tappydefender.model.PowerUpShip;
import edu.utep.cs.cs4381.tappydefender.model.StarDust;

public class GameView extends SurfaceView implements Runnable {
    private int width, height;
    int totalDistanceToWin = 10000;  //Easy = 1000;    // Regular = 10000;
    float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;
    private boolean playing, gameEnded, gameWon;
    private Canvas canvas;
    private Paint paint;
    private SurfaceHolder holder;
    private Thread gameThread;
    private PlayerShip player;
    private StarDust spec;
//    private MusicListener musicLis;
    private SoundEffect soundEffect;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;



    private Context cntx;
    public List<StarDust> dustList = new CopyOnWriteArrayList<>();
    private List<EnemyShip> enemyShips = new CopyOnWriteArrayList<>();
    private List<PowerUpShip> healthShips = new CopyOnWriteArrayList<>();

    //The W given as parameter is LEFT & RIGHT when in landscape mode
    //The H given as parameter is UP & DOWN when in landscape mode
    public GameView(Context context, int w, int h) {
        super(context);
        this.width = w;
        this.height = h;

        paint = new Paint();
        canvas = new Canvas();
        holder = getHolder();
        soundEffect = new SoundEffect(context);
        cntx = getContext();
        fastestTime = Long.MAX_VALUE;

        prefs = context.getSharedPreferences("highScore1", context.MODE_PRIVATE);
        editor = prefs.edit();

        startNewGame();
    }

    //METHOD called to start new game and restart player stats
    public void startNewGame(){
        calculateWidthAndHeight();
        player = new PlayerShip(cntx, width, height);
        playing = true;
        gameEnded = false;
        gameWon = false;

        distanceRemaining = totalDistanceToWin;

        //SETUP the starDust/dustLise
        dustList.clear();
        int numSpecs = 40;
        for (int i = 0; i < numSpecs; i++) {
            spec = new StarDust(width, height); //spaceDust
            dustList.add(spec);
        }

        //SETUP the construction of enemyShips
        enemyShips.clear();
        enemyShips.add(new EnemyShip(cntx, width, height));
        enemyShips.add(new EnemyShip(cntx, width, height));
        enemyShips.add(new EnemyShip(cntx, width, height));
        enemyShips.add(new EnemyShip(cntx, width, height));
//        enemyShips.add(new EnemyShip(cntx, width, height));
//        enemyShips.add(new EnemyShip(cntx, width, height));

        healthShips.clear();
        healthShips.add(new PowerUpShip(cntx, width, height));

        timeTaken = 0;
        timeStarted = System.currentTimeMillis();

        resume();
        soundEffect.play(SoundEffect.Sound.START);
    }

    ////////////////////////// THREAD
    @Override
    public void run() {
        while (playing && !gameEnded) {
            update();
            draw();
            control();
        }
        if(gameEnded) pause();
    }
    private void update() {
        //Update Enemy Ships
        for (EnemyShip enemy: enemyShips) {
            //Collision detected
            if (Rect.intersects(player.getHitbox(), enemy.getHitbox() ) ) {
                soundEffect.play(SoundEffect.Sound.BUMP);  //play sound
                enemy.setX(-enemy.getBitmap().getWidth()); //remove enimy ship from screen
                enemy.setHasCollided(true);
                player.setShieldStrength(player.getShieldStrength() -1);
                if(player.getShieldStrength() <= 0 ){
                    //set game Over
                    gameEnded = true;
                    soundEffect.play(SoundEffect.Sound.DESTROYED);
                }
            }
        }

        //Update PowerUp Ships
        for (PowerUpShip hs: healthShips) {
            //Collision detected
            if (Rect.intersects(player.getHitbox(), hs.getHitbox() ) ) {
                hs.setX(-hs.getBitmap().getWidth()); //remove enemy ship from screen
                hs.setHasCollided(true);
                soundEffect.play(SoundEffect.Sound.BUMP);
                player.setShieldStrength(player.getShieldStrength() +1);
            }
        }

        //CHECKS if Game WON
        if (distanceRemaining <= 0) {
            if (timeTaken < fastestTime) {
                fastestTime = timeTaken;
                editor.putLong("fastestTime", timeTaken);
                editor.commit();
                fastestTime = timeTaken;
            }
            gameEnded = true;
            gameWon = true;
            soundEffect.play(SoundEffect.Sound.WIN); //play win
        }

        //UPDATE PLAYER-SHIP, HEALTH-SHIP AND ENEMY-SHIP
        player.update(); //move ship along
        for (StarDust sd : dustList) {
            sd.update(player.getSpeed());
        }
        for (EnemyShip enemy: enemyShips) {
            enemy.update(player.getSpeed() );
        }
        for (PowerUpShip hs: healthShips) {
            hs.update(player.getSpeed() );
        }

        //Update Distance and time taken
        if (!gameEnded) {
            distanceRemaining -= 9;
            timeTaken = System.currentTimeMillis() - timeStarted;
        }
    }

    private void draw() {
        if (holder.getSurface().isValid() ){
            canvas = holder.lockCanvas();
            ///////////////

            //draw here
            drawSetupAndBackground();

            //Update HUB
            if(!gameEnded){
                drawHub();
            }
            else{
                if(gameWon){
                    drawWon();
                }
                else{ drawLoss();}
            }

            ///////////////
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try{
            gameThread.sleep(17);
        }catch(InterruptedException e){
        }
    }

    ///////////////////////// LIFE-CYCLE
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    //hits this case when app second after gameActivity
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    ///////////////////////// UI - TOUCH EVENT
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if(gameEnded){
                    handleTapReplay(motionEvent.getX(), motionEvent.getY() );
                }
                player.setBoosting(true);
                break;
            case MotionEvent.ACTION_UP:
                player.setBoosting(false);
                break;
        }
        return true;
    }

    //Calculate Height and width
    private void calculateWidthAndHeight() {
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    height = Math.min(getWidth(), getHeight());
                    width = Math.max(getWidth(), getHeight());
                }
            });
        }
    }

    // Format time so that you can display appropiately
    private String formatTime(String label, long time) { // time in milliseconds
        return String.format("%s: %d.%03ds", label, time / 1000, time % 1000);
    }

    private static boolean isIn(float x, float y, float left, float top, float right, float bottom) {
        return new RectF(left, top, right, bottom).contains(x, y);
    }

    public void handleTapReplay(float x, float y){
        if( isIn(x,y,450, 600,1350, 850) ){
            Log.w("w1", "START NEW GAME");

            //start new game
            startNewGame();  // How to pass context
        }
    }

    //////////////////////////////////////////// DRAWING
    //Update and draw Background
    public void drawSetupAndBackground(){
        canvas.drawColor(Color.argb(255, 0, 0, 0)); //Draw black background

        //Draw player-ship(s)
        canvas.drawBitmap(
                player.getBitmap(),
                player.getX(),  player.getY(), paint);

        //Draw the StarDust(s)
        paint.setColor(Color.WHITE);
        for (StarDust sd : dustList) {
            paint.setStrokeWidth(sd.getSizeOfDust() );
            canvas.drawPoint(sd.getX(), sd.getY(), paint);
        }

        //Draw the EnemyShip(s)
        for (EnemyShip enemy: enemyShips) {
            canvas.drawBitmap(enemy.getBitmap(), enemy.getX(), enemy.getY(), paint); //draws Enemy-Ship(s)
        }

        //Draw the Power-up ship(s)
        for (PowerUpShip hs : healthShips) {
            canvas.drawBitmap(hs.getBitmap(), hs.getX(), hs.getY(), paint); //draws Power-Up ship(s)
        }

    }

    //UPDATE HUB on screen
    public void drawHub(){
        paint.setColor(Color.argb(255, 255, 255, 255));
        paint.setStrokeWidth(4);
        paint.setTextSize(48);
        int topy = 50;
        paint.setTextAlign(Paint.Align.LEFT);

        if(fastestTime >= Long.MAX_VALUE){
            //canvas.drawText(formatTime("Fastest", 10000), 10, topy, paint);
            canvas.drawText("Fastest time not set" , 10, topy, paint);
        }
        else{
            long tempP = prefs.getLong("fastestTime", Long.MAX_VALUE);
            canvas.drawText(formatTime("Fastest", tempP), 10, topy, paint);
        }
        canvas.drawText("Shield: " + player.getShieldStrength(), 10, 1000, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(formatTime("Time", timeTaken), (width / 2), topy, paint);
        canvas.drawText("Distance: " + distanceRemaining / 1000 + " KM", (width / 2), 1000, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Speed: " + player.getSpeed() * 60 + " MPS", (width / 2)+850, topy, paint);
    }

    //Draw game loss
    public void drawLoss(){
        //Draw background square POP-UP
        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(130,255,255,255); //white transpaerent
        canvas.drawRect(100,100,1700, 900, paint);

        //Writes GAME OVER
        paint.setARGB(240,0,0,0); //Red lettering
        paint.setTextSize(100);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Game Over",(width/2),200, paint);

        //Draws time stats
        paint.setTextSize(50);
        canvas.drawText(formatTime("Time ", timeTaken), (width/2), 400, paint);

        //Draws distance stats
        paint.setTextSize(50);
        canvas.drawText("Distance remaning:  "+distanceRemaining + " KM",(width/2),500, paint);

        //Draws square button to restart game
        paint.setARGB(170,247,13,65);
        canvas.drawRect(450,600,1350, 850, paint);

        //Draws Text inside button
        paint.setColor(Color.BLACK);
        paint.setTextSize(70);
        canvas.drawText("Tap to replay, Joaquin", (width/2),750, paint);
    }

    //Draw game won
    public void drawWon(){
        //Draw background square POP-UP
        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(130,255,255,255); //white transpaerent
        canvas.drawRect(100,100,1700, 900, paint);

        //Writes GAME OVER
        paint.setARGB(240,0,0,0); //Red lettering
        paint.setTextSize(100);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Congratulations, You Win!",(width/2),200, paint);

        //Draws time stats
        paint.setTextSize(50);
        canvas.drawText(formatTime("Time ", timeTaken), (width/2), 400, paint);

        //Draws distance stats
        paint.setTextSize(50);
        canvas.drawText("Shield(s) remaining:   "+player.getShieldStrength(),(width/2),500, paint);

        //Draws square button to restart game
        paint.setARGB(170,247,13,65);
        canvas.drawRect(450,600,1350, 850, paint);

        //Draws Text inside button
        paint.setColor(Color.BLACK);
        paint.setTextSize(70);
        canvas.drawText("Tap to replay, Joaquin", (width/2),750, paint);

    }
}

//PLAY SOUNDS
class SoundEffect {
    public enum Sound {
        START(R.raw.start),
        BUMP(R.raw.bump),
        DESTROYED(R.raw.destroyed),
        WIN(R.raw.win);

        public final int resourceId;
        private int soundId;

        Sound(int resourceId) {
            this.resourceId = resourceId;
        }
    }

    private final SoundPool soundPool;

    public SoundEffect(Context context) {
        soundPool = new SoundPool.Builder().setMaxStreams(Sound.values().length).build();
        for (Sound sound: Sound.values()) {
            sound.soundId = soundPool.load(context, sound.resourceId, 1);
        }
    }

    public void play(Sound sound) {
        soundPool.play(sound.soundId, 1, 1, 0, 0, 1);
    }
}


