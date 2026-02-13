package com.WorkSphere.WorkSphereBackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.WorkSphere.WorkSphereBackend.dto.UserDetailsDto;
import com.WorkSphere.WorkSphereBackend.entity.Users;
import com.WorkSphere.WorkSphereBackend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

 // add user
    @PostMapping("/register") 
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
    	try {
            UserDetailsDto savedUser = userService.addUser(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
    

    // ✅ DELETE USER
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<String> removeUser(@PathVariable Integer id) {
        System.out.println("delete controller");
        userService.removeUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // ✅ GET USER BY ID
    @GetMapping("/get-user/{id}")
    public ResponseEntity<UserDetailsDto> getUserById(@PathVariable Integer id) {
        System.out.println("get by id controller");
        UserDetailsDto users = userService.getUserById(id);
        return ResponseEntity.ok(users);
    }

    // ✅ GET ALL USERS
    @GetMapping("/get-users")
    public ResponseEntity<List<UserDetailsDto>> getAllUsers() {
        System.out.println("get controller");
        List<UserDetailsDto> usersList = userService.getAllUsers();
        return ResponseEntity.ok(usersList);
    }
    
    @PutMapping("/update-user/{id}")
    public ResponseEntity<UserDetailsDto> updateUser(
            @PathVariable Integer id,
            @RequestBody Users users) {

        UserDetailsDto updatedUser = userService.updateUser(id, users);
        return ResponseEntity.ok(updatedUser);
    }
    
}
