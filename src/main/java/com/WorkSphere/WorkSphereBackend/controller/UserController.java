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
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    // ✅ ADD USER
//    @PostMapping
//    public ResponseEntity<UserDetailsDto> addUser(@RequestBody Users users) {
//        System.out.println("post controller");
//        UserDetailsDto savedUser = userService.addUser(users);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
//    }

    // ✅ DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeUser(@PathVariable Integer id) {
        System.out.println("delete controller");
        userService.removeUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // ✅ GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDto> getUserById(@PathVariable Integer id) {
        System.out.println("get by id controller");
        UserDetailsDto users = userService.getUserById(id);
        return ResponseEntity.ok(users);
    }

    // ✅ GET ALL USERS
    @GetMapping
    public ResponseEntity<List<UserDetailsDto>> getAllUsers() {
        System.out.println("get controller");
        List<UserDetailsDto> usersList = userService.getAllUsers();
        return ResponseEntity.ok(usersList);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDetailsDto> updateUser(
            @PathVariable Integer id,
            @RequestBody Users users) {

        UserDetailsDto updatedUser = userService.updateUser(id, users);
        return ResponseEntity.ok(updatedUser);
    }
    
}
