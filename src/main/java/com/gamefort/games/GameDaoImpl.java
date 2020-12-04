package com.gamefort.games;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository("GameDao")
public class GameDaoImpl implements GameDao {
	
	@Autowired
	MongoTemplate mongoTemplate;
	final String COLLECTION = "games";

	public void insert(Game m) {
		mongoTemplate.insert(m);
	}

	public void update(Game m) {
		mongoTemplate.save(m);
	}

	public void delete(Game m) {
		mongoTemplate.remove(m);
	}

	public Game find(String id) {
		Game m = mongoTemplate.findById(id, Game.class, COLLECTION);
		return m;	
	}

	public List<Game> findAll() {
		return (List <Game> ) mongoTemplate.findAll(Game.class);
	}

}
