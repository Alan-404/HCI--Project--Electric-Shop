package com.hci.electric.services.impl;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hci.electric.models.User;
import com.hci.electric.repositories.UserRepository;
import com.hci.electric.services.UserService;
import com.hci.electric.utils.Constants;
import com.hci.electric.utils.Libraries;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user){
        try{
            user.setId(Libraries.generateId(Constants.lengthId));
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            user.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            System.out.println(user.getBirthDate());
            return this.userRepository.save(user);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public User getByEmail(String email){
        try{
            Optional<User> user = this.userRepository.getUserByEmail(email);
            if(user.isPresent() == false)
                return null;
            return user.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
