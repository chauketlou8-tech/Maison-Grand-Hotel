package ui.views.dashboard;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import models.Notification;
import models.Settings;
import models.User;
import ui.views.layout.Content;
import ui.views.layout.Footer;
import ui.views.layout.TopBar;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Consumer;

public class DashBoardView extends VBox {

    private final TopBar topBar;

    public DashBoardView(User currentUser, Consumer<Boolean> setIsLoggedIn, Runnable updateScene, Consumer<User> setCurrentUser, Settings settings) throws IOException, SQLException {

        this.topBar = new TopBar(currentUser, settings);
        Content content = new Content(currentUser, setIsLoggedIn, updateScene, setCurrentUser, settings, this::setNotifications);
        Footer footer = new Footer(currentUser, settings);

        this.topBar.setOnBellClick(() -> content.updatePage("Notifications"));

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

    public void setNotifications(Notification[] notifications) {
        if (this.topBar != null) {
            this.topBar.setData(notifications);
        }
    }
}