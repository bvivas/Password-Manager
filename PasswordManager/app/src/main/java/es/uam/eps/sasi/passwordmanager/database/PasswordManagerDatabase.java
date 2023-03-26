package es.uam.eps.sasi.passwordmanager.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.jetbrains.annotations.NotNull;

import es.uam.eps.sasi.passwordmanager.Site;
import es.uam.eps.sasi.passwordmanager.User;

@Database(entities = {User.class, Site.class}, version = 1, exportSchema = false)
public abstract class PasswordManagerDatabase extends RoomDatabase {

    public abstract PasswordManagerDAO getPasswordManagerDAO();

    private static volatile PasswordManagerDatabase INSTANCE = null;

    public static PasswordManagerDatabase getInstance(@NotNull Context context) {
        PasswordManagerDatabase instance = PasswordManagerDatabase.INSTANCE;

        if(instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    PasswordManagerDatabase.class,
                    "passwordmanager_database"
            )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

            PasswordManagerDatabase.INSTANCE = instance;
        }
        return instance;
    }
}
