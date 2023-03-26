package es.uam.eps.sasi.passwordmanager;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_name")
    private String name;
    @ColumnInfo(name = "user_password")
    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    // Getters and setters
    public String getName() { return this.name; }
    public String getPassword() { return this.password; }

    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
}
