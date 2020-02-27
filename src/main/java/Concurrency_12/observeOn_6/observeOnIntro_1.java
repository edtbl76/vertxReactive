package Concurrency_12.observeOn_6;

import Utils.Generic;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class observeOnIntro_1 {

    /*
        Here's our handy Observable that demonstrates observeOn() fairly well.

        1.) Let's say our just() factory is actually a blocking task, reading JSON from a REST call. It makes sense to
        use Schedulers.io() here.

        2.) our flatMap represents a fairly arcane JSON parser to extract just the values.
        - this is probably a very "hand wavy" version of processing. I apologize. I don't want to obfuscate the real
        purpose of the lesson.

        3.) At this point, our workflow is no longer IO-based, so our time spent idling is going to decrease. It makes
        sense to change things up here, because remaining on a dynamic thread model is more dangerous under the circumstances
        - let's change over to the computation() scheduler.

        4.) We do our processing, attach the Observer via subscribe, and we wrap it up.

     */
    public static void main(String[] args) {
        Observable.just(
                "Key1:Value1",
                "Key2:Value2",
                "Key3:Value3"
        ).subscribeOn(Schedulers.io())
                .flatMap(kv -> Observable.fromArray(kv.split(":")))
                .observeOn(Schedulers.computation())
                .filter(s -> !s.startsWith("Key"))
        .subscribe(
                s -> System.out.println("[" + Thread.currentThread().getName() + "] - " + s)
        );

        Generic.wait(10);

        ;
    }
}
