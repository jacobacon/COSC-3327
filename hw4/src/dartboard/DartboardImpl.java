package dartboard;


import combinatorics.Permutation;


import java.util.Set;

public class DartboardImpl implements Dartboard {

    int topNumber;
    Permutation<Integer> permutation;

    public DartboardImpl(Permutation<Integer> p, int topmostNumber){
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
        return permutation.getImage(number);
    }

    @Override
    public String toString(){

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(topNumber);




        return stringBuilder.toString();
    }

}
