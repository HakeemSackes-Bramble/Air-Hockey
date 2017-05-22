package nyc.c4q.hakeemsackes_bramble.airhockey;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hakeemsackes-bramble on 5/21/17.
 */

public class TableView extends View {

    private float yAngAccel;
    private float xAngAccel;
    private float puckRadius;
    private float xPosition;
    private float yPosition;
    private Paint dotPaint;
    int xdir = 1;
    int ydir = 1;
    private float xSpeed = 0;
    private float ySpeed = 0;
    private Canvas c;
    private float strikerXPos;
    private float strikerYPos;
    private float strikerRadius;
//    TiltSensor sensor;
//    SensorManager sensorManager;


    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        puckRadius = 50;
        strikerRadius = 70;
        xPosition = 200;
        yPosition = 200;
        strikerXPos = getX() / 2;
        strikerYPos = 3 * getY() / 4;
        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setColor(Color.BLUE);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                double strikerDistFromPuck = Math.sqrt(Math.pow(xPosition - strikerXPos, 2) + Math.pow(yPosition - strikerYPos, 2));
        double fingerDistFromStriker = Math.sqrt(Math.pow(xPosition - event.getX(), 2) + Math.pow(yPosition - event.getY(), 2));
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        if(fingerDistFromStriker < strikerRadius){
                            strikerXPos = event.getX();
                            strikerYPos = event.getY();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        strikerXPos = event.getX();
                        strikerYPos = event.getY();
                        if (strikerDistFromPuck < puckRadius + strikerRadius) {
                            xSpeed += ((xPosition - event.getX()) / 20 * xdir);
                            ySpeed += ((yPosition - event.getY()) / 20 * ydir);
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

    }


    public void setxAngAccel(float xAngAccel) {
        this.xAngAccel = xAngAccel;
    }

    public void setyAngAccel(float yAngAccel) {
        this.yAngAccel = yAngAccel;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        c = canvas;
        canvas.drawCircle(xPosition, yPosition, puckRadius, dotPaint);
        dotPaint.setColor(Color.BLACK);
        canvas.drawCircle(strikerXPos, strikerYPos, strikerRadius, dotPaint);
        if (xPosition < this.getLeft() + puckRadius || xPosition > this.getRight() - puckRadius) {
            xdir *= -1;
        }
        if (yPosition > (this.getBottom() - puckRadius) || yPosition < (this.getTop() + puckRadius)) {
            ydir *= -1;
        }
        xPosition += xSpeed * xdir;
        yPosition += ySpeed * ydir;

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        invalidate();

    }
}

