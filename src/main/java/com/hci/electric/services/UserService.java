package com.hci.electric.services;

import com.hci.electric.models.User;

public interface UserService {
    public User save(User user);
    public User getByEmail(String email);
}
