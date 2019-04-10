package com.ryansusana.spotitube.users;

import com.elepy.annotations.*;
import com.elepy.http.AccessLevel;

@RestModel(name = "Users", slug = "/users")
@Create(accessLevel = AccessLevel.PUBLIC)
@Update(accessLevel = AccessLevel.DISABLED)
@Delete(accessLevel = AccessLevel.DISABLED)
@Find(accessLevel = AccessLevel.DISABLED)
public class User {

    private String id;
    private String username;
    private String password;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
