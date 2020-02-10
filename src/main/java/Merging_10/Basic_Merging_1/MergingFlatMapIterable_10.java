package Merging_10.Basic_Merging_1;


import io.reactivex.Observable;
import org.apache.commons.text.WordUtils;

import java.util.List;

public class MergingFlatMapIterable_10 {

    public static void main(String[] args) {

        // Observable of 4 Lists.
        Observable<List<String>> observable = Observable.just(
                List.of("dallas", "new york", "philadelphia", "washington"),
                List.of("chicago", "detroit", "green bay", "minnesota"),
                List.of("atlanta", "carolina", "new orleans", "tampa bay"),
                List.of("arizona", "los angeles", "san francisco", "seattle")
        );

        /*
            FlatMap is going to take each of the list of strings and map them to an observable.

            I deliberately printed out the strings in their input form before I process them via mpa() (separately).
            In this particular case, it doesn't make much of a difference.

            THis particular example is data-driven, as we would need a finite (and hopefully small) data set
            before we decide to sort them.

         */
        observable
                .flatMapIterable(strings -> {
                    System.out.print("This is a List<String> that will become an Iterable: ");
                    System.out.println(strings);

                    /*
                        At the point we return strings, each Iterable is going to be FLATTENED down into its elements,
                        and each flattened Iterable is merged together to create one reactive stream of
                        Observable<String>
                     */

                    return strings;
                })
                /*
                    Note WordUtils in commons-lang3 was deprecated, so we have to pull it from commons-text.
                    - This is a shortcut for performing space-delimited capitalization.
                 */
                .map(WordUtils::capitalizeFully)
                .sorted()
                .subscribe(System.out::println);

    }

}
