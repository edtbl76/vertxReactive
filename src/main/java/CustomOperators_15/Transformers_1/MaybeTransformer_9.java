package CustomOperators_15.Transformers_1;

import com.google.common.collect.Lists;
import com.google.common.primitives.Chars;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import javafx.util.Pair;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MaybeTransformer_9 {

    public static void main(String[] args) {

        /*
            Create a new MaybeObserver so you can see what is going on inside it. (We don't use these much...)
         */
        MaybeObserver<List<Character>> maybeObserver = new MaybeObserver<>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onSuccess(List<Character> characters) {
                System.out.println(characters);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done");
            }
        };


        List<Pair<MaybeTransformer<String, List<Character>>, String>> transformers =
                Arrays.asList(
                        new Pair <>(stringToCharArrayNaiveCharArray(),"Naive toCharArray()") ,
                        new Pair <>(stringToCharArrayNaiveCharAt(), "Naive CharAt()"),
                        new Pair <>(stringToCharArrayStreamsRange(), "Streams range()"),
                        new Pair <>(stringToCharArrayStreamsChars(), "Streams chars()"),
                        new Pair <>(stringToCharArrayAbstractListUtil(), "AbstractList()"),
                        new Pair <>(stringToCharArrayGuavaChars(), "Google Guava Chars.AsList()"),
                        new Pair <>(stringToCharArrayGuavaLists(), "Google Guava Lists.charactersOf()")
                );



        transformers.forEach(
                stringListMaybeTransformer -> {

                    System.out.println("Method Example: " + stringListMaybeTransformer.getValue());
                    Maybe.<String>empty()
                            .compose(stringListMaybeTransformer.getKey())
                            .subscribe(maybeObserver);

                    Maybe.just("Maybe")
                            .compose(stringListMaybeTransformer.getKey())
                            .subscribe(maybeObserver);

                    System.out.println("\n");
                }
        );


    }

    /*
        There are multiple implementations of these methods due to an external request. Someone asked me about
        different ways to move from string to List<Character>, so i decided to make that the use case for my
        MaybeTransformer.

        This is a naive method that builds an internal list, iterates through the results of toCharArray() and
        uses them to populate the list.
     */
    static MaybeTransformer<String, List<Character>> stringToCharArrayNaiveCharArray() {
        return maybeObserver -> maybeObserver.map(s -> {
            List<Character> list = new ArrayList<>();
            for (char ch : s.toCharArray())
                list.add(ch);
            return list;
        });
    }

    /*
        Another naive implementation using a traditional for loop. This demonstrates the use of charAt()
     */
    static MaybeTransformer<String, List<Character>> stringToCharArrayNaiveCharAt() {
        return maybeObserver -> maybeObserver.map(s -> {
            List<Character> list = new ArrayList<>();
            for (int i = 0; i < s.length(); i++)
                list.add(s.charAt(i));

            return list;
        });
    }


    /*
        Java Streams makes this a little bit more civilized.
        - We create an IntStream that generates the same number of integers as there are chars in the
        provided string
        - Then we iterate through it in the same fashion above via a lambda.
     */
    static MaybeTransformer<String, List<Character>> stringToCharArrayStreamsRange() {
        return maybeObserver -> maybeObserver.map(
                s -> {
                    List<Character> list = new ArrayList<>();
                    IntStream.range(0, s.length())
                            .forEach(i -> list.add(s.charAt(i)));
                    return list;
                }
        );
    }

    /*
        We can condense this further by leveraging the helper functions.
        - chars() breaks a stream down into an IntStream, but it isn't quite the same as before.
        These are the integer values that correspond to the CHARACTER CODES.
        - mapToObj() converts each of the character codes into the actual chars
            (This is important... map() is going to print the char codes)
        - our last step is to collect the results into a List for consumption.

     */
    static MaybeTransformer<String, List<Character>> stringToCharArrayStreamsChars() {
        return maybeObserver -> maybeObserver.map(s ->
                s.chars()
                        .mapToObj(obj -> (char)obj)
                        .collect(Collectors.toList())
        );
    }



    /*
        I know this is an older method, but I've always liked it. (I'm old).
        I believe the language used in the javadocs is "skeletal implementation".

        Basically this is a lightweight way to gain access to the List<> interface.
        The end result has to be immutable. (or "unmodifiable").

     */
    static MaybeTransformer<String, List<Character>> stringToCharArrayAbstractListUtil() {
        return maybeObserver -> maybeObserver
                .map(s -> {
                    return new AbstractList<Character>() {
                        @Override
                        public Character get(int i) {
                            return s.charAt(i);
                        }

                        @Override
                        public int size() {
                            return s.length();
                        }
                    };
                });
    }

    /*
        Save the best for last. Google Guava for the win.
        This is just that simple.

        This is closer in impl to the very first naive example (yes... I placed them in that order on purpose :).
        I love circular journeys.)

        We are using s.toCharArray() to create the Character[] structure, but Chars.asList(), and the nice
        folks at Google, is/are doing all of the leg
        work to convert into List<Character> for us.
     */
    static MaybeTransformer<String, List<Character>> stringToCharArrayGuavaChars() {
        return maybeObserver -> maybeObserver
                .map(s -> Chars.asList(s.toCharArray()));
    }


    /*
        This is another way to do it with Google Guava, but its still marked as a @Beta feature.
     */
    static MaybeTransformer<String, List<Character>> stringToCharArrayGuavaLists() {
        return maybeObserver -> maybeObserver
                .map(Lists::charactersOf);
    }

}
