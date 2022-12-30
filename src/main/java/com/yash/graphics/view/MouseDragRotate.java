package com.yash.graphics.view;

public class MouseDragRotate {

    /**
     * Co-ordinates where the mouse drag started
     * */
    double initialX;
    double initialY;

    /**
     * Co-ordinates where the mouse was dragged to
     * */
    double finalX;
    double finalY;

    public int getRotationAngleAboutY(){
        return (int) (finalX - initialX);
    }

    public int getRotationAngleAboutX(){
        return (int) (finalY - initialY);
    }
}
