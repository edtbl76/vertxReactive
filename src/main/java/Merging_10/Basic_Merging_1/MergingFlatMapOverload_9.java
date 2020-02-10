package Merging_10.Basic_Merging_1;

import io.reactivex.Observable;

public class MergingFlatMapOverload_9 {

    public static void main(String[] args) {

        /*
            This demonstrates the BiFunction<T, U, R> overload for flatMap.

            In this particular example:
                T is the original event (i.e. the String)
                then we have U and R, which you can think of as "original" and "mapped"
                where the "original" value is the "input" event that has become the observable, and "mapped" are the
                elements that are now events.

            (The point of this particular example is demonstrating that new events CAN BE RELATED TO THEIR SOURCE).

         */
       Observable<String> myLetters = Observable.just("these", "are", "my", "letters");

       myLetters
               .flatMap(s ->
                       Observable.fromArray(s.split("")), (original, mapped) -> original + "[" + mapped + "]")
               .subscribe(System.out::println);

    }
}
