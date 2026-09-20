package ui.views.layout;

import utils.NavItem;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.User;

import java.util.function.Consumer;

public class SideBar extends VBox {
    private final Consumer<Boolean> setIsLoggedIn;
    private final Runnable updateScene;
    private final Consumer<User> setCurrentUser;
    private final Consumer<String> updatePage;

    public SideBar(User user, Consumer<Boolean> setIsLoggedIn, Runnable updateScene, Consumer<User> setCurrentUser, Consumer<String> updatePage) {
        this.setIsLoggedIn = setIsLoggedIn;
        this.updateScene = updateScene;
        this.setCurrentUser = setCurrentUser;
        this.updatePage = updatePage;

        getStyleClass().add("sidebar");
        setPrefWidth(20);
        setMinWidth(200);
        setMaxWidth(200);
        setMaxHeight(Double.MAX_VALUE);

        VBox nav = new VBox();
        nav.getStyleClass().add("sidebar-nav");
        nav.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(nav, Priority.ALWAYS);

        NavItem[] front_desk = {
                new NavItem(FontAwesomeIcons.TH_LARGE, "Dashboard", true),
                new NavItem(FontAwesomeIcons.USERS, "Guest Registry", false),
                new NavItem(FontAwesomeIcons.CALENDAR, "Bookings", false),
                new NavItem(FontAwesomeIcons.CALENDAR_ALT, "Reservations", false),
        };

        NavItem[] operations = {
                new NavItem(FontAwesomeIcons.BED, "Room Management", false),
                new NavItem(FontAwesomeIcons.LIST_UL, "Housekeeping", false),
                new NavItem(FontAwesomeIcons.BELL_O, "Services", false),
        };

        NavItem[] administration = {
                new NavItem(FontAwesomeIcons.USER_PLUS, "Staff Directory", false),
                new NavItem(FontAwesomeIcons.CREDIT_CARD, "Payments", false),
                new NavItem(FontAwesomeIcons.COG, "System Settings", false),
        };

        Label front_desk_label = new Label("FRONT DESK");
        front_desk_label.getStyleClass().add("sidebar-section-label");
        front_desk_label.setMaxWidth(Double.MAX_VALUE);
        nav.getChildren().add(front_desk_label);

        for (NavItem item : front_desk) {
            HBox hBox = new HBox();
            hBox.getStyleClass().add("sidebar-item");
            hBox.setSpacing(12);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setMaxWidth(Double.MAX_VALUE);

            FontAwesomeIcon icon = item.getIcon();
            icon.setSize("14");
            icon.setFill(Color.web("#6a6058"));

            Label label = new Label(item.getText());
            label.getStyleClass().add("sidebar-item-label");

            hBox.getChildren().addAll(icon, label);

            if (item.isActive()) {
                hBox.getStyleClass().add("active");
                icon.setFill(Color.web("#c9a84c"));
            }

            hBox.setOnMouseClicked(_ -> {
                for (Node n : nav.getChildren()) {
                    if (n instanceof HBox) {
                        n.getStyleClass().remove("active");
                        for (Node child : ((HBox) n).getChildren()) {
                            if (child instanceof FontAwesomeIcon) {
                                ((FontAwesomeIcon) child).setFill(Color.web("#6a6058"));
                            }
                        }
                    }
                }
                hBox.getStyleClass().add("active");
                icon.setFill(Color.web("#c9a84c"));

                item.setActive(true);
                this.updateActiveNavItem(item);
            });

            nav.getChildren().add(hBox);
            this.updateActiveNavItem(item);
        }

        Label operations_label = new Label("OPERATIONS");
        operations_label.getStyleClass().add("sidebar-section-label");
        operations_label.getStyleClass().add("bordered");
        operations_label.setMaxWidth(Double.MAX_VALUE);
        nav.getChildren().add(operations_label);

        for (NavItem item : operations) {
            HBox hBox = new HBox();
            hBox.getStyleClass().add("sidebar-item");
            hBox.setSpacing(12);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setMaxWidth(Double.MAX_VALUE);

            FontAwesomeIcon icon = item.getIcon();
            icon.setSize("14");
            icon.setFill(Color.web("#6a6058"));

            Label label = new Label(item.getText());
            label.getStyleClass().add("sidebar-item-label");

            hBox.getChildren().addAll(icon, label);

            if (item.isActive()) {
                hBox.getStyleClass().add("active");
                icon.setFill(Color.web("#c9a84c"));
            }

            hBox.setOnMouseClicked(_ -> {
                for (Node n : nav.getChildren()) {
                    if (n instanceof HBox) {
                        n.getStyleClass().remove("active");
                        for (Node child : ((HBox) n).getChildren()) {
                            if (child instanceof FontAwesomeIcon) {
                                ((FontAwesomeIcon) child).setFill(Color.web("#6a6058"));
                            }
                        }
                    }
                }
                hBox.getStyleClass().add("active");
                icon.setFill(Color.web("#c9a84c"));

                item.setActive(true);
                this.updateActiveNavItem(item);
            });

            nav.getChildren().add(hBox);
            this.updateActiveNavItem(item);
        }

        Label administration_label = new Label("ADMINISTRATION");
        administration_label.getStyleClass().add("sidebar-section-label");
        administration_label.getStyleClass().add("bordered");
        administration_label.setMaxWidth(Double.MAX_VALUE);
        nav.getChildren().add(administration_label);

        for (NavItem item : administration) {
            HBox hBox = new HBox();
            hBox.getStyleClass().add("sidebar-item");
            hBox.setSpacing(12);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setMaxWidth(Double.MAX_VALUE);

            FontAwesomeIcon icon = item.getIcon();
            icon.setSize("14");
            icon.setFill(Color.web("#6a6058"));

            Label label = new Label(item.getText());
            label.getStyleClass().add("sidebar-item-label");

            hBox.getChildren().addAll(icon, label);

            if (item.isActive()) {
                hBox.getStyleClass().add("active");
                icon.setFill(Color.web("#c9a84c"));
            }

            hBox.setOnMouseClicked(_ -> {
                for (Node n : nav.getChildren()) {
                    if (n instanceof HBox) {
                        n.getStyleClass().remove("active");
                        for (Node child : ((HBox) n).getChildren()) {
                            if (child instanceof FontAwesomeIcon) {
                                ((FontAwesomeIcon) child).setFill(Color.web("#6a6058"));
                            }
                        }
                    }
                }
                hBox.getStyleClass().add("active");
                icon.setFill(Color.web("#c9a84c"));

                item.setActive(true);
                this.updateActiveNavItem(item);
            });

            nav.getChildren().add(hBox);
            this.updateActiveNavItem(item);
        }

        VBox curr_user = new VBox();
        curr_user.getStyleClass().add("sidebar-user");
        curr_user.setMaxWidth(Double.MAX_VALUE);

        Label name = new Label(user.getName());
        name.getStyleClass().add("sidebar-user-name");
        name.setMaxWidth(Double.MAX_VALUE);

        Label role = new Label(String.valueOf(user.getRole()));
        role.getStyleClass().add("sidebar-user-role");
        role.setMaxWidth(Double.MAX_VALUE);

        FontAwesomeIcon logoutIcon = new FontAwesomeIcon();
        logoutIcon.setIcon(FontAwesomeIcons.SIGN_OUT);
        logoutIcon.setSize("11");
        logoutIcon.setFill(Color.web("#6a6058"));

        Label logoutText = new Label("Sign Out");
        logoutText.getStyleClass().add("sidebar-logout-text");

        HBox logout_box = new HBox(8, logoutIcon, logoutText);
        logout_box.getStyleClass().add("sidebar-logout");
        logout_box.setAlignment(Pos.CENTER_LEFT);
        logout_box.setMaxWidth(Double.MAX_VALUE);

        logout_box.setOnMouseClicked(_ -> {
            this.setIsLoggedIn.accept(false);
            this.setCurrentUser.accept(null);
            this.updateScene.run();
        });

        curr_user.getChildren().addAll(name, role, logout_box);

        getChildren().addAll(nav, curr_user);
    }

    private void updateActiveNavItem(NavItem item) {
        if (item.isActive()) {
            this.updatePage.accept(item.getText());
        }
    }
}