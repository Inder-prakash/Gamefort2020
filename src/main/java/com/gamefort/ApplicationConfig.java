package com.gamefort;

import java.net.UnknownHostException;
import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@ComponentScan(basePackages = {"com.gamefort"})
@EnableWebMvc

public class ApplicationConfig {
	
	public @Bean MongoClient mongoClient() throws UnknownHostException {
        return new MongoClient(
        		new MongoClientURI("mongodb+srv://gamer:gamer123@gamefort.iqleq.mongodb.net/gamefort?"
        				+ "retryWrites=true&w=majority"));
	}
	
	
//	public @Bean MongoClient mongoClient() throws UnknownHostException {
//        return new MongoClient(new ServerAddress("ds239988.mlab.com",39988), new ArrayList<MongoCredential>() {
//            {
//                add(MongoCredential.createCredential("inder", "gamefort", "inder@123".toCharArray()));
//            }
//        });
//    }
	
	@Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(mongoClient(), "gamefort");
    }
 
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }
    
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
