package com.gamefort.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("UserDao")
public class UserDaoImpl implements UserDao {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	final String COLLECTION = "users";

	public User find(User User) {
		Query query = new Query(Criteria.where("email").is(User.getEmail()));
        return mongoTemplate.findOne(query, User.class, COLLECTION);
        
	}
}
