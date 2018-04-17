package dartboard;


import combinatorics.Permutation;


import java.util.Set;

public class DartboardImpl_Beneski implements Dartboard {

    int topNumber;
    Permutation<Integer> permutation;

    public DartboardImpl_Beneski(Permutation<Integer> p, int topmostNumber){
        assert p.getDomain().contains(topmostNumber) : "Top Number not in Domain of Permutation";
        permutation = p;
        this.topNumber = topmostNumber;
    }

    @Override
    public int getTopmostNumber() {
        return topNumber;
    }

    @Override
    public Set<Integer> getNumbers() {
        return permutation.getDomain();
    }

    @Override
    public int getClockwiseAdjacentNumber(int number) {
        assert permutation.getDomain().contains(number) : "Number not in domain!";
        return permutation.getImage(number);
    }

    @Override
    public String toString(){

        StringBuilder stringBuilder = new StringBuilder();

        int numEntries = getNumbers().size();

        Integer number = getTopmostNumber();

        stringBuilder.append(number + " -> ");

        for(int i = 0; i < numEntries; i ++){
            number = getClockwiseAdjacentNumber(number);
            stringBuilder.append(number + " -> ");
        }

        return stringBuilder.toString();
    }

}
