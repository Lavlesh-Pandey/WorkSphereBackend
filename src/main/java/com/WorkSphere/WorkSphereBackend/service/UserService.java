package com.WorkSphere.WorkSphereBackend.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.WorkSphere.WorkSphereBackend.dto.UserDetailsdto;
import com.WorkSphere.WorkSphereBackend.entity.Users;
import com.WorkSphere.WorkSphereBackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ✅ ADD USER
    public UserDetailsdto addUser(Users users) {

        Users savedUser = userRepository.save(users);
        return convertToDto(savedUser);
    }

    // ✅ REMOVE USER
    public void removeUser(Integer userId) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found with id: " + userId));

        userRepository.delete(user);
    }

    // ✅ GET USER BY ID
    public UserDetailsdto getUserById(Integer userId) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found with id: " + userId));

        return convertToDto(user);
    }

    // ✅ GET ALL USERS
    public List<UserDetailsdto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // ✅ UPDATE USER
    public UserDetailsdto updateUser(Integer userId, Users updatedUser) {

        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found with id: " + userId));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setRole(updatedUser.getRole());

        Users savedUser = userRepository.save(existingUser);

        return convertToDto(savedUser);
    }

    // ✅ ENTITY → DTO MAPPER
    private UserDetailsdto convertToDto(Users user) {

        return UserDetailsdto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
