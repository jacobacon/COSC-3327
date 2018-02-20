package tictactoe;

public class TicTacToeBoardImpl_Beneski implements TicTacToeBoard{

    protected static final int NO_MOVE = -1;
    protected static final int NO_MATCH = -1;

    protected int[] movesArray;

    //Create an empty TicTacToe Card
    public TicTacToeBoardImpl_Beneski(){
        final int CELL_COUNT = ROW_COUNT * COLUMN_COUNT;
        movesArray = new int[CELL_COUNT];
        for(int i = 0; i < CELL_COUNT; i++){
            movesArray[i] = NO_MOVE;
        }
    }

    //Returns the mark at a specific location
    public Mark getMark(int row, int column) {

        assert(row < ROW_COUNT);
        assert(column < COLUMN_COUNT);
        assert( 0 <= row);
        assert( 0 <= column);

        //0,0 - 0
        //0,1 - 1
        //0,2 - 2

        //1,0 - 3
        //1,1 - 4
        //1,2 - 5

        //2,0 - 6
        //2,1 - 7
        //2,2 - 8


        int searchIndex = convertArrayLocation(row,column);

        int location = NO_MATCH;

        //Find the desired search in moves array.
        for(int i = 0; i < movesArray.length; i++){
            if((movesArray[i]) == searchIndex){
                location = i;
            }

        }

        Mark mark = null;

        if(location != NO_MATCH) {
            if ((location % 2) == 0)
                mark = Mark.X;
            else
                mark = Mark.O;
        }

        return mark;
    }

    //Sets a mark at a specific location
    public void setMark(int row, int column) {
        assert(row < ROW_COUNT);
        assert(column < COLUMN_COUNT);
        assert( 0 <= row);
        assert( 0 <= column);

        int playIndex = NO_MATCH;

        int arrayIndex = convertArrayLocation(row, column);

        for(int i = 0; i < movesArray.length; i++){
            if(movesArray[i] == NO_MOVE){
                playIndex = i;
            }

        }

        movesArray[playIndex] = arrayIndex;

    }

    //Returns the next players turn
    public Mark getTurn() {

        int playLocation = NO_MATCH;

        for(int i = 0; i < movesArray.length; i++){
            if(movesArray[i] == NO_MOVE){
                playLocation = i;
            }
        }

        if((playLocation % 2) == 0){
            return Mark.X;
        }

        return Mark.O;

    }

    //Check if the game is over
    public boolean isGameOver() {

        boolean gameOver = true;

        //Check if there is a winner. If there is, the game is over.
        Mark mark = getWinner();

        if(mark == null)
            gameOver = false;

        //Check if all spaces are filled.
        if(mark == null) {

            //Check if there are still moves able to be made.
            for (int i = 0; i < movesArray.length; i++) {
                if (movesArray[i] == NO_MOVE) {
                    gameOver = false;
                }
            }
        }

        return gameOver;
    }

    //Returns the winner
    public Mark getWinner() {

        Mark mark = null;

        //Horizontal
        if(getMark(0,0) == getMark(0,1) && getMark(0,1) == getMark(0,2)){
            mark = getMark(0,0);
        }

        if(getMark(1,0) == getMark(1,1) && getMark(1,1) == getMark(1,2)){
            mark = getMark(1,0);
        }
        if(getMark(2,0) == getMark(2,1) && getMark(2,1) == getMark(2,2)){
            mark = getMark(2,0);
        }

        //Vertical
        if(getMark(0,0) == getMark(1,0) && getMark(1,0) == getMark(2,0)){
            mark = getMark(0,0);
        }

        if(getMark(0,1) == getMark(1,1) && getMark(1,1) == getMark(2,1)){
            mark = getMark(0,1);
        }

        if(getMark(0,2) == getMark(1,2) && getMark(1,2) == getMark(2,2)){
            mark = getMark(0,2);
        }

        //Diag \
        if(getMark(0,0) == getMark(1,1) && getMark(1,1) == getMark(2,2)){
            mark = getMark(0,0);
        }

        //Diag /
        if(getMark(2,0) == getMark(1,1) && getMark(1,1) == getMark(0,2)){
            mark = getMark(2,0);
        }

        return mark;
    }

    //Converts the row col positions to a moves array index
    private int convertArrayLocation(int row, int column){


        //0,0 - 0
        //0,1 - 1
        //0,2 - 2

        //1,0 - 3
        //1,1 - 4
        //1,2 - 5

        //2,0 - 6
        //2,1 - 7
        //2,2 - 8

        int arrayPosition = NO_MATCH;


        switch(row){

            case 0:
                switch(column){
                    case 0:
                        arrayPosition = 0;
                        break;
                    case 1:
                        arrayPosition = 1;
                        break;
                    case 2:
                        arrayPosition = 2;
                        break;
                }
                break;
            case 1:
                switch(column){
                    case 0:
                        arrayPosition = 3;
                        break;
                    case 1:
                        arrayPosition = 4;
                        break;
                    case 2:
                        arrayPosition = 5;
                        break;
                }
                break;
            case 2:
                switch(column){
                    case 0:
                        arrayPosition = 6;
                        break;
                    case 1:
                        arrayPosition = 7;
                        break;
                    case 2:
                        arrayPosition = 8;
                        break;
                }
                break;

            default:
                arrayPosition = NO_MATCH;
                break;

        }

        return arrayPosition;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();


        String newLineString = "\n";


        for(int i = 0; i < ROW_COUNT; i++){
            String tempString = "";

            for(int j = 0; j < COLUMN_COUNT; j++){

                if(getMark(i,j) == null){
                    tempString += " ";
                } else {
                    tempString += getMark(i,j);
                }

                if(j < COLUMN_COUNT - 1)
                    tempString += "|";
            }

            stringBuilder.append(tempString);
            stringBuilder.append(newLineString);
            if(i< ROW_COUNT - 1)
                stringBuilder.append("-----");
            stringBuilder.append(newLineString);

        }
        return stringBuilder.toString();

    }
}
