package BOARD;

import javax.swing.JPanel;

public class Square extends JPanel{
    private int xval;
    private int yval;

    public Square(int x, int y) {
        xval = x;
        yval = y;
    }
    public Square(Square square) {
        this.xval = square.xval;
        this.yval = square.yval;
    }

    public int getXPos() {
        return xval;
    }

    public int getYPos() {
        return yval;
    }
    public boolean isEmpty() {
        if(this.getComponentCount() > 0){
            return false;
        }
        return true;
    }
}
