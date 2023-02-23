package GAME;


import java.awt.Component;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.swing.ImageIcon;


public class Bot {

    //for use in the search method:
    String turn = "black";
    Stack<Move> moveStack = new Stack<>();
    int moves = 0;
    ArrayList<MovePair> bestMoves = new ArrayList<MovePair>();
    double beta = Double.NEGATIVE_INFINITY;
    
    public void play (Square[][] board){
        moves++;
        bestMoves.clear();
        turn = "black";
        moveStack.clear();

        search(board, 2);

        Random rand = new Random();
        int randomIndex = rand.nextInt(bestMoves.size());
        MovePair new_move = bestMoves.get(randomIndex);
        ((Piece)new_move.move.selectedPiece.getComponent(0)).makeMove(new_move.move.selectedPiece, new_move.move.clickedSquare, board);
    }
    
    public double search(Square[][] board, int depth){
        //base case
        if(depth == 0){
            return evaluatePosition(board, "black");
        }

        if(depth == 2){
            beta = Double.NEGATIVE_INFINITY;
        }
        double maxEval = Double.NEGATIVE_INFINITY;
        
        ArrayList<Move> moves = validMoves(generatePossMoves(board), board, turn);


        if(moves.size() == 0){
            //return neg infinity if checkmate, 0 otherwise
            for(int i = 0; i < 64; i++){
                if(board[i/8][i%8].getComponentCount() > 0){
                    if(((Piece)board[i/8][i%8].getComponent(0)).inCheck(board, turn)){
                        return Double.NEGATIVE_INFINITY;
                    }
                }
            }
            return 0;
        }
        for(Move move : moves){
            simulateMove(move);
            turn = (turn == "white") ? "black" : "white"; //update turn with a move
            double eval = -1 * search(board, depth - 1);
            undoSimulatedMove(move);
            turn = (turn == "white") ? "black" : "white"; //turn updated is undone
            if(eval >= maxEval){
                maxEval = eval;
                if(depth==2){
                    bestMoves.add(new MovePair(move, eval)); 
                }
            }
            if(depth == 2 && eval > beta){
                beta = eval;
            }
            if(depth == 1 && -eval < beta){
                return eval;
            }
        }
        if(depth==2){
            for(int i = 0; i < bestMoves.size(); i++){
                if(bestMoves.get(i).eval < maxEval){
                    bestMoves.remove(i);
                    i--;
                }
            }
        }
        return maxEval;
    }
    //makes a move and adds a copy of the squares involved to the move stack (subsquent changes to the move squares don't interfere)
    public void simulateMove(Move move){
        Move moveCopy = new Move(move);
        moveStack.add(moveCopy);
        move.clickedSquare.removeAll();
        move.clickedSquare.add(move.selectedPiece.getComponent(0));
        //special case of pawn promotion:
        if(move.clickedSquare.getComponent(0) instanceof Pawn){
            if(move.clickedSquare.getYPos() == 7 || move.clickedSquare.getYPos() == 0){
                String queen_color = ((Piece)move.clickedSquare.getComponent(0)).getColor();
                move.clickedSquare.removeAll();
                if(queen_color.equals("white")){
                    move.clickedSquare.add(new Queen(new ImageIcon("GAME/white_pieces_imgs/queenwhite.png"), queen_color));
                }
                else{
                    move.clickedSquare.add(new Queen(new ImageIcon("GAME/black_pieces_imgs/queenblack.png"), queen_color));
                }
            }
        }
    }
    //undos the simulated move by popping from the move stack, and undoing the move
    public void undoSimulatedMove(Move move){
        Move moveCopy = moveStack.pop();
        Square selectedPieceCopy = moveCopy.selectedPiece;
        Square clickedSquareCopy = moveCopy.clickedSquare;
        Piece savedSelected = (Piece)selectedPieceCopy.getComponent(0);
        Piece savedClicked = null;
        if(clickedSquareCopy.getComponentCount() > 0){
            savedClicked = (Piece)clickedSquareCopy.getComponent(0);
        }
        undoMove(move.selectedPiece, move.clickedSquare, savedSelected, savedClicked);
    }
    public void undoMove(Square SelectedPiece, Square clickedSquare, Component savedSelected, Component savedClicked){
        SelectedPiece.add(savedSelected);
        clickedSquare.removeAll();
        if(savedClicked != null){
            clickedSquare.add(savedClicked);
        }
    }
    
    public double evaluatePosition(Square[][] board, String color){
        double score = 0;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                if(board[i][j].getComponentCount() > 0){
                    int xDistance = Math.min(Math.abs(3-j),Math.abs(4-j));
                    int yDistance = Math.min(Math.abs(3-i),Math.abs(4-i));
                    double evaluation = evaluatePiece((Piece)board[i][j].getComponent(0));
                    if(!(board[i][j].getComponent(0) instanceof Rook || board[i][j].getComponent(0) instanceof King || board[i][j].getComponent(0) instanceof Queen)){
                        evaluation += (5.0/(xDistance + yDistance + 3.0)); 
                        if(moves > 20 && color == "white" && board[i][j].getComponent(0) instanceof Pawn){
                            evaluation += 3/(i+1);
                        }
                        if(moves > 20 && color == "black" && board[i][j].getComponent(0) instanceof Pawn){
                            evaluation += 3/(8-i);
                        }
                    } 
                    //endgame tactics
                    if(board[i][j].getComponent(0) instanceof King && ((Piece)board[i][j].getComponent(0)).getColor() != color){
                        evaluation += ((moves+1)/12)*(5.0/(xDistance + yDistance + 3.0));
                    }
                    if(!((Piece)board[i][j].getComponent(0)).getColor().equals(color)){
                        evaluation *= -1;
                    }
                    score += evaluation;
                }
            }
        }
        if(moves > 20){
            double sameKingX =0;
            double sameKingY =0;
            double diffKingX =0;
            double diffKingY =0;
            for(int k = 0; k < 64; k++){
                if(board[k/8][k%8].getComponentCount() > 0){
                    if(board[k/8][k%8].getComponent(0) instanceof King){
                        if(((Piece)board[k/8][k%8].getComponent(0)).getColor() == color){
                            sameKingX = k%8;
                            sameKingY = k/8;
                        }
                        else{
                            diffKingX = k%8;
                            diffKingY = k/8;
                        }
                    }   
                } 
            }
            score += 10/(Math.abs(sameKingX - diffKingX) + Math.abs(sameKingY - diffKingY));
        }
        return score;
    }

    public int evaluatePiece(Piece piece){
        if(piece instanceof Pawn){
            return 1;
        }
        if(piece instanceof Queen){
            return 9;
        }
        if(piece instanceof King){
            return 0;
        }
        if(piece instanceof Bishop){
            return 4;
        }
        if(piece instanceof Knight){
            return 3;
        }
        if(piece instanceof Rook){
            return 5;
        }
        return -1;
    }
    public ArrayList<Move> generatePossMoves(Square[][] board) {
        //stores all possible moves
        ArrayList<Move> possMoves = new ArrayList<Move>();
        for(int i = 0; i < 64; i++){
            for(int j = 0; j < 64; j++){
                possMoves.add(new Move(board[i/8][i%8], board[j/8][j%8]));
            }
        }
        return possMoves;
    }
    public ArrayList<Move> validMoves(ArrayList<Move> possMoves, Square[][] board, String color){
        ArrayList<Move> validMoves = new ArrayList<Move>();
        for(Move move : possMoves){
            if(move.selectedPiece.getComponentCount() > 0){
                if(((Piece)move.selectedPiece.getComponent(0)).moveValidate(move.selectedPiece, move.clickedSquare, board, color)){
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }

    public static class MovePair {
        Move move;
        double eval;
        public MovePair(Move move, double eval){
            this.move = move;
            this.eval = eval;
        }
    }
    public static class Move {
        Square selectedPiece;
        Square clickedSquare;
        public Move(Square selectedPiece, Square clickedSquare){
            this.selectedPiece = selectedPiece;
            this.clickedSquare = clickedSquare;
        }
        public Move(Move move){
            Square selected = new Square(move.selectedPiece.getXPos(), move.selectedPiece.getYPos());
            Square clicked = new Square(move.clickedSquare.getXPos(), move.clickedSquare.getYPos());
            //fill selected with right piece
            if(move.selectedPiece.getComponent(0) instanceof Pawn){
                selected.add(new Pawn(((Piece)move.selectedPiece.getComponent(0)).getThisIcon(), ((Piece)move.selectedPiece.getComponent(0)).getColor()));
                ((Pawn)selected.getComponent(0)).setMoves(((Pawn)move.selectedPiece.getComponent(0)).getMoves());
            }
            if(move.selectedPiece.getComponent(0) instanceof Rook){
                selected.add(new Rook(((Piece)move.selectedPiece.getComponent(0)).getThisIcon(), ((Piece)move.selectedPiece.getComponent(0)).getColor()));
                ((Rook)selected.getComponent(0)).setMoves(((Rook)move.selectedPiece.getComponent(0)).getMoves());
            }
            if(move.selectedPiece.getComponent(0) instanceof Queen){
                selected.add(new Queen(((Piece)move.selectedPiece.getComponent(0)).getThisIcon(), ((Piece)move.selectedPiece.getComponent(0)).getColor()));
            }
            if(move.selectedPiece.getComponent(0) instanceof King){
                selected.add(new King(((Piece)move.selectedPiece.getComponent(0)).getThisIcon(), ((Piece)move.selectedPiece.getComponent(0)).getColor()));
                ((King)selected.getComponent(0)).setMoves(((King)move.selectedPiece.getComponent(0)).getMoves());
            }
            if(move.selectedPiece.getComponent(0) instanceof Bishop){
                selected.add(new Bishop(((Piece)move.selectedPiece.getComponent(0)).getThisIcon(), ((Piece)move.selectedPiece.getComponent(0)).getColor()));
            }
            if(move.selectedPiece.getComponent(0) instanceof Knight){
                selected.add(new Knight(((Piece)move.selectedPiece.getComponent(0)).getThisIcon(), ((Piece)move.selectedPiece.getComponent(0)).getColor()));
            }
            //fill clicked with the right piece
            if(move.clickedSquare.getComponentCount()>0){
                if(move.clickedSquare.getComponent(0) instanceof Pawn){
                    clicked.add(new Pawn(((Piece)move.clickedSquare.getComponent(0)).getThisIcon(), ((Piece)move.clickedSquare.getComponent(0)).getColor()));
                    ((Pawn)clicked.getComponent(0)).setMoves(((Pawn)move.clickedSquare.getComponent(0)).getMoves());
                }
                if(move.clickedSquare.getComponent(0) instanceof Rook){
                    clicked.add(new Rook(((Piece)move.clickedSquare.getComponent(0)).getThisIcon(), ((Piece)move.clickedSquare.getComponent(0)).getColor()));
                    ((Rook)clicked.getComponent(0)).setMoves(((Rook)move.clickedSquare.getComponent(0)).getMoves());
                }
                if(move.clickedSquare.getComponent(0) instanceof Queen){
                    clicked.add(new Queen(((Piece)move.clickedSquare.getComponent(0)).getThisIcon(), ((Piece)move.clickedSquare.getComponent(0)).getColor()));
                }
                if(move.clickedSquare.getComponent(0) instanceof King){
                    clicked.add(new King(((Piece)move.clickedSquare.getComponent(0)).getThisIcon(), ((Piece)move.clickedSquare.getComponent(0)).getColor()));
                    ((King)clicked.getComponent(0)).setMoves(((King)move.clickedSquare.getComponent(0)).getMoves());
                }
                if(move.clickedSquare.getComponent(0) instanceof Bishop){
                    clicked.add(new Bishop(((Piece)move.clickedSquare.getComponent(0)).getThisIcon(), ((Piece)move.clickedSquare.getComponent(0)).getColor()));
                }
                if(move.clickedSquare.getComponent(0) instanceof Knight){
                    clicked.add(new Knight(((Piece)move.clickedSquare.getComponent(0)).getThisIcon(), ((Piece)move.clickedSquare.getComponent(0)).getColor()));
                }
            }
            this.selectedPiece = selected;
            this.clickedSquare = clicked;
        }
    }
}
