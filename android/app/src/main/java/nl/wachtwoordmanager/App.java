package nl.wachtwoordmanager;

import android.app.Application;

public class App extends Application {

    private static Kluis kluis;

    @Override
    public void onCreate() {
        super.onCreate();
        kluis = new Kluis(this);
    }

    public static Kluis getKluis() {
        return kluis;
    }
}
