package Operators_9.Transformers_2;

import io.reactivex.Observable;
import org.apache.commons.lang3.StringUtils;

public class XFormerMap_1 {
    public static void main(String[] args) {

        /*
            Demonstration of map.
            Yes, I used a third party library to capitalize...Sue me.
         */
        Observable.just("john", "jacob", "jingle", "heimer", "smith")
                .map(StringUtils::capitalize)
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("Properly capitalized")
                );
    }
}
