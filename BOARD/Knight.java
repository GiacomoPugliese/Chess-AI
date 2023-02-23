package BOARD;

import javax.swing.Icon;

public class Knight extends Piece{
    public Knight(Icon icon, String color){
        super(icon, color);
    }
    public boolean moveValidate(Square SelectedPiece, Square clickedSquare, Square[][] board, String turn){
        if(!super.moveValidate(SelectedPiece, clickedSquare, board, turn)){
            return false;
        }
        if(!canSee(SelectedPiece, clickedSquare, board)){
            return false;
        }
        return true;
    }
    public boolean canSee(Square SelectedPiece, Square clickedSquare, Square[][] board){
        //moves can only be made 1 or 2  to the side
        if(!(Math.abs(SelectedPiece.getXPos() - clickedSquare.getXPos()) == 1 || Math.abs(SelectedPiece.getXPos() - clickedSquare.getXPos()) == 2)){
            return false;
        }

        //moves can only be made 1 or 2 up
        if(!(Math.abs(SelectedPiece.getYPos() - clickedSquare.getYPos()) == 1 || Math.abs(SelectedPiece.getYPos() - clickedSquare.getYPos()) == 2)){
            return false;
        }

        //total distance traveled must be equal to 3
        if(!((Math.abs(((SelectedPiece.getXPos() - clickedSquare.getXPos()))) + Math.abs((SelectedPiece.getYPos() - clickedSquare.getYPos())) == 3))){
            return false;
        }
        
        return true;
    }
}
