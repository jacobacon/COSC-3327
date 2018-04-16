package combinatorics;


import java.util.*;

public class PermutationImpl<E> implements Permutation<E> {

    private Set<E> domain;
    private Set<List<E>> cycles;

    private Map<E, E> values = new HashMap<>();


    public PermutationImpl(Set<List<E>> cycles){
        //TODO
        this.cycles = cycles;

        for (List<E> cycle: cycles) {
            for (int i = 1; i < cycle.size(); i++) {

                //TODO Use a better way than this.
                domain.add(cycle.get(i-1));
                domain.add(cycle.get(i));

                values.put(cycle.get(i-1), cycle.get(i));

                //System.out.println(cycle.get(i-1));
                //System.out.println(cycle.get(i));
            }

            values.put(cycle.get(cycle.size() - 1), cycle.get(0));

        }
    }

    public PermutationImpl(Set<List<E>> cycles, Set<E> domain){
        this.cycles = cycles;
        this.domain = domain;
        //TODO add cycles from domain

        for (List<E> cycle: cycles) {
            for (int i = 1; i < cycle.size(); i++) {

                values.put(cycle.get(i-1), cycle.get(i));

                //System.out.println(cycle.get(i-1));
                //System.out.println(cycle.get(i));
            }

            values.put(cycle.get(cycle.size() - 1), cycle.get(0));

        }

        //System.out.println("The Map Contains: " + values.toString());
    }

    @Override
    public E getPreImage(E element) {
        //TODO double check
        Map<E, E> reverseMap = new HashMap<>();

        for (Map.Entry<E, E> value: values.entrySet()) {
            reverseMap.put(value.getValue(), value.getKey());
        }

        //System.out.println(values);
        //System.out.println(reverseMap);

        E value = reverseMap.get(element);
        
        
        return value;
    }

    @Override
    public E getImage(E element) {
        return values.get(element);
    }

    @Override
    public Set<E> getDomain() {
        return domain;
    }

}
