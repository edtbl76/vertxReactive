package Operators_9.ActionOperators_6;


import io.reactivex.Observable;

public class ActionDoOnSuccess_8 {

    public static void main(String[] args) {

        /*
            In the previous example, I deliberately introduced onSuccess into doFinally as a segue for this lesson.

            What if we are using a reduce() operator that converts an Observable into a Maybe or Single?
            - these don't have onNext or onComplete, they ONLY have onSuccess.

            This little ditty takes an Observable of strings. For whatever reason we want to reduce() this to the
            total characters.
         */
        Observable.just("all", "the", "single", "ladies")
                .map(String::length)
                .reduce((sum, number) -> sum + number)
                .doOnSuccess(integer -> System.out.println("PUSHING: " + integer))
                .subscribe(
                        integer -> System.out.println("RCVD: " + integer),
                        Throwable::printStackTrace
                );

    }
}
