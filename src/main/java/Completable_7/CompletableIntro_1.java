package Completable_7;

import io.reactivex.Completable;

public class CompletableIntro_1 {

    public static void main(String[] args) {

        // This is the simplest version. It just calls "complete"
        Completable.complete().subscribe(
                () -> System.out.println("I'm done!")
        );
    }
}
