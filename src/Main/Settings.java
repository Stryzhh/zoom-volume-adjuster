package Main;

public class Settings {

    private boolean startup;
    private int volume;

    public Settings(boolean startup, int volume) {
        super();
        this.startup = startup;
        this.volume = volume;
    }

    public void setStartup(boolean value) {
        startup = value;
    }

    public boolean isStartup() {
        return startup;
    }

    public void setVolume(int value) {
        volume = value;
    }

    public int getVolume() {
        return volume;
    }

}
