package com.jackson.userapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackson.userapi.model.Address;
import com.jackson.userapi.repo.AddressRepository;
import com.jackson.userapi.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
public class AddressControllerTests {
    private static final Address ADDRESS = new Address("line 1", "line2", "city", "state", "zip", "country", new ArrayList<>(Collections.singletonList("1")));

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressRepository addressRepository;

    @Test
    void testCreateAddress() throws Exception {
        String content = new ObjectMapper().writeValueAsString(ADDRESS);

        when(addressRepository.save(ADDRESS)).thenReturn(ADDRESS);

        mockMvc.perform(MockMvcRequestBuilders.post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().json(content));
    }
}
