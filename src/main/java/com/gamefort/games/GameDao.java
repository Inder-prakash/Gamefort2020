package com.gamefort.games;

import java.util.List;

public interface GameDao {
	public void insert(Game m);
	public void update(Game m);
	public void delete(Game m);
	public Game find(String data);
	public List<Game> findAll();
}
