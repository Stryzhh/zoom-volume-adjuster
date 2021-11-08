package Main.MainUI;

import Main.MixerControl.MixerListener;
import Main.Settings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfoenix.controls.JFXSlider;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import net.jimmc.jshortcut.JShellLink;

public class MainController implements Initializable {

    @FXML
    private JFXSlider slider;
    @FXML
    private CheckBox startup;
    private Settings settings;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MixerListener.listen();

        Gson gson = new Gson();
        File file = new File("settings.json");
        try {
            Reader reader = new FileReader(file);
            settings = gson.fromJson(reader, Settings.class);

            startup.setSelected(settings.isStartup());
            slider.setValue(settings.getVolume());
            reader.close();
        } catch (IOException e) {
            //ignore
        }

        startup.selectedProperty().addListener(e -> {
            settings.setStartup(startup.isSelected());
            try {
                modifyStartup(settings.isStartup());
                update();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        slider.valueProperty().addListener(e -> {
            settings.setVolume((int) slider.getValue());
            try {
                update();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void update() throws IOException {
        try {
            FileWriter writer = new FileWriter("settings.json");
            Gson g = new GsonBuilder().setPrettyPrinting().create();
            writer.write(g.toJson(settings));
            writer.close();
        } catch (IOException e) {
            //ignore
        }
    }

    private void modifyStartup(boolean startup) throws IOException {
        File folder = new File(System.getProperty("user.home") + "/AppData/Roaming/Microsoft/Windows/Start Menu/Programs/Startup");
        File batch = new File("zoom-volume-adjuster.bat");

        if (startup) {
            FileOutputStream output = new FileOutputStream(batch);
            DataOutputStream input = new DataOutputStream(output);
            input.writeBytes("zoom-volume-adjuster.exe");
            input.close();
            output.close();

            JShellLink link = new JShellLink();
            String filePath = JShellLink.getDirectory("") + batch.getAbsolutePath();
            link.setFolder(folder.getAbsolutePath());
            link.setName(batch.getName());
            link.setPath(filePath);
            link.save();
        } else {
            File startupFile = new File(System.getProperty("user.home") +
                    "/AppData/Roaming/Microsoft/Windows/Start Menu/Programs/Startup/zoom-volume-adjuster.bat.lnk");
            if (!startupFile.delete() || !batch.delete()) {
                System.out.print("");
            }
        }
    }

}
