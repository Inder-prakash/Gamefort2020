package com.gamefort;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.gamefort.games.Game;
import com.gamefort.games.GameDao;
import com.gamefort.users.User;
import com.gamefort.users.UserDao;


@org.springframework.web.bind.annotation.RestController
public class GameFortController {

	
	@Autowired
	UserDao udao;
	@Autowired
	GameDao gdo;
	
	@RequestMapping("/")
    @ResponseBody
    String hello() {
        return "GameBackend is running.";
    }	
	
	@PostMapping("/login")
	public String login(@RequestBody String data) {
		
		JSONObject json = new JSONObject();	
		try
		{
			JSONParser jp = new JSONParser();		
			JSONObject joObject = (JSONObject)jp.parse(data);
			User u = new User();	
			u.setEmail(joObject.get("Email").toString());
			u = udao.find(u);	
			if( u != null && u.getPassword().equals( joObject.get("Password").toString() ) )
			{	
				json.put("msg", "Success");
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
			json.put("msg", "Failure");
		}
		return json.toJSONString();

	}
	
	
	

	

	@PostMapping("/AddGame")
	public String addgame(@RequestBody String data ) throws ParseException, IOException {	
		JSONParser jp = new JSONParser();		
		JSONObject json = new JSONObject();	
		JSONObject joObject = (JSONObject)jp.parse(data);	
		Game m = new Game();
		m.setName(joObject.get("Name").toString());
		m.setSize(joObject.get("Size").toString());
		m.setStatus(joObject.get("Status").toString());
		m.setGenere(joObject.get("Genere").toString());
		gdo.insert(m);
		json.put("msg","Data Saved");
		return json.toJSONString();	
	}
	

	
	
	@PostMapping("/GetGame") 
	public JSONArray getgame(@RequestBody String data) throws ParseException {	
		JSONArray jarr = new JSONArray();
		JSONObject job = new JSONObject();	
		JSONParser jp = new JSONParser();		
		Game m = gdo.find(data);
		job.put("Id",m.getId());		
		job.put("Name",m.getName());
		job.put("Imageid",m.getImageid());
		job.put("Imageurl",m.getImageurl());
		job.put("Size",m.getSize());
		job.put("Status",m.getStatus());
		job.put("Genere",m.getGenere());
		JSONArray dlinks = new JSONArray();
		List<String> links = new ArrayList<String>();
		for(String l:m.getDownloadlink()) {
			JSONObject j = new JSONObject();	
			j.put("link",l);
			dlinks.add(j);
		}
		job.put("Downloadlinks",dlinks);
		jarr.add(job);
		return jarr;	
	}
	
	@GetMapping("/ViewGames")
	public JSONArray ViewGames() {	
		JSONArray jarr = new JSONArray();
			for(Game m : gdo.findAll()) {	
				JSONObject job = new JSONObject();	
				job.put("Id",m.getId());		
				job.put("Name",m.getName());
				job.put("Imageid",m.getImageid());
				job.put("Imageurl",m.getImageurl());
				job.put("Size",m.getSize());
				job.put("Status",m.getStatus());
				job.put("Genere",m.getGenere());
				job.put("Urls",m.getDownloadlink());
				jarr.add(job);
			}	 	
			return jarr;
	}
	
	
	@GetMapping("/ViewActiveGames")
	public JSONArray ViewActiveGames() throws ParseException {	
		JSONArray jarr = getgames("Active");
		return jarr;
	}
	
	@GetMapping("/ViewBlacklistGames")
	public JSONArray ViewBlacklistGames() throws ParseException {	
		JSONArray jarr = getgames("Blacklist");
		return jarr;
	}
	
	@GetMapping("/ViewDeletedGames")
	public JSONArray ViewDeletedGames() throws ParseException {	
		JSONArray jarr = getgames("Deleted");
		return jarr;
	}
	
	@GetMapping("/ViewcloudGames")
	public JSONArray ViewcloudGames() throws ParseException {	
		JSONArray jarr = getgames("Onlycloud");
		return jarr;
	}
	
	
	@GetMapping("/ViewallGames")
	public JSONArray ViewallGames() throws ParseException {	
		JSONArray jarr = new JSONArray();
		for(Game m : gdo.findAll()) {	
			JSONObject job = new JSONObject();	
			job.put("Id",m.getId());		
			job.put("Name",m.getName());
			job.put("Size",m.getSize());
			job.put("Status",m.getStatus());
			jarr.add(job);
		}	 	
		return jarr;
	}
	
	@GetMapping("/ViewWishedGames")
	public JSONArray ViewWishedGames() throws ParseException {	
		JSONArray jarr = getgames("Wished");
		return jarr;
	}
	
	public JSONArray getgames(String status) {
		JSONArray jarr = new JSONArray();
		for(Game m : gdo.findAll()) {	
			if( m.getStatus().equals(status)) {
				JSONObject job = new JSONObject();	
				job.put("Id",m.getId());		
				job.put("Name",m.getName());
				job.put("Imageid",m.getImageid());
				job.put("Imageurl",m.getImageurl());
				job.put("Size",m.getSize());
				job.put("Status",m.getStatus());
				job.put("Genere",m.getGenere());
				jarr.add(job);
			}	
		}	 	
		return jarr;
	}
	
	
	@GetMapping("/ViewStrategyGames")
	public JSONArray ViewStrategyGames() throws ParseException {	
		JSONArray jarr =getgamesbygenere("Strategy");
		return jarr;
	}
	
	@GetMapping("/ViewRolePlayingGames")
	public JSONArray ViewRolePlayingGames() throws ParseException {	
		JSONArray jarr = getgamesbygenere("Role Playing");
		return jarr;
	}
	
	@GetMapping("/ViewActionGames")
	public JSONArray ViewActionGames() throws ParseException {	
		JSONArray jarr = getgamesbygenere("Action");
		return jarr;
	}
	
	@GetMapping("/ViewRacingGames")
	public JSONArray ViewRacingGames() throws ParseException {	
		JSONArray jarr =getgamesbygenere("Racing");
		return jarr;
	}
	
	@GetMapping("/ViewSportsGames")
	public JSONArray ViewSportsGames() throws ParseException {	
		JSONArray jarr = getgamesbygenere("Sports");
		return jarr;
	}
	
	@GetMapping("/ViewArcadeGames")
	public JSONArray ViewArcadeGames() throws ParseException {	
		JSONArray jarr = getgamesbygenere("Arcade");
		return jarr;
	}
	
	@GetMapping("/ViewHorrorGames")
	public JSONArray ViewHorrorGames() throws ParseException {	
		JSONArray jarr = getgamesbygenere("Horror");
		return jarr;
	}
	
	public JSONArray getgamesbygenere(String genere) {
		JSONArray jarr = new JSONArray();
		for(Game m : gdo.findAll()) {	
			if( m.getGenere().equals(genere)) {
				JSONObject job = new JSONObject();	
				job.put("Id",m.getId());		
				job.put("Name",m.getName());
				job.put("Imageid",m.getImageid());
				job.put("Imageurl",m.getImageurl());
				job.put("Size",m.getSize());
				job.put("Status",m.getStatus());
				job.put("Genere",m.getGenere());
				jarr.add(job);
			}	
		}	 	
		return jarr;
	}
	
	
	@PostMapping("/updatename")
	public  String updatename(@RequestBody String data) throws ParseException {	
		JSONObject json = new JSONObject();	
		JSONParser jp = new JSONParser();		
		JSONObject joObject = (JSONObject)jp.parse(data);
		Game m = gdo.find(joObject.get("Id").toString());
		m.setName(joObject.get("Name").toString());
		gdo.update(m);
		json.put("msg","Done");
		return json.toJSONString();
	}
	
	@PostMapping("/updatestatus")
	public  String updatestatus(@RequestBody String data) throws ParseException {	
		JSONObject json = new JSONObject();	
		JSONParser jp = new JSONParser();		
		JSONObject joObject = (JSONObject)jp.parse(data);
		Game m = gdo.find(joObject.get("Id").toString());
		m.setStatus(joObject.get("Status").toString());
		gdo.update(m);
		json.put("msg","Done");
		return json.toJSONString();
	}
	
	@PostMapping("/updategenere")
	public  String updategenere(@RequestBody String data) throws ParseException {	
		JSONObject json = new JSONObject();	
		JSONParser jp = new JSONParser();		
		JSONObject joObject = (JSONObject)jp.parse(data);
		Game m = gdo.find(joObject.get("Id").toString());
		m.setGenere(joObject.get("Genere").toString());
		gdo.update(m);
		json.put("msg","Done");
		return json.toJSONString();
	}
	
	@PostMapping("/updatesize")
	public  String updatesizee(@RequestBody String data) throws ParseException {	
		JSONObject json = new JSONObject();	
		JSONParser jp = new JSONParser();		
		JSONObject joObject = (JSONObject)jp.parse(data);
		Game m = gdo.find(joObject.get("Id").toString());
		m.setSize(joObject.get("Size").toString());
		gdo.update(m);
		json.put("msg","Done");
		return json.toJSONString();
	}
	
	
	@PostMapping("/updatedownloadlinks") 
	public String updatedownloadlinks(@RequestBody String data) throws ParseException {
		JSONParser jp = new JSONParser();		
		JSONObject joObject = (JSONObject)jp.parse(data);
		JSONObject json = new JSONObject();
		Game g = gdo.find(joObject.get("Id").toString());
		List<String> glink = new ArrayList<String>();		
		JSONArray ja = (JSONArray)jp.parse(joObject.get("Downloadlinks").toString());		
		for(int i = 0; i < ja.size() ;i++) {
			glink.add(ja.get(i).toString());
	    }		
		g.setDownloadlink(glink);
		gdo.update(g);
		json.put("msg", "done");
		return json.toJSONString();
	}
	
	
	@PostMapping("/updateimage") 
	public JSONObject updateimage(@RequestBody MultipartFile file  ,String id , HttpServletRequest req) {
		JSONObject json = new JSONObject();		
			try {
				String path = req.getRealPath("/"); 
		        MultipartFile img = file;
		        byte b[] = new byte[img.getInputStream().available()];
		        img.getInputStream().read(b);
		        FileOutputStream fos = new FileOutputStream(path+img.getOriginalFilename());
		        fos.write(b);
		        fos.close();	        	        
		        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
		                "cloud_name", "df3btb7v4",
		                "api_key", "144397625466591",
		                "api_secret", "_auyDn69x8adsIwxVwMRrljcx0U"));
		        File f = new File(path+img.getOriginalFilename());		
		        Map uploadResult = cloudinary.uploader().upload(f,ObjectUtils.emptyMap());	
		        f.delete();
		        
		        Game m = gdo.find(id);
		        if(m.getImageid() == null){
		        	m.setImageurl(uploadResult.get("secure_url").toString());
		        	m.setImageid(uploadResult.get("public_id").toString());
		        	gdo.update(m);
		        }
		        else {
		        	String publickey = m.getImageid();
		        	m.setImageurl(uploadResult.get("secure_url").toString());
		        	m.setImageid(uploadResult.get("public_id").toString());
		        	gdo.update(m);
		        	cloudinary.uploader().destroy(publickey,ObjectUtils.emptyMap());        	
		        }	
				json.put("msg", "Upload successfully");
				return json;
			}
			catch( Exception e ) {
				json.put("msg", "Failes");		
				return json;
			}
	}



	@PostMapping("/DeleteGame")
	public  String DeleteGame(@RequestBody String data) throws ParseException, IOException {	
		JSONObject json = new JSONObject();	
		JSONParser jp = new JSONParser();		
		JSONObject joObject = (JSONObject)jp.parse(data);
		Game m = gdo.find(joObject.get("Id").toString());	
		if(m.getImageid() != null) {
			Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
	                "cloud_name", "df3btb7v4",
	                "api_key", "144397625466591",
	                "api_secret", "_auyDn69x8adsIwxVwMRrljcx0U"));
			cloudinary.uploader().destroy(m.getImageid(),ObjectUtils.emptyMap()); 
		}
		gdo.delete(m);
		json.put("msg","Data Deleted");
		return json.toJSONString();
	}
	
}
