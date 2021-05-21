package com.example.dodgegame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import java.util.concurrent.ExecutionException;

public class MainThread extends Thread
{
    public static final int MAX_FPS = 30;
    private double avgFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private  boolean running;
    public static Canvas canvas;

    public void setRunning (boolean running)
    {
        this.running = running;
    }

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel)
    {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run()
    {
        long startTime;
        long timeMS = 1000/MAX_FPS;
        long waitTime;
        int framecount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder)
                {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                if (canvas != null)
                {
                    try
                    {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            timeMS = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMS;
            try
            {
                if(waitTime > 0)
                {
                    this.sleep(waitTime);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            totalTime += System.nanoTime() - startTime;
            framecount++;
            if(framecount == MAX_FPS)
            {
                avgFPS = 1000/((totalTime/framecount)/1000000);
                framecount = 0;
                totalTime = 0;
                System.out.println(avgFPS);
            }
        }
    }
}
