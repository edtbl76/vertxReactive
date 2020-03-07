package Operators_9.Transforming_2;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

public class IfEmpty_3 {

    public static void main(String[] args) {

        // Nothing special ->
        Observable<String> fruit = Observable.just("Banana", "Apple", "Pear", "Peach", "Apricot", "Plum");
        Observable<String> vegetables = Observable.just("Asparagus", "Brussel Sprouts", "Carrots", "Onions");

        // Filter a predicate and generate
        System.out.println("Example 1: Demonstrate Observable That isn't Empty.");
        fruit
                .filter(getPredicate("P"))
                /*
                    Sometimes I'm going to do this... It is a shortcut when I don't really want to type out the
                    onComplete callback.
                 */
                .subscribe(System.out::println, Throwable::printStackTrace);

        // defaultIfEmpty provides a string or some other value to emit if the chain AT THAT POINT is empty.
        System.out.println("\nExample 2: Demonstrate a case of defaultIfEmpty");
        fruit
                .filter(getPredicate("Z"))
                .defaultIfEmpty("No Fruit For You!")
                .subscribe(System.out::println, Throwable::printStackTrace);

        // This illustrates the capitalized letters in the previous example...
        System.out.println("\nExample 3: Demonstrate a case where ORDER MATTERS!");
        fruit
                .defaultIfEmpty("This isn't going to be emitted")
                .filter(getPredicate("Z"))
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("Done...got nuthin'")
                );

        /*
            switchIfEmpty() is similar to default If Empty, but rather than providing a
            simple default "value", we are providing it with an alternate obserable altogether.
         */

        System.out.println("\nExample 4: Demonstrate switchIfEmpty");
        fruit
                .filter(getPredicate("L"))
                .switchIfEmpty(vegetables)
                .subscribe(System.out::println, Throwable::printStackTrace);

    }


   private static Predicate<String> getPredicate(String letter) {
        return s -> s.startsWith(letter.toUpperCase());
   }


}
