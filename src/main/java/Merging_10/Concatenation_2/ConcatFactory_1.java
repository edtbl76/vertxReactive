package Merging_10.Concatenation_2;

import io.reactivex.Observable;

public class ConcatFactory_1 {

    public static void main(String[] args) {

        // Nothing Special
        Observable<String> oscar = Observable.just("my", "baloney", "has", "a", "first", "name");
        Observable<String> meyer = Observable.just("my", "baloney", "has", "a", "second", "name");


        /*
            Using the Concat Factory which takes multiple observables as args.

            NOTE: if you try to do this w/ merge, you'll more than likely get the same result because the Observables
            are small and they are being consumed on the same system.

            The important point to note is that
            - with merge()... ORDERING IS NOT GUARANTEED
            - with concat()... IT IS.

         */
        Observable.concat(oscar, meyer)
                .subscribe(
                        s -> System.out.print(s + " "),
                        Throwable::printStackTrace,
                        () -> System.out.println("-> Finished concatenating"));

        Observable.concat(meyer, oscar)
                .subscribe(
                        s -> System.out.print(s + " "),
                        Throwable::printStackTrace,
                        () -> System.out.println("-> Finished concatenating"));


    }
}
