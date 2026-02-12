package com.WorkSphere.WorkSphereBackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.WorkSphere.WorkSphereBackend.dto.UserDetailsdto;
import com.WorkSphere.WorkSphereBackend.entity.Users;
import com.WorkSphere.WorkSphereBackend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ ADD USER
    @PostMapping
    public ResponseEntity<UserDetailsdto> addUser(@RequestBody Users users) {
        System.out.println("post controller");
        UserDetailsdto savedUser = userService.addUser(users);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // ✅ DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeUser(@PathVariable Integer id) {
        System.out.println("delete controller");
        userService.removeUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // ✅ GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsdto> getUserById(@PathVariable Integer id) {
        System.out.println("get by id controller");
        UserDetailsdto users = userService.getUserById(id);
        return ResponseEntity.ok(users);
    }

    // ✅ GET ALL USERS
    @GetMapping
    public ResponseEntity<List<UserDetailsdto>> getAllUsers() {
        System.out.println("get controller");
        List<UserDetailsdto> usersList = userService.getAllUsers();
        return ResponseEntity.ok(usersList);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDetailsdto> updateUser(
            @PathVariable Integer id,
            @RequestBody Users users) {

        UserDetailsdto updatedUser = userService.updateUser(id, users);
        return ResponseEntity.ok(updatedUser);
    }
    
}
