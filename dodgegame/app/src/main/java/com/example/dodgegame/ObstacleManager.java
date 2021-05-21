package com.example.dodgegame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class ObstacleManager
{
    private ArrayList<Obstacles> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;
    private long startTime;
    private long initTime;
    private int score = 0;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color)
    {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;
        startTime = initTime = System.currentTimeMillis();
        obstacles = new ArrayList<>();

        populateObstacles();
    }

    public boolean playerCollision (Player player)
    {
        for (Obstacles ob : obstacles)
        {
            if (ob.playerCollision(player))
                return true;
        }
        return false;
    }

    private void populateObstacles()
    {
        int currY = -5*Constants.SCREEN_HEIGHT/4;

        while(currY < 0)
        {
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacles(obstacleHeight, color, xStart, currY, playerGap));
            currY += obstacleHeight + obstacleGap;
        }
    }
    public void update()
    {
        int elapsedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float)(Math.sqrt(1+(startTime-initTime)/1000.0))*Constants.SCREEN_HEIGHT/10000.0f;
        for(Obstacles ob : obstacles)
        {
            ob.increY(speed*elapsedTime);
        }
        if (obstacles.get(obstacles.size()-1).getRectangle().top >= Constants.SCREEN_HEIGHT)
        {
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0, new Obstacles(obstacleHeight,color,xStart,obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap,playerGap));
            obstacles.remove(obstacles.size()-1);
            score++;
        }
    }

    public void draw (Canvas canvas)
    {
        for(Obstacles ob : obstacles)
            ob.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.MAGENTA);
        canvas.drawText("Score = "+score, 50, 50 + paint.descent()-paint.ascent(), paint);
    }
}
