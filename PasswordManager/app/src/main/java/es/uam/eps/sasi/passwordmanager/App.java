package es.uam.eps.sasi.passwordmanager;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDAO;
import es.uam.eps.sasi.passwordmanager.database.PasswordManagerDatabase;

public class App extends Application {

    private static App app = null;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        final PasswordManagerDatabase passwordManagerDatabase = PasswordManagerDatabase.getInstance(this);
        PasswordManagerDAO passwordManagerDAO = passwordManagerDatabase.getPasswordManagerDAO();
    }

    public static Context getContext() { return app.getApplicationContext(); }
}
