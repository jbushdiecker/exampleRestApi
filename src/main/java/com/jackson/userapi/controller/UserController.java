package com.jackson.userapi.controller;

import com.jackson.userapi.model.Address;
import com.jackson.userapi.model.Result;
import com.jackson.userapi.model.User;
import com.jackson.userapi.repo.AddressRepository;
import com.jackson.userapi.repo.UserRepository;
import com.jackson.userapi.service.UserProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserProducer producer;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Autowired
    UserController(UserProducer producer, UserRepository userRepository, AddressRepository addressRepository) {
        this.producer = producer;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        User newUser = userRepository.save(user);
        producer.sendMessage(newUser);
        return newUser;
    }

    @PostMapping("/{userId}/associateAddress/{addressId}")
    public ResponseEntity<String> associateUserWithAddress(@PathVariable String userId, @PathVariable String addressId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Address> addressOptional = addressRepository.findById(addressId);

        if(userOptional.isPresent() && addressOptional.isPresent()) {
            User user = userOptional.get();
            Address address = addressOptional.get();
            user.addAddress(addressId);
            userRepository.save(user);

            address.addUser(userId);
            addressRepository.save(address);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User or address not found", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    public List<Result> getUsers(@RequestParam String country) {
        List<Result> results = new ArrayList<>();
        List<Address> byCountry = addressRepository.findByCountry(country);
        for (Address address : byCountry) {
            List<String> userIds = address.getUserIds();
            if (userIds == null) {
                continue;
            }
            for (String userId : userIds) {
                userRepository.findById(userId).ifPresent(user -> results.add(new Result(user, address)));
            }
        }
        return results;
    }
}
