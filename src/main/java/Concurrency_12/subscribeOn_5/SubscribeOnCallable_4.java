package Concurrency_12.subscribeOn_5;


import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SubscribeOnCallable_4 {

    public static void main(String[] args) {

        try {
            /*
                This is a real URL used for examples just like this.
             */
            URL giphy = new URL("https://jsonplaceholder.typicode.com/todos/1");

            /*
                Try-with-resources that is kind of a monstrosity, but this sets up a BufferedReader.
             */
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(giphy.openStream()))) {

                /*
                    I kick off my Callable() by collecting the "lines" from my reader to a list.
                    This becomes the factory for my Observable, such that each line from the reader is going to
                    become an event.

                    - we hand this off to io, because in theory, multiple calls could block.
                    - we flat map this down.
                        - I could have just done something like s -> s, but I wanted to use ArrayList::new to
                        demonstrate that we're creating a list that is going to get flattened into the contents (
                        which are strings).

                   - We DUPLICATED this pattern with different logic. We probably could have avoid this if we wanted to.
                        - the second time around, we are splitting all of the strings that have a colon in them and
                        dumping them to lists... then we are flatMapping those back down to a chain of String events.

                    - Our last operators strip off the whitespace and filter out anything that isn't quoted (i.e.
                    probably not a string).

                    - our blockingSubscribe() keeps the main thread alive so that we can consume the emissions
                    w/ our print statement in onNext().

                    The cool result here is that since we are using Schedulers.io(), the main thread isn't blocking.
                    - Schedulers.io is assigning the blockingSubscribe() (the observer we want) to a new thread from
                    the pool managed by Schedulers.io).

                    This means that the main thread could be doing other things.
                    (This is very important in event loop architectures, where blocking the event loop is going to
                    result in a pretty bad day for everyone in involved.)
                 */
                Observable.fromCallable(() -> bufferedReader.lines().collect(Collectors.toList()))
                        .subscribeOn(Schedulers.io())
                        .flatMapIterable(ArrayList::new)
                        .map(s -> Pattern.compile(":").splitAsStream(s).collect(Collectors.toList()))
                        .flatMapIterable(ArrayList::new)
                        .map(String::strip)
                        .filter(s -> s.startsWith("\""))
                        .blockingSubscribe(System.out::println);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
