package Merging_10.Basic_Merging_1;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

public class MergingFlatMapOneToMany_5 {

    public static void main(String[] args) {

        /*
            Created a bunch of lists. Focused on variance in values and the number of elements.
         */
        List<Integer> odds = Arrays.asList(1, 3, 5, 7, 9, 11, 13, 15, 17, 19);
        List<Integer> twos = Arrays.asList(2, 4, 6, 8, 10, 12, 14, 16, 18);
        List<Integer> threes = Arrays.asList(3, 6, 9, 12, 15, 18);
        List<Integer> fours = Arrays.asList(4, 8, 12, 16);
        List<Integer> fives = Arrays.asList(5, 10, 15);
        List<Integer> sixes = Arrays.asList(6, 12, 18);
        List<Integer> sevens = Arrays.asList(7, 14);


        /*
            Create an observable of the Lists
         */
        Observable<List<Integer>> numbers = Observable.just(odds, twos, threes, fours, fives, sixes, sevens);

        /*
            This demonstrates having ONE Observable containing several Lists of Integers.

            1.) flatMap takes the seven events from the "source" Observable and maps each one to its Observable own
            observable
                - (It's easier to think of it in terms of dimension sometimes. An Observable of Events, becomes an
                Observable of an Observable of Events).
            2.) we use the Observable.fromIterable() factory to extract the elements from each List<> to
                return each List<T> as an Observable of integers.
                - I provided the BREAK text to demonstrate the boundary of each of the Observables we generated
            3.) flatMap() merges ALL of the Observables it has created into a single reactive stream to be
            consumed (Observed) by subscribe()

         */

        numbers
                .flatMap(integers -> {
                    // Inserted just so you can see the "line" between each
                    System.out.println("BREAK");
                    return Observable.fromIterable(integers);
                })
                .subscribe(
                        s -> System.out.println("OBSERVED: " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("FINITO MR. ROBOTO")
                );


    }
}
