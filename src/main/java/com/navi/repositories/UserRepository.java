package com.navi.repositories;

import com.navi.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRepository {
    private Map<String, User> userMap = new HashMap<>();

    public User saveUser(User user) {
        if(userMap.containsKey(user.getUserName())) {
            userMap.remove(user);
        }
        userMap.put(user.getUserName(), user);
        return user;
    }

    public User getUserByName(String name) {
        return userMap.get(name);
    }
}
