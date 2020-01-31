package Completable_7;

import io.reactivex.Completable;

public class CompletableExample_2 {

    public static void main(String[] args) {

        /*
            This is a slightly more involved example of a Completable.
            Here - we pass a callback to fromRunnable() which executes before the completable is called.
         */
        Completable.fromRunnable(() -> runMe()).subscribe(() -> System.out.println("Complete!"));
    }

    private static void runMe() {
        System.out.println("I have been run!");
    }
}
