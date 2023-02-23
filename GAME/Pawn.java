package GAME;

import javax.swing.Icon;
import javax.swing.ImageIcon;


public class Pawn extends Piece {
    //to count number of moves pawn has made
    private int moves = 0;

    public Pawn(Icon icon, String color){
        super(icon, color);
    }
    public boolean moveValidate(Square SelectedPiece, Square clickedSquare, Square[][] board, String turn){
        if(!super.moveValidate(SelectedPiece, clickedSquare, board, turn)){
            return false;
        }
        //first move cases
        if(clickedSquare.isEmpty()){
            if(((Piece) SelectedPiece.getComponent(0)).getColor().equals("white")){
                //check for first move case
                if(((Pawn) SelectedPiece.getComponent(0)).getMoves() == 0){
                    if(SelectedPiece.getYPos() - 2 == clickedSquare.getYPos() && SelectedPiece.getXPos() == clickedSquare.getXPos() && board[clickedSquare.getYPos()+1][clickedSquare.getXPos()].isEmpty()){
                        return true;
                    }
                }
            }
            //check for first move case
            else{
                if(((Pawn) SelectedPiece.getComponent(0)).getMoves() == 0){
                    if(SelectedPiece.getYPos() + 2 == clickedSquare.getYPos() && SelectedPiece.getXPos() == clickedSquare.getXPos() && board[clickedSquare.getYPos()-1][clickedSquare.getXPos()].isEmpty()){
                        return true;
                    }
                }
            }
        }
        if(!canSee(SelectedPiece, clickedSquare, board)){
            return false;
        }
        return true;
    }

    public int getMoves() {
        return moves;
    }
    public void setMoves(int moves){
        this.moves = moves;
    }
    public void incrementMoves(){
        moves++;
    }
    public boolean canSee(Square SelectedPiece, Square clickedSquare, Square[][] board){
        if(SelectedPiece.getComponentCount() < 1){
            return false;
        }
        //validate for white pawns
        if(((Piece) SelectedPiece.getComponent(0)).getColor().equals("white")){
            //pawns must move one up and at most one to the side
            if(Math.abs(SelectedPiece.getXPos() - clickedSquare.getXPos()) > 1 || SelectedPiece.getYPos() - 1 != clickedSquare.getYPos()){
                return false;
            }
            //en pessant for white
            // if(Math.abs(SelectedPiece.getXPos() - clickedSquare.getXPos()) == 1 && 
            //     clickedSquare.isEmpty() && (board[clickedSquare.getYPos() + 1][clickedSquare.getXPos()].getComponentCount() > 0)){
            //     if(board[clickedSquare.getYPos() + 1][clickedSquare.getXPos()].getComponent(0) instanceof Pawn){
            //         if(((Pawn)board[clickedSquare.getYPos() + 1][clickedSquare.getXPos()].getComponent(0)).getMoves() == 1){
            //             board[clickedSquare.getYPos() + 1][clickedSquare.getXPos()].removeAll();
            //             return true;
            //         }
            //     }
            // }

        }
        //validate for black pawns
        if(((Piece) SelectedPiece.getComponent(0)).getColor().equals("black")){
            //pawns must move one down and at most one to the side
            if(Math.abs(SelectedPiece.getXPos() - clickedSquare.getXPos()) > 1 || SelectedPiece.getYPos() + 1 != clickedSquare.getYPos()){
                return false;
            }
            //en pessant for black 
            // if(Math.abs(SelectedPiece.getXPos() - clickedSquare.getXPos()) == 1 && 
            //     clickedSquare.isEmpty() && (board[clickedSquare.getYPos() - 1][clickedSquare.getXPos()].getComponentCount() > 0)){
            //     if(board[clickedSquare.getYPos() - 1][clickedSquare.getXPos()].getComponent(0) instanceof Pawn){
            //         if(((Pawn)board[clickedSquare.getYPos() - 1][clickedSquare.getXPos()].getComponent(0)).getMoves() == 1){
            //             board[clickedSquare.getYPos() - 1][clickedSquare.getXPos()].removeAll();
            //             return true;
            //         }
            //     }
            // }
        }
        //pawns can't movie diagonally if no piece is there to take
        if(SelectedPiece.getXPos() != clickedSquare.getXPos() && clickedSquare.getComponentCount() < 1){
            return false;
        }
        //can't take unless diagonally
        if(SelectedPiece.getXPos() == clickedSquare.getXPos()){
            if(!clickedSquare.isEmpty()){
                return false;
            }
        }
        return true;
    }
    //pawns promote on the final rank
    public void makeMove(Square SelectedPiece, Square clickedSquare, Square[][] board){
        super.makeMove(SelectedPiece, clickedSquare, board);
        if(clickedSquare.getYPos() == 7 || clickedSquare.getYPos() == 0){
            String queen_color = ((Piece)clickedSquare.getComponent(0)).getColor();
            clickedSquare.removeAll();
            if(queen_color.equals("white")){
                clickedSquare.add(new Queen(new ImageIcon("GAME/white_pieces_imgs/queenwhite.png"), queen_color));
            }
            else{
                clickedSquare.add(new Queen(new ImageIcon("GAME/black_pieces_imgs/queenblack.png"), queen_color));
            }
        }
        incrementMoves();
    }
}
