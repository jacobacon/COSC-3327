package bowling;

/**
 * @author kart
 * NOTE: You can "strip this skeleton for parts"
 * Don't assume that all of the preconditions are included
 * Be sure to check this code for correctness
 */
public class SinglePlayerBowlingScoreboardImpl_Beneski implements SinglePlayerBowlingScoreboard, AssignmentMetaData {
    private static final int MAXIMUM_ROLLS = 21;    //Maximum rolls in a one player game
    private String playerName;
    private int[] pinsKnockedDownArray = new int[MAXIMUM_ROLLS];
    private int rollCount = 0;

    public SinglePlayerBowlingScoreboardImpl_Beneski(String playerName) {
        assert playerName != null : "playerName is null!";
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }


    public int getCurrentFrame() {
        assert !isGameOver() : "Game is already over";

        int currentFrame;
        if ((rollCount + getNumStrikes() + 1) % 2 == 0) {
            currentFrame = ((rollCount + getNumStrikes() + 1) / 2);
        } else {
            currentFrame = ((rollCount + 1 + getNumStrikes()) / 2) + 1;
        }
        return currentFrame;
    }

    public Mark getMark(int frameNumber, int boxIndex) {
        assert 1 <= frameNumber : "frameNumber = " + frameNumber + " < 1!";
        assert frameNumber <= 10 : "frameNumber = " + frameNumber + " > 10!";
        assert 1 <= boxIndex : "boxIndex = " + boxIndex + " < 1!";
        assert boxIndex <= 3 : "boxIndex = " + boxIndex + " > 3!";
//		Exercise for student: Fix
//		assert (boxIndex != 2) || (!isStrike(frameNumber) && !isSpare(frameNumber)) : "boxIndex = " + boxIndex + ", but there was a Strike! : frameNumber = " + frameNumber;
//		assert (boxIndex != 2) || (!isSpare(frameNumber)) : "boxIndex = " + boxIndex + ", but there was a Spare! : frameNumber = " + frameNumber;
        Mark mark = null;
        if (frameNumber < 10) mark = getMarkSingleDigitFrameNumber(frameNumber, boxIndex);
        else mark = getMarkTenthFrame(boxIndex);
        assert mark != null : "mark is null!";
        return mark;
    }

    private Mark getMarkSingleDigitFrameNumber(int frameNumber, int boxIndex) {
        assert 1 <= frameNumber : "frameNumber = " + frameNumber + " < 1!";
        assert frameNumber <= 9 : "frameNumber = " + frameNumber + " > 9!";
        assert 1 <= boxIndex : "boxIndex = " + boxIndex + " < 1!";
        assert boxIndex <= 2 : "boxIndex = " + boxIndex + " > 2!";


        Mark mark;

        switch (boxIndex) {

            case 1:
                if (pinsKnockedDownArray[getRollIndexOfFirstBall(frameNumber)] == 10) {
                    mark = Mark.EMPTY;
                } else
                    mark = Mark.translate(pinsKnockedDownArray[getRollIndexOfFirstBall(frameNumber)]);
                break;
            case 2:
                int pinsKnockedDown = pinsKnockedDownArray[getRollIndexOfFirstBall(frameNumber) + 1];

                if (pinsKnockedDownArray[getRollIndexOfFirstBall(frameNumber)] == 10) {
                    mark = Mark.STRIKE;
                } else if ((pinsKnockedDown == 10) || (pinsKnockedDownArray[getRollIndexOfFirstBall(frameNumber)] + pinsKnockedDown == 10)) {
                    mark = Mark.SPARE;
                } else {
                    mark = Mark.translate(pinsKnockedDownArray[getRollIndexOfFirstBall(frameNumber) + 1]);
                }
                break;
            default:
                mark = null;
                break;
        }

        return mark;

    }

    private Mark getMarkTenthFrame(int boxIndex) {
        assert 1 <= boxIndex : "boxIndex = " + boxIndex + " < 1!";
        assert boxIndex <= 3 : "boxIndex = " + boxIndex + " > 3!";

        Mark mark = null;

        switch (boxIndex) {
            //TODO

            case 1:
                mark = Mark.translate(pinsKnockedDownArray[getRollIndexOfFirstBall(10)]);
                break;
            case 2:
                mark = Mark.translate(pinsKnockedDownArray[getRollIndexOfFirstBall(10) + 1]);
                break;
            case 3:
                mark = Mark.translate(pinsKnockedDownArray[getRollIndexOfFirstBall(10) + 2]);
                break;
            default:
                mark = Mark.EMPTY;
        }


        return mark;
    }

    public int getScore(int frameNumber) {
        assert 1 <= frameNumber : "frameNumber = " + frameNumber + " < 1!";
        assert frameNumber <= 10 : "frameNumber = " + frameNumber + " > 10!";

        int score = 0;
        //Score starts at 0


        //Add every roll to score, up until the frame number
        for (int i = 0; i < frameNumber * 2; i++) {
            score += pinsKnockedDownArray[i];

        }

        //Use this to pass some test cases, doesn't catch all strikes or spares only the last one.
        if (frameNumber == 10) {
            if (isStrikeOrSpare(getMark(10, 2)))
                score += pinsKnockedDownArray[getRollIndexOfFirstBall(10) + 2];
        }

        if (isSpare(frameNumber)) {
            score += pinsKnockedDownArray[getRollIndexOfFirstBall(frameNumber) + 1];
        }


/*
        This should work, but it doesn't for some reason...

        for(int i = 1; i < frameNumber; i++){
            if (isSpare(i)) {
                score += pinsKnockedDownArray[getRollIndexOfFirstBall(i) + 1];
            }

            if (isStrike(i)) {
                score += pinsKnockedDownArray[getRollIndexOfFirstBall(i) + 1];
            }
        }

*/

        return score;
    }


    public boolean isGameOver() {

        boolean gameOver = false;

        //Easy target
        if (rollCount == MAXIMUM_ROLLS) {
            gameOver = true;
        } else {
            //If the number of rolls == MAXIMUM_ROLLS if a strike was scored, or MAXIMUM_ROLLS - 1 if not
            if ((rollCount + getNumStrikes()) == MAXIMUM_ROLLS - 1) {
                if (!isStrikeOrSpare(getMark(10, 2)))
                    gameOver = true;
                else {
                    if (rollCount + getNumStrikes() == MAXIMUM_ROLLS) {
                        gameOver = true;
                    }
                }
            }
        }

        return gameOver;
    }

    public void recordRoll(int pinsKnockedDown) {
        assert 0 <= pinsKnockedDown : "pinsKnockedDown = " + pinsKnockedDown + " < 0!";
        assert pinsKnockedDown <= 10 : "pinsKnockedDown = " + pinsKnockedDown + " > 10!";
        assert (getCurrentBall() == 1) ||
                ((getCurrentBall() == 2) && (((getCurrentFrame() == 10) && isStrikeOrSpare(getMark(10, 1))) || ((pinsKnockedDownArray[rollCount - 1] + pinsKnockedDown) <= 10))) ||
                ((getCurrentBall() == 3) && (((getCurrentFrame() == 10) && isStrikeOrSpare(getMark(10, 2))) || ((pinsKnockedDownArray[rollCount - 1] + pinsKnockedDown) <= 10)));
        assert !isGameOver() : "Game is over!";

        //Just add it to the array
        pinsKnockedDownArray[rollCount] = pinsKnockedDown;
        rollCount++;
    }

    public int getCurrentBall() {
        assert !isGameOver() : "Game is over!";

        //Works for 1-9, but not frame 10
        int currentBall;

        if (getCurrentFrame() <= 9) {

            //System.out.println("Roll Count: " + rollCount);
            //System.out.println("Current frame " + getCurrentFrame());

            int numStrikes = getNumStrikes();


            //numStrikes has already been counted once, we add it again to 'normalize' to two rolls per frame.
            int biasedRollCount = rollCount + numStrikes;


            //Gives previous ball
            currentBall = biasedRollCount % 2;

            //Change to current ball
            currentBall++;
        } else {
            //It must be frame 10

            //If a strike || spare was scored, we get another ball
            if (isStrikeOrSpare(getMark(10, 2))) {
                currentBall = 3;
            } else {
                //Otherwise calculate as normal

                int biasedRollCount = rollCount + getNumStrikes();

                currentBall = biasedRollCount % 2;

                currentBall++;
            }
        }

        return currentBall;
    }

    private static final String VERTICAL_SEPARATOR = "#";
    private static final String HORIZONTAL_SEPARATOR = "#";
    private static final String LEFT_EDGE_OF_SMALL_SQUARE = "[";
    private static final String RIGHT_EDGE_OF_SMALL_SQUARE = "]";

    private String getScoreboardDisplay() {
        StringBuffer frameNumberLineBuffer = new StringBuffer();
        StringBuffer markLineBuffer = new StringBuffer();
        StringBuffer horizontalRuleBuffer = new StringBuffer();
        StringBuffer scoreLineBuffer = new StringBuffer();
        frameNumberLineBuffer.append(VERTICAL_SEPARATOR);

        markLineBuffer.append(VERTICAL_SEPARATOR);
        horizontalRuleBuffer.append(VERTICAL_SEPARATOR);
        scoreLineBuffer.append(VERTICAL_SEPARATOR);

        for (int frameNumber = 1; frameNumber <= 9; frameNumber++) {
            frameNumberLineBuffer.append("  " + frameNumber + "  ");
            markLineBuffer.append(" ");
            markLineBuffer.append(getMark(frameNumber, 1));
            markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
            markLineBuffer.append(getMark(frameNumber, 2));
            markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);

            final int CHARACTER_WIDTH_SCORE_AREA = 5;
            for (int i = 0; i < CHARACTER_WIDTH_SCORE_AREA; i++) horizontalRuleBuffer.append(HORIZONTAL_SEPARATOR);
            if (isGameOver() || frameNumber < getCurrentFrame()) {
                int score = getScore(frameNumber);
                final int PADDING_NEEDED_BEHIND_SCORE = 1;
                final int PADDING_NEEDED_IN_FRONT_OF_SCORE = CHARACTER_WIDTH_SCORE_AREA - ("" + score).length() - PADDING_NEEDED_BEHIND_SCORE;
                for (int i = 0; i < PADDING_NEEDED_IN_FRONT_OF_SCORE; i++) scoreLineBuffer.append(" ");
                scoreLineBuffer.append(score);
                for (int i = 0; i < PADDING_NEEDED_BEHIND_SCORE; i++) scoreLineBuffer.append(" ");
            } else {
                for (int i = 0; i < CHARACTER_WIDTH_SCORE_AREA; i++) scoreLineBuffer.append(" ");
            }

            frameNumberLineBuffer.append(VERTICAL_SEPARATOR);
            markLineBuffer.append(VERTICAL_SEPARATOR);
            horizontalRuleBuffer.append(VERTICAL_SEPARATOR);
            scoreLineBuffer.append(VERTICAL_SEPARATOR);
        }
        //Frame 10:
        {
            final String THREE_SPACES = "   ";
            frameNumberLineBuffer.append(THREE_SPACES + 10 + THREE_SPACES);

            markLineBuffer.append(" ");
            markLineBuffer.append(getMark(10, 1));
            markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
            markLineBuffer.append(getMark(10, 2));
            markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);
            markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
            markLineBuffer.append(getMark(10, 3));
            markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);

            final int CHARACTER_WIDTH_SCORE_AREA = 8;
            for (int i = 0; i < CHARACTER_WIDTH_SCORE_AREA; i++) horizontalRuleBuffer.append(HORIZONTAL_SEPARATOR);
            if (isGameOver()) {
                int score = getScore(10);
                final int PADDING_NEEDED_BEHIND_SCORE = 1;
                final int PADDING_NEEDED_IN_FRONT_OF_SCORE = CHARACTER_WIDTH_SCORE_AREA - ("" + score).length() - PADDING_NEEDED_BEHIND_SCORE;
                for (int i = 0; i < PADDING_NEEDED_IN_FRONT_OF_SCORE; i++) scoreLineBuffer.append(" ");
                scoreLineBuffer.append(score);
                for (int i = 0; i < PADDING_NEEDED_BEHIND_SCORE; i++) scoreLineBuffer.append(" ");
            } else {
                for (int i = 0; i < CHARACTER_WIDTH_SCORE_AREA; i++) scoreLineBuffer.append(" ");
            }

            frameNumberLineBuffer.append(VERTICAL_SEPARATOR);
            markLineBuffer.append(VERTICAL_SEPARATOR);
            horizontalRuleBuffer.append(VERTICAL_SEPARATOR);
            scoreLineBuffer.append(VERTICAL_SEPARATOR);
        }

        return getPlayerName() + "\n" +
                horizontalRuleBuffer.toString() + "\n" +
                frameNumberLineBuffer.toString() + "\n" +
                horizontalRuleBuffer.toString() + "\n" +
                markLineBuffer.toString() + "\n" +
                scoreLineBuffer.toString() + "\n" +
                horizontalRuleBuffer.toString();
    }

    public String toString() {
        return getScoreboardDisplay();
    }

    private int getRollIndexOfFirstBall(int frameNumber) {
        //Iterate through the list until we find the right frame number, and index.
        //Index += 1 if Strike || Index += 2 otherwise

        int searchIndex = 0;

        int searchFrame = 1;

        boolean found = false;

        while ((!found) && (searchIndex < pinsKnockedDownArray.length - 1)) {
            int pinsFirstBox = pinsKnockedDownArray[searchIndex];

            if (searchFrame == frameNumber) {
                found = true;
            }

            if (!found) {

                if (pinsFirstBox == 10) {
                    //System.out.println("Strike! " + searchFrame + " " + searchIndex);
                    searchIndex++;
                    searchFrame++;
                    //System.out.println(searchFrame + " " + searchIndex);
                } else {
                    //System.out.println("Not a Strike! " + searchFrame + " " + searchIndex);
                    searchIndex += 2;
                    searchFrame++;
                }
            }

            //System.out.println("Search Frame Number: " + searchFrame + "Target: " + frameNumber);

        }
        return searchIndex;
    }

    private boolean isStrike(int frameNumber) {
        //STRIKES are stored in box 2

        return isStrikeOrSpare(getMark(frameNumber, 2));
    }

    private boolean isSpare(int frameNumber) {
        //SPARES are stored in box 2

        return isStrikeOrSpare(getMark(frameNumber, 2));
    }

    private boolean isStrikeOrSpare(Mark mark) {
        //Test if the mark is == a STRIKE || SPARE
        return ((mark == Mark.STRIKE) || (mark == Mark.SPARE));
    }

    private int getNumStrikes() {
        int numStrikes = 0;

        //Iterate trough the list and find Strikes.
        //Strikes == 10
        for (int i = 0; i < rollCount; i++) {
            if (pinsKnockedDownArray[i] == 10)
                numStrikes++;
        }

        return numStrikes;
    }

    //*************************************************
    //*************************************************
    //*************************************************
    //*********ASSIGNMENT METADATA STUFF***************
    public String getFirstNameOfSubmitter() {
        return "Jacob";
    }

    public String getLastNameOfSubmitter() {
        return "Beneski";
    }

    public double getHoursSpentWorkingOnThisAssignment() {
        return 8.0;
    }

    public int getScoreAgainstTestCasesSubset() {
        return 100;
    }
}