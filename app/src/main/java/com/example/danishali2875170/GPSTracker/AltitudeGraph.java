package com.example.danishali2875170.GPSTracker;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AltitudeGraph extends View {

    double altitudecoordinates[] = null;
    double m_maxY = 0;

    Paint m_paint;
    Paint n_paint;


    public AltitudeGraph(Context context) {
        super(context);
        init();
    }

    public AltitudeGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AltitudeGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * onDraw tool initializer
     */
    private void init() {
        m_paint = new Paint();
        n_paint = new Paint();
        m_paint.setColor(Color.LTGRAY);
        n_paint.setColor(Color.BLACK);
        m_paint.setStrokeWidth(10);
        n_paint.setStrokeWidth(15);
    }

    public void setgraphco_ordinates(double Xi_graphArray[], int Xi_maxY)
    {
        altitudecoordinates = Xi_graphArray;
        m_maxY = Xi_maxY;
    }

    /**
     * <bold>Set max from co-ordinates of input array</bold>
     * @param Xi_graphArray
     */
    public void setgraphco_ordinates(double Xi_graphArray[])
    {
        int maxY = 0;
        for(int i = 0; i < Xi_graphArray.length; ++i)
        {
            if(Xi_graphArray[i] > maxY)
            {
                maxY = (int) Xi_graphArray[i];
            }
        }
        setgraphco_ordinates(Xi_graphArray, maxY);

    }

    /**
     * <bold>Draw Altitude co-ordinates</bold>
     * <p>Draw <em>point</em> on every co-ordinate, <em>colour Green if gain or colour Red is loss or colour Grey if same</em></p>
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        graph_lines(canvas);

        if(altitudecoordinates == null)
        {
            return;
        }

        int maxX = altitudecoordinates.length;

        float factorX = getWidth()/ (float)maxX;
        float factorY = getHeight()/ (float)m_maxY;
        int j = 0;
        for(int i = 1; i < altitudecoordinates.length; ++i) {
            j=i-1;
            int x0 = i - 1;
            double y0 = altitudecoordinates[i - 1]/2;
            int x1 = i;
            double y1 = altitudecoordinates[i]/2;

            int sx = (int) (x0 * factorX)+50;
            int sy = getHeight() - (int) (y0 * factorY)-80;
            int ex = (int) (x1 * factorX)+50;
            int ey = getHeight() - (int) (y1 * factorY)-80;


            if(y0<y1) {
                m_paint.setColor(Color.GREEN);
                canvas.drawLine(sx, sy, ex, ey, m_paint);
            }
            if(y0>y1){
                m_paint.setColor(Color.RED);
                canvas.drawLine(sx, sy, ex, ey, m_paint);
            }
            if(y0==y1) {
                m_paint.setColor(Color.GRAY);
                canvas.drawLine(sx, sy, ex, ey, m_paint);
            }
            canvas.drawPoint(sx, sy, n_paint);
            canvas.drawPoint(ex, ey, n_paint);
            n_paint.setTextSize(20f);
            canvas.drawText("" + String.format("%.03f", altitudecoordinates[j]*10), sx, sy, n_paint);

        }
        n_paint.setStrokeWidth(5);
        n_paint.setTextSize(40f);
        canvas.drawText("Altitude",600,570,n_paint);
        canvas.drawLine(20, 20, 20, getHeight()-20, n_paint);
        canvas.drawLine(20, getHeight()-20, getWidth()-20, getHeight()-20, n_paint);



    }

    /**
     * <bold>Grid Lines Formation</bold>
     * <p>Get width() & height(), form 8 boxes in background</p>
     * @param canvas
     */

    private void graph_lines(Canvas canvas){
        int cellWidth = getWidth() / 8;
        int cellHeight = getHeight() / 8;
        Paint linegridepaint = new Paint();
        linegridepaint.setColor(Color.WHITE);

        for (int i=1; i<8; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, getHeight(), linegridepaint);
            canvas.drawLine(0, i * cellHeight, getWidth(), i * cellHeight, linegridepaint);
        }
    }

}
