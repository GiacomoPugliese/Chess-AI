package GAME;
import javax.swing.Icon;

public class Rook extends Piece{
    private int moves = 0;

    public Rook(Icon icon, String color){
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
    public void makeMove(Square SelectedPiece, Square clickedSquare, Square[][] board){
        super.makeMove(SelectedPiece, clickedSquare, board);
        incrementMoves();
    }
    public boolean canSee(Square SelectedPiece, Square clickedSquare, Square[][] board){
        //can only move in a straight line
        if(!(SelectedPiece.getX() == clickedSquare.getX() || SelectedPiece.getY() == clickedSquare.getY())){
            return false;
        }
        //make sure no pieces are in the way (4 different lines are possible, so 4 different loops could be used)
        if(SelectedPiece.getXPos() > clickedSquare.getXPos()){
            for(int i = 1; i < SelectedPiece.getXPos() - clickedSquare.getXPos(); i++){
                if(!board[SelectedPiece.getYPos()][SelectedPiece.getXPos()-i].isEmpty()){
                    return false;
                }
            }
        }
        if(SelectedPiece.getXPos() < clickedSquare.getXPos()){
            for(int i = 1; i < clickedSquare.getXPos() - SelectedPiece.getXPos(); i++){
                if(!board[SelectedPiece.getYPos()][SelectedPiece.getXPos()+i].isEmpty()){
                    return false;
                }
            }
        }
        if(SelectedPiece.getYPos() > clickedSquare.getYPos()){
            for(int i = 1; i < SelectedPiece.getYPos() - clickedSquare.getYPos(); i++){
                if(!board[SelectedPiece.getYPos()-i][SelectedPiece.getXPos()].isEmpty()){
                    return false;
                }
            }
        }
        if(SelectedPiece.getYPos() < clickedSquare.getYPos()){
            for(int i = 1; i < clickedSquare.getYPos() - SelectedPiece.getYPos(); i++){
                if(!board[SelectedPiece.getYPos()+i][SelectedPiece.getXPos()].isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }

    public int getMoves(){
        return moves;
    }
    public void setMoves(int moves){
        this.moves = moves;
    }
    public void incrementMoves(){
        moves++;
    }
}
