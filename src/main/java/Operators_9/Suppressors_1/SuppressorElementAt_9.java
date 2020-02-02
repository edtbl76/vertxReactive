package Operators_9.Suppressors_1;

import io.reactivex.Observable;

public class SuppressorElementAt_9 {

    public static void main(String[] args) {

        Observable<String> jvmLanguages = Observable.just("Java", "Clojure", "Scala", "PHP", "Kotlin");

        /*
            Example one is a basic example of elementAt (returns a Maybe!)
            onComplete() isn't called in this case.
         */
        System.out.println("Example 1: (ElementAt)");
        jvmLanguages.elementAt(3)
                .subscribe(
                        s -> System.out.println(s + " is not a JVM based language!"),
                        Throwable::printStackTrace,
                        () -> System.out.println("I'm done with you!")
                );

        /*
            Example of elementAt where the Index doesn't exist
            onComplete() is called

            (Remember, Maybe has a conditional onSuccess or onComplete)
         */
        System.out.println("\nExample 2: (ElementAt - Index not exist)");
        jvmLanguages.elementAt(100)
                .subscribe(
                        s -> System.out.println("There is no index 100"),
                        Throwable::printStackTrace,
                        () -> System.out.println("Nothing happens!")
                );


        System.out.println("\nExample 3: ElementAtOrError");
        jvmLanguages.elementAtOrError(10)
                .subscribe(
                        s -> System.out.println("There is no index"),
                        throwable -> {
                            for (Object o : throwable.getStackTrace()) {
                                System.out.println(o);
                            }
                        }
                );

        // Nothing special -> gets first element
        System.out.println("\nExample 4: First");
        jvmLanguages.firstElement().subscribe(System.out::println, Throwable::printStackTrace);

        // Nothing special -> gets last element
        System.out.println("\nExample 5: Last");
        jvmLanguages.lastElement().subscribe(System.out::println, Throwable::printStackTrace);

        /*
            This takes an Observable with a SINGLE element and turns it into a Single.
         */
        System.out.println("\nExample 6: Observable w/ one Event -> Single");
        Observable.just("ONE").singleElement().subscribe(System.out::println, Throwable::printStackTrace);

        /*
            This is what happens when you use singleElement() with more than one element.
         */
        System.out.println("\nExample 7: Observable w/ multiple Events -> Single");
        Observable.just("more", "than", "one")
                .singleElement()
                .subscribe(
                        System.out::println,
                        throwable -> {
                            for (Object o : throwable.getStackTrace())
                                System.out.println(o);
                        }
                );


    }
}
