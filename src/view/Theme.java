package view;

import javax.swing.*;

public enum Theme {
    sunSet(new ImageIcon("res/sunSet.png")),wave(new ImageIcon("res/wave.png"));
    private final ImageIcon backgroundIcon;

    Theme(ImageIcon backgroundIcon) {
        this.backgroundIcon=backgroundIcon;
    }
    public ImageIcon getBackgroundIcon(){
        return backgroundIcon;
    }
}
