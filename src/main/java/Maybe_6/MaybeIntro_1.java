package Maybe_6;

import io.reactivex.Maybe;

public class MaybeIntro_1 {

    public static void main(String[] args) {

        // Maybe w/ an event.
        Maybe<Integer> integerMaybe1 = Maybe.just(7);

        integerMaybe1.subscribe(
                integer -> System.out.println("SUB1 - RCVD - " + integer),
                Throwable::printStackTrace,
                () -> System.out.println("SUB1 - DONE")
        );

        // Maybe w/o an event
        Maybe<Integer> integerMaybe2 = Maybe.empty();

        integerMaybe2.subscribe(
                integer -> System.out.println("SUB2 - RCVD - " + integer),
                Throwable::printStackTrace,
                () -> System.out.println("SUB2 - DONE")
        );
    }
}
