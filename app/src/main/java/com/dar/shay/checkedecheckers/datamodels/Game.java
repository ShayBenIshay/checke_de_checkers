package com.dar.shay.checkedecheckers.datamodels;

import java.util.Arrays;

class Player {
    boolean is_black;
    boolean is_human;

    Player(boolean is_black, boolean is_human) {
        this.is_black = is_black;
        this.is_human = is_human;
    }
}

public class Game {

    public final static int BOARD_SIZE = 8;

    Square[][] board;
    Player black_player;
    Player white_player;

    boolean is_black_turn = true;
    private Point waitingPoint = null;


    public Square[][] getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return "Game{" +
                "board=" + Arrays.toString(board) +
                ", black_player=" + black_player +
                ", white_player=" + white_player +
                '}';
    }

    public Game(boolean is_black_start) {
        board = getStartBoard();
        this.is_black_turn = is_black_start;
    }

    private Square[][] getStartBoard() {
        Square[][] new_board = new Square[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i = i + 2) {
            //setting the matrix to default values
            //line 0
            new_board[0][i] = Square.WHITE_SQUARE;
            new_board[0][i + 1] = Square.WHITE_SOLDIER;
            //line 1
            new_board[1][i] = Square.WHITE_SOLDIER;
            new_board[1][i + 1] = Square.WHITE_SQUARE;
            //line 2
            new_board[2][i] = Square.WHITE_SQUARE;
            new_board[2][i + 1] = Square.WHITE_SOLDIER;
            //line 3
            new_board[3][i] = Square.BLACK_SQUARE;
            new_board[3][i + 1] = Square.WHITE_SQUARE;
            //line 4
            new_board[4][i] = Square.WHITE_SQUARE;
            new_board[4][i + 1] = Square.BLACK_SQUARE;
            //line 5
            new_board[5][i] = Square.BLACK_SOLDIER;
            new_board[5][i + 1] = Square.WHITE_SQUARE;
            //line 6
            new_board[6][i] = Square.WHITE_SQUARE;
            new_board[6][i + 1] = Square.BLACK_SOLDIER;
            //line 7
            new_board[7][i] = Square.BLACK_SOLDIER;
            new_board[7][i + 1] = Square.WHITE_SQUARE;
        }
        return new_board;
    }

    Square getSquare(Point p) {
        return board[p.x][p.y];
    }

    void removeSoldier(Point p) {
        //assume's that the square is BLACK_SOLDIER / WHITE_SOLDIER
        board[p.x][p.y] = Square.BLACK_SQUARE;
    }

    public TilePickResult move(Point source, Point destination) {
        TilePickResult tilePick_result_status = ValidateMove(source, destination, is_black_turn);
        if (tilePick_result_status == TilePickResult.INVALID_MOVE)
            return TilePickResult.INVALID_MOVE;

        removeSoldier(source);
        if (is_black_turn) {
            board[destination.x][destination.y] = Square.BLACK_SOLDIER;
        } else {
            board[destination.x][destination.y] = Square.WHITE_SOLDIER;
        }
        if (tilePick_result_status == TilePickResult.EAT) {
            int target_x = (source.x + destination.x) / 2;
            int target_y = (source.y + destination.y) / 2;
            removeSoldier(new Point(target_x, target_y));
        }
        is_black_turn = !is_black_turn;
        return tilePick_result_status;
    }

    TilePickResult ValidateMove(Point source, Point destination, boolean is_black) {
        int diffX = Math.abs(source.x - destination.x);
        int diffY = Math.abs(source.y - destination.y);
        if (diffX != diffY) return TilePickResult.INVALID_MOVE;
        if (is_black) {
            if (board[source.x][source.y] != Square.BLACK_SOLDIER)
                return TilePickResult.INVALID_MOVE;
        } else {
            if (board[source.x][source.y] != Square.WHITE_SOLDIER)
                return TilePickResult.INVALID_MOVE;
        }
        //diffX == diffY
        if (diffX == 1 && board[destination.x][destination.y] == Square.BLACK_SQUARE)
            return TilePickResult.STEP;
        else if (diffX == 2) {
            int target_x = (source.x + destination.x) / 2;
            int target_y = (source.y + destination.y) / 2;
            if (is_black && board[target_x][target_y] != Square.WHITE_SOLDIER)
                return TilePickResult.INVALID_MOVE;
            if (!is_black && board[target_x][target_y] != Square.BLACK_SOLDIER)
                return TilePickResult.INVALID_MOVE;
            return TilePickResult.EAT;
        } else return TilePickResult.INVALID_MOVE;

    }

    /**
     * Handling a tile click
     *
     * @param tile - the tile clicked
     * @return The result of the click
     */
    public TilePickResult tilePick(Point tile) {
        if (waitingPoint != null) {
            TilePickResult move_result = move(waitingPoint, tile);
            if (move_result != TilePickResult.INVALID_MOVE)
                waitingPoint = null;
            return move_result;
        }
        //
        else {
            if (!checkIfCorrectSoldier(tile))
                return TilePickResult.INVALID_MOVE;
            waitingPoint = tile;
            return TilePickResult.SOLDIER_PICK;
        }
    }

    private boolean checkIfCorrectSoldier(Point tile) {
        Square soldier_type = is_black_turn ? Square.BLACK_SOLDIER : Square.WHITE_SOLDIER;
        return board[tile.x][tile.y] == soldier_type;
    }

}
/*
*  int diffX,diffY;
        //checking if s is a valid soldier and p is an empty square
        if( s == nullptr || !(this->is_empty_square(p))){
            return false;
        }
        diffY = abs(p.gety()-s->p.gety());
        diffX = abs(p.getx()-s->p.getx());
        //movement is always diagonal
        if(diffX != diffY){
            return false;
        }
        if( diffY == 1){
            return true;
        }
        if(diffY == 2){
            if(diffX == 2){
            int x = (p.getx()+s->p.getx())/2;
            int y = (p.gety()+s->p.gety())/2;
            Coordinate predicate_soldier(x,y);
            return valid_pred(cur_player,p);
            }
        }
        return false;*/