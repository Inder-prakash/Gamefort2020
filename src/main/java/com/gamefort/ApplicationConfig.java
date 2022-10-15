package com.gamefort;

import java.net.UnknownHostException;
import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@ComponentScan(basePackages = {"com.gamefort"})
@EnableWebMvc

public class ApplicationConfig {
	
	@Bean
	public MongoClient getMongoClient() {
		MongoClient mongoClient = MongoClients.create(
			    "mongodb+srv://gamer:gamer123@gamefort.iqleq.mongodb.net/gamefort?retryWrites=true&w=majority&useSSL=false");
		System.out.println("Connecting with Mongo Client ..............................");
		return mongoClient;
	}
	
	@Bean
	public MongoDatabase getNameDatabase() {
		
			MongoDatabase database = getMongoClient().getDatabase("gamefort");
			System.out.println("Connection Establish ..............................");
			return database;
	}
	
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mgo = new MongoTemplate(getMongoClient(), getNameDatabase().getName());		
		return mgo;
	}	
	
//	@Bean
//    public MongoDbFactory mongoDbFactory() throws Exception {
//		return new SimpleMongoDbFactory(mongoClient(), "gamefort");
//    }
// 
//    @Bean
//    public MongoTemplate mongoTemplate() throws Exception {
//        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
//        return mongoTemplate;
//    }
    
    
//	public @Bean MongoClient mongoClient() throws UnknownHostException {
//        return new MongoClient(new ServerAddress("ds239988.mlab.com",39988), new ArrayList<MongoCredential>() {
//            {
//                add(MongoCredential.createCredential("inder", "gamefort", "inder@123".toCharArray()));
//            }
//        });
//    }
	

    
//    @Bean(name = "multipartResolver")
//	public CommonsMultipartResolver createMultipartResolver() {
//		
//		System.out.println("Multpart ASD Resolver");
//		
//	    CommonsMultipartResolver resolver=new CommonsMultipartResolver();
//	    resolver.setDefaultEncoding("utf-8");
//	    return resolver;
//	}
}
