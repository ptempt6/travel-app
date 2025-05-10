package com.example.travelapp.cache;

import com.example.travelapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserCache extends LfuCache<User> {
    public UserCache() {
        super(2);
    }
}
