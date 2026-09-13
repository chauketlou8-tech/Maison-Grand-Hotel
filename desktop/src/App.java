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
    private DashBoardView dashBoardView;
    private Stage stage;
    private User currentUser;

    public App() throws SQLException, IOException, ClassNotFoundException {
        ServerRepository.connectDB();
        List<User> users = ServerRepository.getUsers();
        this.loginPage = new LoginPage(users, this::setIsLoggedIn, this::updateScene, this::setCurrentUser);
    }

    @Override
    public void start(Stage stage) throws Exception {
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
        loggedIn = isLoggedIn;
    }

    private void updateScene() {
        Scene appScene = new Scene(loggedIn ? this.dashBoardView : this.loginPage);
        this.stage.setScene(appScene);
        this.stage.setFullScreen(true);
        appScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/login.css")).toExternalForm());
        appScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dashboard.css")).toExternalForm());
    }

    private void setCurrentUser(User u) {
        try {
            this.currentUser = u;

            if (u != null) {
                this.dashBoardView = new DashBoardView(u, this::setIsLoggedIn, this::updateScene, this::setCurrentUser);
            }

            else {
                this.dashBoardView = null;
            }
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
