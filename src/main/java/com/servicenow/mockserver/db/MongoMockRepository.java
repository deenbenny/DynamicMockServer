package com.servicenow.mockserver.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.servicenow.mockserver.model.MockAPI;

@Repository
public interface MongoMockRepository extends MongoRepository<MockAPI, String> {

	MockAPI findFirstByApiName(String apiName);

	MockAPI findFirstById(String id);

}