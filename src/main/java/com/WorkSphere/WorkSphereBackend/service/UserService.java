package com.WorkSphere.WorkSphereBackend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.WorkSphere.WorkSphereBackend.dto.UserDetailsDto;
import com.WorkSphere.WorkSphereBackend.entity.Users;
import com.WorkSphere.WorkSphereBackend.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import com.WorkSphere.WorkSphereBackend.mapper.UserMapper;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // ✅ ADD USER
    public UserDetailsDto addUser(Users user) {

    	Users doesExist = usersRepository.findByEmail(user.getEmail()).orElse(null);
    	if (doesExist != null) {
    		throw new IllegalArgumentException("User Already Exists."); 
    	}
        
        Users newUser = new Users();
         
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));  // 🔥 IMPORTANT
        newUser.setRole(user.getRole());
        newUser.setPhone(user.getPhone());

        Users savedUser = usersRepository.save(newUser);
        return userMapper.toUserDetailsDto(savedUser);
    }

    // ✅ REMOVE USER
    public void removeUser(Integer userId) {

        Users user = usersRepository.findById(userId)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found with id: " + userId));

        usersRepository.delete(user);
    }

    // ✅ GET USER BY ID
    public UserDetailsDto getUserById(Integer userId) {

        Users user = usersRepository.findById(userId)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found with id: " + userId));

        return userMapper.toUserDetailsDto(user);
    }
    
 // GET USER BY EMAIL
    public UserDetailsDto getUserByEmail(String email) {

        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found with email: " + email));

        return userMapper.toUserDetailsDto(user);
    }

    // ✅ GET ALL USERS
    public List<UserDetailsDto> getAllUsers() {


        List<Users> users = usersRepository.findAll();
        List<UserDetailsDto> userDtos = new ArrayList<>();

        for (Users user : users) {
            UserDetailsDto dto = userMapper.toUserDetailsDto(user);
            userDtos.add(dto);
        }

        return userDtos;
    }

    // ✅ UPDATE USER
    public UserDetailsDto updateUser(Integer userId, Users updatedUser) {

        Users existingUser = usersRepository.findById(userId)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found with id: " + userId));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setRole(updatedUser.getRole());

        Users savedUser = usersRepository.save(existingUser);

        return userMapper.toUserDetailsDto(savedUser);
    }

    
}
