package ConcurrencyFlowControl_13.CoolTricks_99;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class SwitchMapStopWatch extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        Label label = new Label("");
        ToggleButton toggleButton = new ToggleButton();

        // Hot/Multicast button's state.
        Observable<Boolean> observable
                = JavaFxObservable.valuesOf(toggleButton.selectedProperty())
                .publish()
                .autoConnect(2);

        /*
            switchMap is being used to determine what to do with the state.
            If the button is "on" (Started) then we kick off an infinite counter, otherwise we
            dispose of it by calling it empty.

            JavaFxScheduler.platform() is a UI Thread (remember those!?!??!)
            We're mapping our result to a string and setting the label (our "counter") to update the text.


         */
        observable
                .switchMap(
                        buttonState -> {
                            if (buttonState)
                                return Observable.interval(1, TimeUnit.MILLISECONDS);
                            else
                                return Observable.empty();
                        })
                .observeOn(JavaFxScheduler.platform())
                .map(Object::toString)
                .subscribe(label::setText);

        /*
            This subscribes to the observable to use the existing state to change the button label.
            If we are ON, then we are started, so the button should read stop.
            If we are STOPPED, then the button should read start.
         */
        observable.subscribe(buttonState -> toggleButton.setText(buttonState ? "STOP" : "START"));

        // JavaFX wiring stuff that I had to look up :)
        root.getChildren().addAll(label, toggleButton);
        stage.setScene(new Scene(root));
        stage.show();

    }
}
