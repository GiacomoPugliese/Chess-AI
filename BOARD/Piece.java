package BOARD;

import javax.swing.Icon;
import javax.swing.JLabel;

import java.awt.Component;



public abstract class Piece extends JLabel{
    private static String colorTurn = "white";
    private String color;
    private Icon icon;

    public Piece(Icon icon, String color) {
        super(icon);
        this.icon = icon;
        this.color = color;
    }
    
    public String getColor() {
        return color;
    }

    public void makeMove(Square SelectedPiece, Square clickedSquare, Square[][] board){
        clickedSquare.removeAll();
        clickedSquare.add(SelectedPiece.getComponent(0));
        if(colorTurn.equals("white")){
            colorTurn = "black";
        }
        else{
            colorTurn = "white";
        }
    }
    public boolean moveValidate(Square SelectedPiece, Square clickedSquare, Square[][] board, String turn){
        //you can't take your own piece!
        if(clickedSquare.getComponentCount() > 0){
            if(((Piece)SelectedPiece.getComponent(0)).getColor().equals(((Piece)clickedSquare.getComponent(0)).getColor())){
                return false;
            } 
        }
        //you can only move if it's your turn!
        if(!((Piece) SelectedPiece.getComponent(0)).getColor().equals(turn)){
            return false;
        } 
        //can't move if you will be in check
        if(stillInCheck(SelectedPiece, clickedSquare, board, turn)){
            return false;
        }
        return true;
    }

    public boolean moveValidate(Square SelectedPiece, Square clickedSquare, Square[][] board){
        return moveValidate(SelectedPiece, clickedSquare, board, "white");
    }
    public abstract boolean canSee(Square SelectedPiece, Square clickedSquare, Square[][] board);

    public boolean stillInCheck(Square SelectedPiece, Square clickedSquare, Square[][] board, String turn){
        //simulate the move being attempted
        Component savedSelected = SelectedPiece.getComponent(0);
        Component savedClicked = null;
        if(clickedSquare.getComponentCount() > 0){
            savedClicked = clickedSquare.getComponent(0);
        }
        clickedSquare.removeAll();
        clickedSquare.add(SelectedPiece.getComponent(0));

        //if the move would yield a position in check, return true
        boolean legal = inCheck(board, turn);

        //undo the move before returning
        SelectedPiece.add(savedSelected);
        clickedSquare.removeAll();
        if(savedClicked != null){
            clickedSquare.add(savedClicked);
        }
        

        if(legal){
            return true;
        }

        return false;
    }
    public boolean inCheck(Square[][] board, String color){
        Square king_square = board[0][0];
        String turn;
        if(color.equals("white")){
            turn = "black";
        }
        else{
            turn = "white";
        }
        //loop through board to find king
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                if(board[i][j].getComponentCount() > 0){
                    if(board[i][j].getComponent(0) instanceof King && ((Piece)board[i][j].getComponent(0)).getColor().equals(color)){
                        king_square = board[i][j];
                    }
                }
            }
        }
        //check if any pieces are currently 'seeing' king and putting him in check
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                if(board[i][j].getComponentCount() > 0){
                    if(((Piece)board[i][j].getComponent(0)).canSee(board[i][j], king_square, board) && 
                    ((Piece)board[i][j].getComponent(0)).getColor().equals(turn)){
                        //pawns can move forward 1 but not take, so we don't consider them to put king in check
                        if(board[i][j].getComponent(0) instanceof Pawn){
                            if(j - king_square.getXPos() != 0){
                                return true;
                            }
                        }
                        else{
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    public String getTurn(){
        return colorTurn;
    }
    public Icon getThisIcon(){
        return icon;
    }
}
