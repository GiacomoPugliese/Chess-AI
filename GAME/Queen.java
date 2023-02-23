package GAME;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Queen extends Piece{
    public Queen(Icon icon, String color){
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
        //if either a rook or a bishop could see it, so could a queen
        Bishop temp_bishop = new Bishop(new ImageIcon("GAME/white_pieces_imgs/bishopwhite.png"), "white");
        Rook temp_rook = new Rook(new ImageIcon("GAME/white_pieces_imgs/rookwhite.png"), "white");
        if(temp_bishop.canSee(SelectedPiece, clickedSquare, board)){
            return true;
        }
        if(temp_rook.canSee(SelectedPiece, clickedSquare, board)){
            return true;
        }
        return false;
    }
}
