package Main;

import java.awt.*;
import java.io.File;
import java.net.URL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        Platform.setImplicitExit(false);
        Parent startWindow = FXMLLoader.load(new URL("file:/" + System.getProperty("user.dir") + "\\src\\Main\\MainUI\\main.fxml"));

        //adds tray icon
        PopupMenu popupMenu = new PopupMenu();
        ImageIcon logo = new ImageIcon("src\\adjuster.png");
        Image image = logo.getImage();

        SystemTray tray = SystemTray.getSystemTray();
        Image trayImage = image.getScaledInstance(tray.getTrayIconSize().width,
                tray.getTrayIconSize().height, java.awt.Image.SCALE_SMOOTH);
        TrayIcon trayIcon = new TrayIcon(trayImage, "ZoomVolumeAdjuster", popupMenu);

        MenuItem open = new MenuItem("Open Zoom Adjuster");
        MenuItem exit = new MenuItem("Close Zoom Adjuster");
        open.addActionListener(e -> Platform.runLater(() -> {
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            window.setX(bounds.getMinX() + (bounds.getWidth() - 310) * 1.0f / 2);
            window.setY(bounds.getMinY() + (bounds.getHeight() - 110) * 1.0f / 3);
            window.setIconified(false);
            window.show();
        }));
        exit.addActionListener(e -> {
            tray.remove(trayIcon);
            System.exit(0);
        });
        popupMenu.add(open);
        popupMenu.add(exit);
        tray.add(trayIcon);

        window.setResizable(false);
        window.setScene(new Scene(startWindow, 310, 110));
        window.iconifiedProperty().addListener((ov, max, min) -> {
            if (min) {
                window.hide();
            }
        });
        window.setOnCloseRequest(e -> System.exit(0));
        window.setTitle("Zoom Volume Adjuster");
        window.getIcons().add(new javafx.scene.image.Image("file:/" + new File("src/adjuster.png").getAbsolutePath()));
        window.setIconified(false);
        window.centerOnScreen();
        window.show();
    }

}
