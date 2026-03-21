package com.ecom.ecom_api.service;

import com.ecom.ecom_api.model.User;
import com.ecom.ecom_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        User existing = getUserById(id);
        existing.setEmail(updatedUser.getEmail());
        existing.setName(updatedUser.getName());
        return userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        getUserById(id); // Ensure user exists before deleting
        userRepository.deleteById(id);
    }

}
