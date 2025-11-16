package com.driver.services.impl;

import com.driver.model.User;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    public  UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void deleteUser(Integer userId) {
        try{
            Optional<User> userOptional = userRepository.findById(userId);
            if(userOptional.isPresent()){
                User user = userOptional.get();

                userRepository.delete(user);
            }
            throw new ServiceException("User not Found");
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public User updatePassword(Integer userId, String password) {
        try{
            Optional<User> userOptional = userRepository.findById(userId);
            if(userOptional.isPresent()){
                User user = userOptional.get();
                user.setPassword(password);

                userRepository.save(user);
                return user;
            }
            throw new ServiceException("User not Found");
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void register(String name, String phoneNumber, String password) {
        try{
            User user = new User();
            user.setName(name);
            user.setPassword(password);
            user.setPhoneNumber(phoneNumber);

            userRepository.save(user);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
}
