package tictactoe;

public class TestDriver {

    public static void main(String[] args){

        TicTacToeBoardImpl_Beneski ticTacToeBoard = new TicTacToeBoardImpl_Beneski();

        //0,0 - 0
        //0,1 - 1
        //0,2 - 2

        //1,0 - 3
        //1,1 - 4
        //1,2 - 5

        //2,0 - 6
        //2,1 - 7
        //2,2 - 8


        //X
        ticTacToeBoard.setMark(1,1);
        //System.out.println(ticTacToeBoard.getTurn());

        //O
        ticTacToeBoard.setMark(2,2);


        //System.out.println(ticTacToeBoard.isGameOver());

        System.out.println(ticTacToeBoard.toString());

        System.out.println(ticTacToeBoard.getWinner());

        System.out.println(ticTacToeBoard.isGameOver());



    }
}
