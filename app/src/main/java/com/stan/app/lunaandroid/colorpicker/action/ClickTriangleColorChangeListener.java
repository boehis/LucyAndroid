package com.stan.app.lunaandroid.colorpicker.action;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.stan.app.lunaandroid.R;
import com.stan.app.lunaandroid.util.Mode;
import com.stan.app.lunaandroid.util.ObservableColor;
import com.stan.app.lunaandroid.util.Point;
import com.stan.app.lunaandroid.util.TriangleDetail;
import com.stan.app.lunaandroid.util.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ClickTriangleColorChangeListener implements View.OnTouchListener {

    private Button selector;
    private ObservableColor color;
    private View parent;
    private boolean progressChanged;
    private double xPos = 0;
    private double yPos = 0;
    private TriangleDetail triDet;

    public ClickTriangleColorChangeListener(View parent, Button selector, ObservableColor color) {
        this.selector = selector;
        this.color = color;
        this.parent = parent;

        this.color.addObserver(new TriangleColorObserver(this));
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.performClick();
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            triDet = new TriangleDetail(view);
        }
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE){
            moveElement(motionEvent.getX(), motionEvent.getY());
            updateColor();
            parent.setBackgroundColor(Util.getAbsColor(color.get()));
        }
        return false;
    }

    public void colorToTrianglePos(int absColor) {
        View view = parent.findViewById(R.id.selector_holder);
        triDet = new TriangleDetail(view);

        //define case
        final int red = Color.red(absColor);
        final int green = Color.green(absColor);
        final int blue = Color.blue(absColor);

        List<Integer> colors = new LinkedList<>(Arrays.asList(red, green, blue));
        int frequency = Collections.frequency(colors, 255);

        if (frequency == 3) {
            xPos = triDet.getFullWidth() / 2;
            yPos = (Math.sqrt(3) / 3 * triDet.getTriangleWidth() + triDet.getHeightDiff());
        } else if (frequency == 2) {
            int index = colors.indexOf(255);
            colors.remove(index);
            index = colors.indexOf(255);
            colors.remove(index);
            Point p = calcColorIntersectionPoint(colors.get(0), 255);
            if (red != 255) {
                // p is solution
                xPos = p.getX();
                yPos = p.getY();
            } else if (green != 255) {
                Point pNew = rotateAroundCenter(p.getX(), p.getY(), Math.PI * 2 / 3);
                xPos = pNew.getX();
                yPos = pNew.getY();
            } else {
                Point pNew = rotateAroundCenter(p.getX(), p.getY(), Math.PI * 4 / 3);
                xPos = pNew.getX();
                yPos = pNew.getY();
            }
        } else if (frequency == 1) {
            int index = colors.indexOf(255);
            colors.remove(index);
            Point p = calcColorIntersectionPoint(colors.get(0), colors.get(1));
            xPos = p.getX();
            yPos = p.getY();
            if (green != 255) {
                xPos = (triDet.getFullWidth() / 2 - (p.getX() - triDet.getFullWidth() / 2));
                if (red == 255) {
                    Point pNew = rotateAroundCenter(xPos, p.getY(), Math.PI * 2 / 3);
                    xPos = pNew.getX();
                    yPos = pNew.getY();
                }
            }
        } else {
            //fucked
            xPos = 0;
            yPos = 0;
        }

        updateSelectorPos();

    }

    private Point calcColorIntersectionPoint(double topColorValue, double cornerColorValue) {

        //calc first slope
        double topCornerX = triDet.getWidthDiff();
        double topCornerY = triDet.getTriangleHeight() + triDet.getHeightDiff();

        //calc sidepoint Ps
        //dist from corner to sitepoint on triangle topCPs
        double topCPs = topColorValue / 255 * (triDet.getTriangleWidth() / 2);
        double topPsX = triDet.getTriangleWidth() + triDet.getWidthDiff() - (Math.cos(Math.PI * 1 / 3) * topCPs);
        double topPsY = triDet.getTriangleHeight() + triDet.getHeightDiff() - (Math.sin(Math.PI * 1 / 3) * topCPs);

        double cornerCPs = cornerColorValue / 255 * (triDet.getTriangleWidth() / 2);

        double cornerCornerX = triDet.getFullWidth() / 2;
        double cornerCornerY = triDet.getHeightDiff();
        double cornerPsX = triDet.getTriangleWidth() + triDet.getWidthDiff() - cornerCPs;
        double cornerPsY = triDet.getTriangleHeight() + triDet.getHeightDiff();

        double x1 = topCornerX;
        double x2 = topPsX;
        double x3 = cornerCornerX;
        double x4 = cornerPsX;


        double y1 = topCornerY;
        double y2 = topPsY;
        double y3 = cornerCornerY;
        double y4 = cornerPsY;

        double resX = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        double resY = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));

        return new Point(resX, resY);
    }

    private Point rotateAroundCenter(double x, double y, double deg) {
        double originX = triDet.getFullWidth() / 2;
        double originY = triDet.getTriangleHeight() - (Math.tan(Math.PI / 6) * triDet.getTriangleWidth() / 2) + triDet.getHeightDiff();

        double xNew = Math.cos(deg) * (x - originX) - Math.sin(deg) * (y - originY) + originX;
        double yNew = Math.sin(deg) * (x - originX) + Math.cos(deg) * (y - originY) + originY;

        return new Point(xNew, yNew);
    }

    private void updateColor() {
        Point pB = rotateAroundCenter(xPos, yPos, Math.PI * 2 / 3);
        Point pG = rotateAroundCenter(xPos, yPos, Math.PI * 4 / 3);

        progressChanged = true;
        color.updatePreserveBrightness(calcColor(xPos, yPos), Mode.RED);
        color.updatePreserveBrightness(calcColor(pG.getX(), pG.getY()), Mode.GEEEN);
        color.updatePreserveBrightness(calcColor(pB.getX(), pB.getY()), Mode.BLUE);
        progressChanged = false;
    }

    private int calcColor(double x, double y) {
        //increasy values to avoid division by 0 errors etc.
        x += 0.00001;
        y += 0.00001;
        //calc point on opposite side: Q
        //get distance from point to triangel height
        double pointDistToMiddle = Math.abs(x - triDet.getFullWidth() / 2);
        //centric stretching to get distance from triangle middle to Q
        double triMiddToQ = 0;
        if (y != 0) {
            triMiddToQ = pointDistToMiddle / (y - triDet.getHeightDiff()) * triDet.getTriangleHeight();
        }

        //calc point on bisectring line: P
        double beta = Math.atan((triDet.getFullHeight() - y - triDet.getHeightDiff()) / Math.abs(triMiddToQ - pointDistToMiddle));
        //calc bisec corner C to P lenght
        double CP = Math.sin(beta) / Math.sin((Math.PI * 5 / 6) - beta) * (triDet.getTriangleWidth() / 2 + triMiddToQ);
        double triMiddToP = Math.cos(Math.PI / 6) * CP - triDet.getTriangleWidth() / 2;
        double pY = triDet.getTriangleHeight() + triDet.getHeightDiff() - Math.sin(Math.PI / 6) * CP;


        if (y <= pY) {
            return 255;
        } else {
            //Length PQ;
            double PQ = Math.sqrt(Math.pow(triMiddToQ - triMiddToP, 2) + Math.pow(Math.sin(Math.PI / 6) * CP, 2));
            //Length P to Point
            double PtoPoint = Math.sqrt(Math.pow(pointDistToMiddle - triMiddToP, 2) + Math.pow(y - pY, 2));
            //map PQ,PtoPoint to 0 - 255
            return (int) (PtoPoint / PQ * (-255) + 255);
        }
    }

    private void moveElement(float xIn, float yIn) {
        double x = Math.max(triDet.getWidthDiff(), Math.min(triDet.getTriangleWidth() + triDet.getWidthDiff(), xIn));
        double y = Math.max(triDet.getHeightDiff(), Math.min(triDet.getTriangleHeight() + triDet.getHeightDiff(), yIn));
        xPos = translateXIntoBound(x, y);
        yPos = y;
        updateSelectorPos();
    }

    private void updateSelectorPos() {
        selector.setX((float) xPos - selector.getWidth() / 2);
        selector.setY((float) yPos - selector.getHeight() / 2);
    }

    private double translateXIntoBound(double x, double y) {
        double minX = Math.max(0, Math.tan(Math.PI / 6) * (triDet.getTriangleHeight() - (y - triDet.getHeightDiff()))) + triDet.getWidthDiff();
        double maxX = triDet.getFullWidth() - minX;
        return Math.max(minX, Math.min(maxX, x));
    }

    public boolean isProgressChanged() {
        return progressChanged;
    }
}
