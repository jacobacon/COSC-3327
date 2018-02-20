package bowling;


import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.junit.Assert.assertEquals;

public class JacobTestCases {

    protected static SinglePlayerBowlingScoreboard singlePlayerBowlingScoreboard;
    protected static final String BOWLER_NAME = "Jacob Beneski";

    @Before
    public void initBeforeEachTestMethod()
    {
        singlePlayerBowlingScoreboard = new SinglePlayerBowlingScoreboardImpl_Beneski(BOWLER_NAME);
    }


    @Test(expected=AssertionError.class)
    public void assertionsEnabledTest()
    {
        assert false;
        throw new RuntimeException("ENABLE ASSERTIONS IN RUN CONFIGURATIONS!");
    }

    @Test
    public void threeRolls(){
        assertEquals(1, singlePlayerBowlingScoreboard.getCurrentBall());
        assertEquals(1, singlePlayerBowlingScoreboard.getCurrentFrame());


        singlePlayerBowlingScoreboard.recordRoll(5);

        assertEquals(2,singlePlayerBowlingScoreboard.getCurrentBall());
        assertEquals(1, singlePlayerBowlingScoreboard.getCurrentFrame());

        singlePlayerBowlingScoreboard.recordRoll(3);

        assertEquals(1,singlePlayerBowlingScoreboard.getCurrentBall());
        assertEquals(2, singlePlayerBowlingScoreboard.getCurrentFrame());

        singlePlayerBowlingScoreboard.recordRoll(2);


    }

}
