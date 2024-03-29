package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.User;

public interface UserService {
    public User save(User user);
    public User edit(User user);
    public User getByEmail(String email);
    public User getById(String id);
    public User getByPhone(String phone);
    public List<User> getUsers();
}
