
package com.example.villagerems;

import java.util.HashMap;
import java.util.Map;

public class UserStore {
    private static final Map<String, String> users = new HashMap<>();
    static {
        users.put("admin", "admin"); // default user
    }
    public static boolean validate(String user, String pass) {
        return users.containsKey(user) && users.get(user).equals(pass);
    }
    public static boolean register(String user, String pass) {
        if (users.containsKey(user)) return false;
        users.put(user, pass);
        return true;
    }
}