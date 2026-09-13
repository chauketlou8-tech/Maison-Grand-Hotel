package views.dashboard;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import models.User;

import java.io.IOException;
import java.util.function.Consumer;

public class DashBoardView extends VBox {

    public DashBoardView(User currentUser, Consumer<Boolean> setIsLoggedIn, Runnable updateScene, Consumer<User> setCurrentUser) throws IOException {

        TopBar topBar = new TopBar(currentUser);
        Content content = new Content(currentUser, setIsLoggedIn, updateScene, setCurrentUser);
        Footer footer = new Footer(currentUser);

        VBox layout = new VBox();
        layout.getChildren().addAll(topBar, content, footer);
        layout.setFillWidth(true);
        layout.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        layout.setStyle("-fx-background-color: #0f0e0c");
        VBox.setVgrow(layout, Priority.ALWAYS);
        VBox.setVgrow(content, Priority.ALWAYS);

        this.getChildren().add(layout);
        this.setFillWidth(true);
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }
}