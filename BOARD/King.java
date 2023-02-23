package BOARD;

import javax.swing.Icon;

public class King extends Piece{
    private int moves = 0;
    public King(Icon icon, String color){
        super(icon, color);
    }
    public boolean moveValidate(Square SelectedPiece, Square clickedSquare, Square[][] board, String turn){
        if(!super.moveValidate(SelectedPiece, clickedSquare, board, turn)){
            return false;
        }
        if(canCastle(SelectedPiece, clickedSquare, board, turn)){
            return true;
        }
        if(!canSee(SelectedPiece, clickedSquare, board)){
            return false;
        }
        return true;
    }
    public void makeMove(Square SelectedPiece, Square clickedSquare, Square[][] board){
        if(canCastle(SelectedPiece, clickedSquare, board, ((Piece)SelectedPiece.getComponent(0)).getColor())){
            if(Math.abs(clickedSquare.getXPos() - SelectedPiece.getXPos()) > 1){
                if(clickedSquare.getXPos() == 1 && clickedSquare.getYPos() == 0){
                    board[0][2].add(board[0][0].getComponent(0));
                    board[0][0].removeAll();
                }
                if(clickedSquare.getXPos() == 5 && clickedSquare.getYPos() == 0){
                    board[0][4].add(board[0][7].getComponent(0));
                    board[0][7].removeAll();
                }
                if(clickedSquare.getXPos() == 1 && clickedSquare.getYPos() == 7){
                    board[7][2].add(board[7][0].getComponent(0));
                    board[7][0].removeAll();
                }
                if(clickedSquare.getXPos() == 5 && clickedSquare.getYPos() == 7){
                    board[7][4].add(board[7][7].getComponent(0));
                    board[7][7].removeAll();
                }
            }
        }
        super.makeMove(SelectedPiece, clickedSquare, board);
        incrementMoves();
    }
    public boolean canSee(Square SelectedPiece, Square clickedSquare, Square[][] board){
        //can't move more than 1 square
        if(Math.abs(SelectedPiece.getXPos() - clickedSquare.getXPos()) > 1 || Math.abs(SelectedPiece.getYPos() - clickedSquare.getYPos()) > 1){
            return false;
        }
        return true;
    }

    public boolean canCastle(Square SelectedPiece, Square clickedSquare, Square[][] board, String turn){
        //the king can castle if it and the rook its using haven't moved yet, and the spaces it has to travel are both empty and won't cause check
        if(getMoves() == 0 && !(inCheck(board, turn))){
            if(((Piece)SelectedPiece.getComponent(0)).getColor().equals("white")){
                if(clickedSquare.getXPos() == 1 && clickedSquare.getYPos() == 7){
                    if(board[7][0].getComponentCount() > 0){
                        if(board[7][0].getComponent(0) instanceof Rook && ((Piece)board[7][0].getComponent(0)).getColor().equals(turn)){
                            if(board[7][1].isEmpty() && board[7][2].isEmpty() && 
                                ((Rook)board[7][0].getComponent(0)).getMoves() == 0){
                                if(!(stillInCheck(SelectedPiece, board[7][1], board, turn) || stillInCheck(SelectedPiece, board[7][2], board, turn))){
                                    return true;
                                }
                            }
                        }
                    }
                }
                if(clickedSquare.getXPos() == 5 && clickedSquare.getYPos() == 7){
                    if(board[7][7].getComponentCount() > 0){
                        if(board[7][7].getComponent(0) instanceof Rook && ((Piece)board[7][7].getComponent(0)).getColor().equals(turn)){
                            if(board[7][6].isEmpty() && board[7][5].isEmpty() && board[7][4].isEmpty() && 
                                ((Rook)board[7][7].getComponent(0)).getMoves() == 0){
                                if(!(stillInCheck(SelectedPiece, board[7][6], board, turn) || stillInCheck(SelectedPiece, board[7][5], board, turn) || stillInCheck(SelectedPiece, board[7][4], board, turn))){
                                    return true;
                                }
                        }
                    }
                    }
                }
            }
            if(((Piece)SelectedPiece.getComponent(0)).getColor().equals("black")){
                if(clickedSquare.getXPos() == 1 && clickedSquare.getYPos() == 0){
                    if(board[0][0].getComponentCount() > 0){
                        if(board[0][0].getComponent(0) instanceof Rook && ((Piece)board[0][0].getComponent(0)).getColor().equals(turn)){
                            if(board[0][1].isEmpty() && board[0][2].isEmpty() && 
                                ((Rook)board[0][0].getComponent(0)).getMoves() == 0){
                                if(!(stillInCheck(SelectedPiece, board[0][1], board, turn) || stillInCheck(SelectedPiece, board[0][2], board, turn))){
                                    return true;
                                }
                        }
                        }
                    }
                }
                if(clickedSquare.getXPos() == 5 && clickedSquare.getYPos() == 0){
                    if(board[0][7].getComponentCount() > 0){
                        if(board[0][7].getComponent(0) instanceof Rook && ((Piece)board[0][7].getComponent(0)).getColor().equals(turn)){
                            if(board[0][6].isEmpty() && board[0][5].isEmpty() && board[0][4].isEmpty() && 
                                ((Rook)board[0][7].getComponent(0)).getMoves() == 0){
                                if(!(stillInCheck(SelectedPiece, board[0][6], board, turn) || stillInCheck(SelectedPiece, board[0][5], board, turn) || stillInCheck(SelectedPiece, board[0][4], board, turn))){
                                    return true;
                                }
                        }
                        }
                    }
                }
            }
        }
        return false;
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
