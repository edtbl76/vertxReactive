package CustomOperators_15.Converters_2;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observers.JavaFxObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.application.Application;
import javafx.beans.binding.Binding;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class BeforeToOperator_2 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        Label label = new Label("");

        Observable<String> observable =
                Observable.interval(400, TimeUnit.MILLISECONDS)
                    .map(Object::toString)
                    .observeOn(JavaFxScheduler.platform());

        Binding<String> binding = JavaFxObserver.toBinding(observable);

        label.textProperty().bind(binding);

        root.setMinSize(200,100);
        root.getChildren().addAll(label);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
