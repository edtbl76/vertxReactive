package ConcurrencyFlowControl_13.CoolTricks_99;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class ImpatientButton extends Application {


    @Override
    public void start(Stage stage) throws Exception {

        /*
            Build something we can see.
         */
        VBox root = new VBox();
        root.setMinSize(200, 100);

        Label label = new Label("");
        root.getChildren().addAll(label);

        Scene scene = new Scene(root);

        /*
            create a multicast emitter that collects keystrokes

            (remember share = refcount + connect)
         */
        Observable<String> observable =
                JavaFxObservable.eventsOf(scene, KeyEvent.KEY_TYPED)
                .map(KeyEvent::getCharacter)
                .share();

        /*
            Create a contrived acquiescence so we can demonstrate the challenge of
            "the button press". (I mentioned somewhere that this is a good use case for throttleWithTimeout)o

            This is a fairly long timeout. play with this.

            We create a display that starts with nothing, and resets at 600 ms of inactivity.

            (Use cases can be search requests, auto-complete etc.)
         */
        Observable<String> acquiescence = observable
                .throttleWithTimeout(600, TimeUnit.MILLISECONDS)
                .startWith("");

        /*
            This is the meat of our keystroke thingey.

            We have a switchMap that takes the typed input and passes it to scan() (remember? That's reduce,
                that creates an event for each result, rather than just the finished result.
                - this will demonstrate "typing" in our console)
                - we use a UI Thread and then we have a unique implementation of subscribe, because we are
                doing more than just printing something out!)
         */
        acquiescence
                .switchMap(s -> observable.scan("", (fullString, nextBit) -> fullString + nextBit))
                .observeOn(JavaFxScheduler.platform())
                .subscribe(s -> {
                    label.setText(s);
                    System.out.println("[" + Thread.currentThread().getName() + "] - " + s);
                });

        stage.setScene(scene);
        stage.show();
    }

}
