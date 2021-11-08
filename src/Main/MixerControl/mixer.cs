public static void SetApplicationVolume(int volume) {
	const string app = "Zoom Meeting";

    foreach (string name in EnumerateApplications()) {        
        if (name == app) {
            SetApplicationVolume(app, volume);
        }
    }
}