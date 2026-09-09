import javafx.stage.Stage;
import models.User;

import views.login.LoginPage;
import views.dashboard.DashBoardView;

import repositories.ServerRepository;

import javafx.application.Application;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class App extends Application {
    private final LoginPage loginPage;
    private boolean loggedIn = false;
    private final DashBoardView dashBoardView;
    private Stage stage;

    public App() throws SQLException, IOException {
        ServerRepository.connectDB();
        List<User> users = ServerRepository.getUsers();
        this.loginPage = new LoginPage(users, this::setIsLoggedIn, this::updateScene);
        this.dashBoardView = new DashBoardView();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.updateScene();

        stage.setFullScreen(true);
        stage.setWidth(800);
        stage.setHeight(600);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setIsLoggedIn(boolean isLoggedIn) {
        loggedIn = true;
    }

    private void updateScene() {
        Scene appScene = new Scene(loggedIn ? this.dashBoardView : this.loginPage);
        this.stage.setScene(appScene);
        appScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
    }
}
