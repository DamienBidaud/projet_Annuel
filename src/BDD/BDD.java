package BDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class BDD {
	
	private Connection conn;
	public BDD(){
		conn = connect();
	}
	
	public Connection connect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver OK");
			
			String url = "jdbc:mysql://localhost:3307/buntan";
			String user = "root";
			String passwd = "esgi";
			
			Connection conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("Connection OK");
			return conn;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	
	}
	
	public ArrayList selectMusic(int idUser){
		try {
			ArrayList media = new ArrayList();
			Statement state = (Statement) conn.createStatement();
			ResultSet result = state.executeQuery("SELECT DISTINCT file_road FROM media,music,appartien where media.id_media = music.id_media and appartien.id_user_fk ="+idUser+"");
			ResultSetMetaData resultMeta = (ResultSetMetaData) result.getMetaData();
				         
			while(result.next()){         
				for(int i = 1; i <= resultMeta.getColumnCount(); i++){
					media.add(result.getObject(i).toString());        
				}
			}

			result.close();
			state.close();
			return media;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList selectVideo(int idUser){
		try {
			ArrayList media = new ArrayList();
			Statement state = (Statement) conn.createStatement();
		    ResultSet result = state.executeQuery("SELECT DISTINCT file_road FROM media,video,appartien where media.id_media = video.idmedia and appartien.id_user_fk ="+idUser+"");
		    ResultSetMetaData resultMeta = (ResultSetMetaData) result.getMetaData();
				         
		      while(result.next()){         
		        for(int i = 1; i <= resultMeta.getColumnCount(); i++)
		           media.add(result.getObject(i).toString());
		            
		      }

		      result.close();
		      state.close();
		      return media;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String selectElement(String name){
		String road = "";
		try{
			Statement state = (Statement) conn.createStatement();
			ResultSet result = state.executeQuery("SELECT file_road FROM media WHERE Name='"+name+"'");
			ResultSetMetaData resultMeta = (ResultSetMetaData) result.getMetaData();
			road = result.getObject(0).toString();
			result.close();
			state.close();
			return road;
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	public boolean idUser(String name, String paswd){
		try{
			Statement state =  conn.createStatement();
			ResultSet result = state.executeQuery("SELECT * FROM user where name LIKE '"+name+"' AND password LIKE '"+paswd+"'");
			ResultSetMetaData resultMeta = result.getMetaData();
			while(result.next()){
				for(int i = 1; i <= resultMeta.getColumnCount(); i++){
					if(result.getObject(i).toString() != null){
						System.out.println(result.getObject(i).toString());
						return true;
					}
					else
						return false;
				}
			}
			result.close();
			state.close();
			return false;
		}
		catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void insertUser(String name, String pswd, String email){
		try {
			Statement state = conn.createStatement();
			int result = state.executeUpdate("INSERT INTO `user`(`password`,`name`,`mail_address`)VALUES('"+pswd+"','"+name+"','"+email+"')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int idUserExpo(String name){
		try{
			Statement state = conn.createStatement();
			ResultSet result = state.executeQuery("Select iduser from user where name='"+name+"'");
			result.next();
			return Integer.parseInt(result.getObject(1).toString());			
		}
		catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public void insertMusic(String name, String fileRoad, int id_user){
		try{
			int id = 0;
			StringTokenizer st = new StringTokenizer(fileRoad, "\\");
			fileRoad = "?";
			while(st.hasMoreTokens()){
				fileRoad += st.nextToken()+"\\\\";
				
			}
			fileRoad += "\\";
			fileRoad = fileRoad.substring(0, fileRoad.length()-1);

			Statement state = conn.createStatement();
			ResultSet result = state.executeQuery("SELECT id_media from media where Name like '"+name+"'");
			if(!result.next()){
				state.executeUpdate("INSERT INTO `media`(`Name`,`file_road`)VALUES('"+name+"','"+fileRoad+"')");				
				result = state.executeQuery("SELECT id_media from media where Name like '"+name+"'");
				result.next();
				id = Integer.parseInt(result.getObject(1).toString());
				state.executeUpdate("INSERT INTO `music`(`id_media`)VALUES("+id+")");
				state.executeUpdate("INSERT INTO `appartien`(`id_media_fk`, `id_user_fk`)VALUES("+id+", "+id_user+")");

				result.close();
				state.close();
			}
			else{
				result = state.executeQuery("SELECT id_media from media where Name like '"+name+"'");
				result.next();
				id = Integer.parseInt(result.getObject(1).toString());
				state.executeUpdate("INSERT INTO `appartien`(`id_media_fk`, `id_user_fk`)VALUES("+id+", "+id_user+")");
				result.close();
				state.close();
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	
}