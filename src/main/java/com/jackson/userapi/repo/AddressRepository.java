package com.jackson.userapi.repo;

import com.jackson.userapi.model.Address;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AddressRepository extends MongoRepository<Address, String> {
    List<Address> findByCountry(String country);
}
