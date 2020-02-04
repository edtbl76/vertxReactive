package Operators_9.ActionOperators_6;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class ActionDoOnDispose_5 {

    public static void main(String[] args) {

        /*

         */
        Disposable d = Observable.just("I'm", "running", "out", "of", "ideas", "for", "string", "themes")

                .doOnSubscribe(
                        disposable -> System.out.println("I'm a Consumer<Disposable>, and I'm subscribing!"))
                .doOnDispose(() -> System.out.println("I'm fired off when dispose() is called!"))
                .subscribe(
                );




    }
}
