package es.uam.eps.sasi.passwordmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import es.uam.eps.sasi.passwordmanager.Site;
import es.uam.eps.sasi.passwordmanager.User;

@Dao
public interface PasswordManagerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(User user);

    @Query("SELECT * FROM user ORDER BY user_name ASC")
    List<User> getUsers();

    @Delete
    void deleteUser(User user);

    @Query("SELECT user_password FROM user WHERE user_name = :username")
    String getUserPassword(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSite(Site site);

    @Query("SELECT * FROM site ORDER BY site_name ASC")
    List<Site> getSites();

    @Delete
    void deleteSite(Site site);
}
