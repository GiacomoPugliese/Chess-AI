package BOARD;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class Board {
    Bot bot = new Bot();
    JFrame frame;
    Square board[][] = new Square[8][8];
    Square selectedPiece = null;

    public Board() {
        frame = new JFrame("Chess vs AI");
        frame.setSize(500, 500);
        frame.setLayout(new GridLayout(8, 8));

        // initialize each square as a Square object with x and y coords, and assign it a color
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square(j, i);
                board[i][j].addMouseListener(new MouseAdapter() {
                    //add a mouse listener to each JPanel, and allow for piece movement
                    @Override
                    public void mousePressed(MouseEvent e) {
                        //stores the object of the last square clicked
                        Square clickedSquare = (Square) e.getSource();

                        //if no square is selected, updated the selected square
                        if (selectedPiece == null && clickedSquare.getComponentCount() > 0) {
                            selectedPiece = clickedSquare;
                        } 

                        //if a square has already been selected, move the selected square to current square
                        else if (selectedPiece != null) {
                            if (((Piece) selectedPiece.getComponent(0)).moveValidate(selectedPiece, clickedSquare, board)) {
                                ((Piece)selectedPiece.getComponent(0)).makeMove(selectedPiece, clickedSquare, board);
                                //update the board and turn after moving a piece
                                frame.revalidate();
                                frame.repaint();
                                //bot moves
                                bot.play(board);
                                frame.revalidate();
                                frame.repaint();
                            }
                            selectedPiece = null;
                        } 


                    }
                });

                if ((i + j) % 2 == 0) {
                    board[i][j].setBackground(Color.orange);
                } else {
                    board[i][j].setBackground(Color.white);
                }   
                frame.add(board[i][j]);
            }
        }

        board[0][0].add(new Rook(new ImageIcon("BOARD/black_pieces_imgs/rookblack.png"), "black"));
        board[0][1].add(new Knight(new ImageIcon("BOARD/black_pieces_imgs/knightblack.png"), "black"));
        board[0][2].add(new Bishop(new ImageIcon("BOARD/black_pieces_imgs/bishopblack.png"), "black"));
        board[0][3].add(new King(new ImageIcon("BOARD/black_pieces_imgs/kingblack.png"), "black"));
        board[0][4].add(new Queen(new ImageIcon("BOARD/black_pieces_imgs/queenblack.png"), "black"));
        board[0][5].add(new Bishop(new ImageIcon("BOARD/black_pieces_imgs/bishopblack.png"), "black"));
        board[0][6].add(new Knight(new ImageIcon("BOARD/black_pieces_imgs/knightblack.png"), "black"));
        board[0][7].add(new Rook(new ImageIcon("BOARD/black_pieces_imgs/rookblack.png"), "black"));

        board[7][0].add(new Rook(new ImageIcon("BOARD/white_pieces_imgs/rookwhite.png"), "white"));
        board[7][1].add(new Knight(new ImageIcon("BOARD/white_pieces_imgs/knightwhite.png"), "white"));
        board[7][2].add(new Bishop(new ImageIcon("BOARD/white_pieces_imgs/bishopwhite.png"), "white"));
        board[7][3].add(new King(new ImageIcon("BOARD/white_pieces_imgs/kingwhite.png"), "white"));
        board[7][4].add(new Queen(new ImageIcon("BOARD/white_pieces_imgs/queenwhite.png"), "white"));
        board[7][5].add(new Bishop(new ImageIcon("BOARD/white_pieces_imgs/bishopwhite.png"), "white"));
        board[7][6].add(new Knight(new ImageIcon("BOARD/white_pieces_imgs/knightwhite.png"), "white"));
        board[7][7].add(new Rook(new ImageIcon("BOARD/white_pieces_imgs/rookwhite.png"), "white"));

        for (int i = 0; i < 8; i++) {
            board[1][i].add(new Pawn(new ImageIcon("BOARD/black_pieces_imgs/pawnblack.png"), "black"));
            board[6][i].add(new Pawn(new ImageIcon("BOARD/white_pieces_imgs/pawnwhite.png"), "white"));
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
    public static void main(String[] args) {
        new Board();
    }
}
