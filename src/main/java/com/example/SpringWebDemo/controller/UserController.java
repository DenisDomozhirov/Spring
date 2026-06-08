package com.example.SpringWebDemo.controller;

import com.example.SpringWebDemo.Service.UserService;
import com.example.SpringWebDemo.model.User;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(
            @RequestBody @Valid User userToCreate
    ){
        log.info("User create = {}", userToCreate);
        var createUser = userService.createNewUser(userToCreate);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createUser);
    }

    @GetMapping("/showAll")
    public List<User> getAllUsers(
            @RequestParam(value = "user-name", required = false) String userName,
            @RequestParam(value = "user-age", required = false) Integer userAge
    ){
        log.info("Get request for getAllUsers");
        return userService.showAllUsers(userName, userAge);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable("id")Long id,
            @RequestBody @Valid User userToUpdate
    ){
        log.info("Put request value update = {}", userToUpdate);
        var updateUser = userService.updateUsers(id, userToUpdate);
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED)
                .body(updateUser);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") Long id
    ){
        log.info("Delete request done = {}", id);
        userService.deleteUserById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<User> findUserById(
            @PathVariable("id") Long id
    ){
        log.info("Get request for findUserById done!");
        userService.findUserById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
