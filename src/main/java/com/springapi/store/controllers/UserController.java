package com.springapi.store.controllers;

import com.springapi.store.dtos.ChangePasswordRequest;
import com.springapi.store.dtos.RegisterUserRequest;
import com.springapi.store.dtos.UpdateUserRequest;
import com.springapi.store.dtos.UserDto;
import com.springapi.store.mappers.UserMapper;
import com.springapi.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public Iterable<UserDto> getAllUsers(
            @RequestHeader(required = false, name = "x-auth-token") String authToken,
            @RequestParam(required = false, defaultValue = "id", name = "sort") String sortBy) {

        System.out.println(authToken);
        if (!Set.of("name", "email", "id").contains(sortBy))
            sortBy = "id";

        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(UriComponentsBuilder uriBuilder,
                                              @RequestBody RegisterUserRequest registerUserRequest) {
        var user = userMapper.toEntity(registerUserRequest);
        userRepository.save(user);
        var userDto = userMapper.toDto(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") Long id,
                                              @RequestBody UpdateUserRequest updateUserRequest) {
        var user = userRepository.findById(id).orElse(null);

        if (user == null)
            return ResponseEntity.notFound().build();

        userMapper.update(updateUserRequest, user);
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ResponseEntity.notFound().build();

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable long id,
                                               @RequestBody ChangePasswordRequest request){
        var user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ResponseEntity.notFound().build();

        if (!user.getPassword().equals(request.getOldPassword()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }
}
