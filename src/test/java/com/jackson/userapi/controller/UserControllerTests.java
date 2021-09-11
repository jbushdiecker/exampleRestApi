package com.jackson.userapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackson.userapi.model.Address;
import com.jackson.userapi.model.Result;
import com.jackson.userapi.model.User;
import com.jackson.userapi.repo.AddressRepository;
import com.jackson.userapi.repo.UserRepository;
import com.jackson.userapi.service.UserProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTests {
    private static final User USER = new User("first", "last", "email", "pass", new ArrayList<>());
    private static final Address ADDRESS = new Address("line 1", "line2", "city", "state", "zip", "country", new ArrayList<>(Collections.singletonList("1")));

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private UserProducer producer;

    @Test
    void testCreateUser() throws Exception {
        when(userRepository.save(USER)).thenReturn(USER);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(USER)))
                .andExpect(status().isOk());

        verify(userRepository, times(1)).save(USER);
        verify(producer, times(1)).sendMessage(USER);
    }

    @Test
    void testAssociateAddress() throws Exception {
        when(addressRepository.findById(anyString())).thenReturn(Optional.of(ADDRESS));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(USER));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/associateAddress/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testAssociateAddressNotFound() throws Exception {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/associateAddress/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testFindUsersByCountry() throws Exception {
        List<Result> results = Collections.singletonList(new Result(USER, ADDRESS));
        String country = "country";
        when(addressRepository.findByCountry(country)).thenReturn(Collections.singletonList(ADDRESS));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(USER));

        mockMvc.perform(MockMvcRequestBuilders.get("/users").param("country", country))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(results)));

        verify(addressRepository, times(1)).findByCountry(country);
        verify(userRepository, times(1)).findById("1");
    }

    @Test
    void testFindUsersByCountryNotFound() throws Exception {
        String country = "country";

        mockMvc.perform(MockMvcRequestBuilders.get("/users").param("country", country))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(Collections.emptyList())));

        verify(addressRepository, times(1)).findByCountry(country);
        verify(userRepository, times(0)).findById(anyString());
    }
}
