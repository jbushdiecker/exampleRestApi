package com.jackson.userapi;

import com.jackson.userapi.controller.AddressController;
import com.jackson.userapi.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private AddressController addressController;

	@Autowired
	private UserController userController;

	@Test
	void contextLoads() {
		assertThat(addressController).isNotNull();
		assertThat(userController).isNotNull();
	}

}
