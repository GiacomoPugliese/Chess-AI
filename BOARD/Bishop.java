package BOARD;

import javax.swing.Icon;

public class Bishop extends Piece{
    public Bishop(Icon icon, String color){
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
        //must be a diagonal move
        if(Math.abs(SelectedPiece.getXPos() - clickedSquare.getXPos()) != Math.abs(SelectedPiece.getYPos() - clickedSquare.getYPos())) {
            return false;
        }
        //make sure no pieces are in the way (4 different diagonals are possible, so 4 different loops could be used)
        if(SelectedPiece.getXPos() > clickedSquare.getXPos() && SelectedPiece.getYPos() > clickedSquare.getYPos()){
            for(int i = 1; i < SelectedPiece.getXPos() - clickedSquare.getXPos(); i++){
                if(!board[SelectedPiece.getYPos()-i][SelectedPiece.getXPos()-i].isEmpty()){
                    return false;
                }
            }
        }
        if(SelectedPiece.getXPos() > clickedSquare.getXPos() && SelectedPiece.getYPos() < clickedSquare.getYPos()){
            for(int i = 1; i < SelectedPiece.getXPos() - clickedSquare.getXPos(); i++){
                if(!board[SelectedPiece.getYPos()+i][SelectedPiece.getXPos()-i].isEmpty()){
                    return false;
                }
            }
        }
        if(SelectedPiece.getXPos() < clickedSquare.getXPos() && SelectedPiece.getYPos() > clickedSquare.getYPos()){
            for(int i = 1; i < clickedSquare.getXPos() - SelectedPiece.getXPos(); i++){
                if(!board[SelectedPiece.getYPos()-i][SelectedPiece.getXPos()+i].isEmpty()){
                    return false;
                }
            }
        }
        if(SelectedPiece.getXPos() < clickedSquare.getXPos() && SelectedPiece.getYPos() < clickedSquare.getYPos()){
            for(int i = 1; i < clickedSquare.getXPos() - SelectedPiece.getXPos(); i++){
                if(!board[SelectedPiece.getYPos()+i][SelectedPiece.getXPos()+i].isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }
}
