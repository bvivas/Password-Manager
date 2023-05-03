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

    @Query("SELECT * FROM user WHERE user_name = :username")
    User getUser(String username);

    @Query("SELECT user_password FROM user WHERE user_name = :username")
    String getPassword(String username);

    @Delete
    void deleteUser(User user);

    @Query("SELECT user_password FROM user WHERE user_name = :username")
    String getUserPassword(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSite(Site site);

    @Query("SELECT * FROM site ORDER BY site_name ASC")
    List<Site> getSites();

    @Query("SELECT * FROM site WHERE user_name = :username ORDER BY site_name ASC")
    List<Site> getUserSites(String username);

    @Query("SELECT * FROM site WHERE site_id = :site_id AND user_name = :username LIMIT 1")
    Site getSiteById(String site_id, String username);

    @Delete
    void deleteSite(Site site);
}
