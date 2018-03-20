package tournament;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BracketImpl_Beneski<P> extends BracketAbstract<P> {
    public BracketImpl_Beneski(List<P> participantMatchups) {
        super(participantMatchups);
    }

    @Override
    public int getMaxLevel() {
        int unique = getNumberUniqueParticipants();
        //The top level is the sqrt of the unique participants. The total number of levels is max+1.
        return (int) Math.sqrt(unique);
    }

    @Override
    public Set<Set<P>> getGroupings(int level) {
        assert level < getMaxLevel() : "Level: " + level + " Must be <= Max Level: " + getMaxLevel();
        Set<Set<P>> returnSet = new HashSet<>();

        int numParticipants = getNumberUniqueParticipants();

        int numGroupings = ((numParticipants / (int) Math.pow(2, level)));

        int groupingSize = numParticipants / numGroupings;

        int groupingCount = 0;

        int index = predictions.size() - numParticipants;

        while (groupingCount < numGroupings) {
            System.out.println("Grouping: " + groupingCount);
            Set<P> tempSet = new HashSet<>();
            int participantCount = 0;
            while (participantCount < groupingSize) {
                System.out.println("Participant: " + participantCount + " " + predictions.get(index));
                tempSet.add(predictions.get(index));
                participantCount++;
                index++;
            }

            returnSet.add(tempSet);
            groupingCount++;
        }


        return returnSet;
    }

    @Override
    public Set<P> getViableParticipants(Set<P> grouping) {
        //Get last index, check if there is a winning parent index, delete any that don't exist under a winner

        List<P> tempList = new ArrayList<>(grouping);

        List<P> returnList = new ArrayList<>(tempList);

        for (P participant: tempList) {
            boolean eliminated = false;
            int level = 0;
            int index = getParentIndex(getStartingParticipantIndex(participant));
            while(!eliminated && level < getMaxLevel()){
                System.out.println(participant + " at: " + index);
                System.out.println(getStartingParticipantIndex(participant));
                if((predictions.get(index) != participant) && (predictions.get(index) != null)){
                    returnList.remove(participant);
                }
                index = getParentIndex(index);
            }
        }

        return new HashSet<>(returnList);
    }

    @Override
    public void setPredictedWinCount(P participant, int exactWinCount) {
        assert participant != null;
        assert predictions.contains(participant);
        assert exactWinCount >= 0;
        assert exactWinCount <= getMaxLevel();
        for (int i = 0; i < exactWinCount; i++) {
            int newIndex = getParentIndex(getParticipantIndex(participant));

            //System.out.println("Setting a win for: " + participant + " at: " + newIndex);
            predictions.set(newIndex, participant);
        }
    }

    //Find two groupings a and b at a lower level such that a U b = grouping with a INT b = empty
    private Set<Set<P>> getSubordinateGroupings(Set<P> grouping) {
        assert grouping.size() > 1 : "grouping.size() = " + grouping.size() + " <= 1!: grouping = " + grouping;
        throw new RuntimeException("NOT IMPLEMENTED!");
    }

    private int getStartingParticipantIndex(P participant){
        assert(predictions.contains(participant)) : "Participant Must be included in predictions";
        int startingIndex = predictions.size() - getNumberUniqueParticipants();
        int index = 0;

        for(int i = startingIndex; i < predictions.size(); i++){
            if(predictions.get(i) == participant)
                index = i;
        }
        return index;
    }

    private int getParticipantIndex(P participant) {
        /* This code didn't work right for some reason. Lets use a built-in method!

		int index = 0;
		for(int i = 0; i < predictions.size(); i++){
			System.out.println(predictions.get(i));
			if(predictions.get(i) == participant) {
				index = i;
			}
		}
		System.out.println("Index of participant: " + index);
		return index;
		*/
        assert(predictions.contains(participant)) : "Participant Must be included in predictions";
        int index = predictions.indexOf(participant);
        System.out.println("Index of part: " + index);
        return index;
    }

    private static int getParentIndex(int childIndex) {
        return ((childIndex + 1) / 2) - 1;
    }

    private Set<P> getGrouping(P member, int level) {
        throw new RuntimeException("NOT IMPLEMENTED!");
    }

    private int getNumberUniqueParticipants() {
        //Get a list of all the unique participants in the predictions list. We don't care about dupes, or null, so throw them away.
        List<P> unique = predictions.stream().distinct().collect(Collectors.toList());
        unique.remove(null);
        return unique.size();
    }

    @Override
    public String toString() {
        //TODO
        StringBuilder stringBuilder = new StringBuilder();
/*
        for(int i = 0; i < predictions.size(); i++){
			stringBuilder.append(predictions.get(i));
		}

		return stringBuilder.toString();
		*/

        int numParticipants = getNumberUniqueParticipants();

        int index = 0;

        for (int i = getMaxLevel(); i >= 0; i--) {

            int groupingSize = numParticipants / (int) Math.pow(2, i);

            //System.out.println(groupingSize);

            stringBuilder.append("\n");

            for (int j = 0; j < groupingSize; j++) {
                if (j != 0)
                    stringBuilder.append(" ");
                stringBuilder.append(predictions.get(index));
                index++;
            }
        }


        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        //TODO
        if (!(obj instanceof Bracket))
            return false;
        else {
            boolean equal = false;

            Bracket<P> otherBracket = (Bracket<P>) obj;

            int otherMaxLevel = otherBracket.getMaxLevel();
            if(getMaxLevel() != otherMaxLevel){
                return false;
            }

            for(int i = 0; i < getMaxLevel(); i++){
                if(getGroupings(i).containsAll(otherBracket.getGroupings(i))){
                    equal = true;
                }
            }


            return equal;
        }
    }

}
