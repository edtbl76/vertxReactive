package Merging_10.Basic_Merging_1;

import io.reactivex.Observable;
import org.apache.commons.lang3.StringUtils;


public class MergingFlatMap_6 {

    public static void main(String[] args) {

        /*
            This is an Observable that has different phone number values that we need converted into numbers for
            an annoying auto-dialer that blasts cell phone numbers about their unsecured credit card debt, the
            latest political polls or some form of social security scam.

            We'll assume that the scraper we're using to provide a list of cell phones has already provided us with
            the separator (a dash) and the area code (800 for our purposes).

            The output
         */
        Observable<String> phoneNumbers = Observable.just(
                "800-795-2822",
                "800-rxj-ava2",
                "800-RXJ-AVA2"
        );

        /*
            1.) Flat Map takes each "String" event and converts it into an observable.
            - first we create a marker that is telling the consumer we are STARTING TO SEND A SET OF "STUFF" that is
            a phone number
            - second we take each String and break it into 3 Observables by splitting it on the phone number separator
            character

            Once we have done this, we have expanded from a Single Observable with 3 string elements to
            3 observables, each with 3 elements.

            The last step is to merge these 3 observables into a single stream of 9 events.

            2.) map() does the heavy lifting by fast tracking strings consisting only of numeric characters, or
            handing it off to our "reactive" keypadConverter to be turned into a string of digits.

            3.) The consumption step just prints out the inputs.
         */
        phoneNumbers
                .flatMap(s -> {
                    System.out.println("Start: ");
                    return Observable.fromArray(s.split("-"));
                })
                .map(s -> Integer.valueOf(
                        (s.matches("[0-9]+") ? s : keypadConverter(s))
                ))
                .subscribe(System.out::println);
    }


    /*
        Pretty arcane way of creating a keypad converter.

        We could have created a Map, but populating maps is fairly annoying and the syntax is counterintuitive to
        reactive programming.

        We could also have chained together a bunch of StringUtils.replaceChars() calls, which IS fairly
        reactive, but the syntax of doing so is clunky and readability is atrocious (because StringUtils doesn't
        support the Chain of Responsibility pattern directly)

        So... I felt like this was a happy medium.

        - replaceEach() allows me to return my result in one nicely en"closed" callback-like statement.
        - at the same time, the two String[] args I provided are far more readable than conventional means of populating
        a map

        I'm explaining (and justifying) these decisions deliberately, because reactive programming isn't just about
        using the libraries and frameworks enable "reactive code", it is about thinking reactively about
        the non-Rx code you will use in reactive programs and systems.

        This helps the code flow, supports deeper understanding of the design, and becomes a vessel for communicating
        the final result.
     */
    private static String keypadConverter(String number) {
        return StringUtils.replaceEach(
                number.toLowerCase(),
                new String[]{
                        "a", "b", "c",
                        "d", "e", "f",
                        "g", "h", "i",
                        "j", "k", "l",
                        "m", "n", "o",
                        "p", "q", "r", "s",
                        "t", "u", "v",
                        "w", "x", "y", "z"
                },
                new String[] {
                        "2", "2", "2",
                        "3", "3", "3",
                        "4", "4", "4",
                        "5", "5", "5",
                        "6", "6", "6",
                        "7", "7", "7", "7",
                        "8", "8", "8",
                        "9", "9", "9", "9"
                }
        );
    }
}
