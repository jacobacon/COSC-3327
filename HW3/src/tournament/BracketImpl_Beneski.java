package tournament;


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
        //Gets the maximum level
        int unique = getNumberUniqueParticipants();
        //The top level is the sqrt of the unique participants. The total number of levels is max+1.
        return (int) Math.sqrt(unique);
    }

    @Override
    public Set<Set<P>> getGroupings(int level) {
        assert level < getMaxLevel() : "Level: " + level + " Must be <= Max Level: " + getMaxLevel();
        Set<Set<P>> returnSet = new HashSet<>();

        //Get the right number of groupings and how big they are based on level.
        int numParticipants = getNumberUniqueParticipants();

        int numGroupings = ((numParticipants / (int) Math.pow(2, level)));

        int groupingSize = numParticipants / numGroupings;

        int groupingCount = 0;

        int index = predictions.size() - numParticipants;

        //While there are more groupings to make.
        while (groupingCount < numGroupings) {
            //System.out.println("Grouping: " + groupingCount);
            Set<P> tempSet = new HashSet<>();
            int participantCount = 0;
            //Iterate through predictions
            while (participantCount < groupingSize) {
                //System.out.println("Participant: " + participantCount + " " + predictions.get(index));
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

        boolean found = false;
        for (int i = 0; i < getMaxLevel(); i++) {
            if (getGroupings(i).contains(grouping)) {
                found = true;
            }
        }

        assert (found) : "The grouping: " + grouping + " Wasn't found.";


        List<P> tempList = new ArrayList<>(grouping);

        List<P> returnList = new ArrayList<>(tempList);

        //For each participant, check if they won or lost.
        for (P participant : tempList) {
            boolean eliminated = false;

            int numGroups = getNumberUniqueParticipants() / grouping.size();

            int a = getNumberUniqueParticipants() / numGroups;

            double maxLevel = Math.log(a) / Math.log(2);

            //System.out.println("Max Level: " + maxLevel);

            int level = 0;
            int index = getParentIndex(getStartingParticipantIndex(participant));
            while (!eliminated && level < maxLevel) {
                //System.out.println(participant + " at: " + index);
                //System.out.println(getStartingParticipantIndex(participant));
                //System.out.println(predictions.get(index));


                if ((predictions.get(index) != participant) && (predictions.get(index) != null)) {
                    returnList.remove(participant);
                    eliminated = true;
                }
                index = getParentIndex(index);
                level++;
            }
        }

        return new HashSet<>(returnList);
    }

    @Override
    public void setPredictedWinCount(P participant, int exactWinCount) {
        assert participant != null : "Participant can't be null";
        assert predictions.contains(participant) : "Predictions list doesn't contain participant";
        assert exactWinCount >= 0 : "Exact win must be >= 0";
        assert exactWinCount <= getMaxLevel() : "Exact win must be <= max level";

        //For each participant set a win at the parent index.
        for (int i = 0; i < exactWinCount; i++) {
            int newIndex = getParentIndex(getParticipantIndex(participant));

            //System.out.println("Setting a win for: " + participant + " at: " + newIndex);
            predictions.set(newIndex, participant);
        }
    }

    private int getStartingParticipantIndex(P participant) {
        //Get index of participant on the base level.
        assert (predictions.contains(participant)) : "Participant Must be included in predictions";
        int startingIndex = predictions.size() - getNumberUniqueParticipants();
        int index = 0;

        for (int i = startingIndex; i < predictions.size(); i++) {
            if (predictions.get(i) == participant)
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

        //Get the first index of participant
        assert (predictions.contains(participant)) : "Participant Must be included in predictions";
        int index = predictions.indexOf(participant);
        //System.out.println("Index of part: " + index);
        return index;
    }

    private static int getParentIndex(int childIndex) {
        return ((childIndex + 1) / 2) - 1;
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

            if (getMaxLevel() != otherBracket.getMaxLevel()) {
                return false;
            }

            for (int i = 0; i < getMaxLevel(); i++) {
                if (getGroupings(i).equals(otherBracket.getGroupings(i))) {
                    equal = true;
                }

                List<Set<P>> groupings = new ArrayList<>(getGroupings(i));

                List<Set<P>> otherGroupings = new ArrayList<>(otherBracket.getGroupings(i));

                if (groupings.size() != otherGroupings.size()) {
                    return false;
                }

                for (int j = 0; j < groupings.size(); j++) {
                    Set<P> group = groupings.get(j);

                    if (!otherGroupings.contains(group)) {
                        return false;
                    }
                }

                for (int j = 0; j < groupings.size(); j++) {
                    Set<P> group = groupings.get(j);
                    Set<P> otherGroup = otherGroupings.get(j);

                    if (getViableParticipants(group) == otherBracket.getViableParticipants(otherGroup)) {
                        equal = true;
                    }

                }
            }


            return equal;
        }
    }

}
