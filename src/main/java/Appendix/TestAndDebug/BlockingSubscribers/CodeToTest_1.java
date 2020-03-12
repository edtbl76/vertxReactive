package Appendix.TestAndDebug.BlockingSubscribers;

import Utils.Generic;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class CodeToTest_1 {
    public static void main(String[] args) {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(5)
                .subscribe(System.out::println);

        Generic.wait(6);
    }
}
