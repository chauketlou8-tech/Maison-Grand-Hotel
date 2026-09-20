import javafx.stage.Stage;

import models.Settings;
import models.User;

import ui.views.login.LoginPage;
import ui.views.dashboard.DashBoardView;

import repositories.ServerRepository;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class App extends Application {
    private final LoginPage loginPage;

    private boolean loggedIn = false;
    private DashBoardView dashBoardView;

    private Stage stage;
    private Scene appScene;

    private final Settings settings;

    public App() throws SQLException, IOException {
        ServerRepository.connectDB();

        List<User> users = ServerRepository.getUsers();

        this.settings = ServerRepository.getSettings();
        this.loginPage = new LoginPage(users, this::setIsLoggedIn, this::updateScene, this::setCurrentUser);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.updateScene();

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setIsLoggedIn(boolean isLoggedIn) {
        this.loggedIn = isLoggedIn;
    }

    private void updateScene() {
        Parent root = loggedIn ? this.dashBoardView : this.loginPage;

        if (this.appScene == null) {
            this.appScene = new Scene(root);

            this.appScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/login.css")).toExternalForm());
            this.appScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dashboard.css")).toExternalForm());

            this.stage.setScene(this.appScene);
        }

        else {
            this.appScene.setRoot(root);
        }

        this.stage.setFullScreen(true);
    }

    private void setCurrentUser(User u) {
        try {

            if (u != null) {
                this.dashBoardView = new DashBoardView(u, this::setIsLoggedIn, this::updateScene, this::setCurrentUser, this.settings);
            }

            else {
                this.dashBoardView = null;
            }
        }

        catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}