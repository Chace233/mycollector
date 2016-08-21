package webcollector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateTable {
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/crawl?characterEncoding=utf8&useSSL=true","root","12345");
		return con;
	}	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		WriteInDB c = new WriteInDB();
		Connection con = c.getConnection();
		PreparedStatement sql_xincailiao,sql_adafruit,sql_36kr,sql_techcrunch;
		/*sql_xincailiao = con.prepareStatement("create table xincailiao("
				                            +"id int AUTO_INCREMENT PRIMARY KEY,"
				                            +"title varchar(255) NOT NULL UNIQUE,"
				                            +"author varchar(50),"		                            
				                            +"summary varchar(1000),"
				                            +"content varchar(10000),"
				                            +"picUrl varchar(50),"
				                            +"postDate varchar(15),"
				                            +"writeDate Date,"
				                            +"source varchar(100)" +")");
		sql_xincailiao.execute();
		sql_adafruit = con.prepareStatement("create table adafruit("
				                           +"id int AUTO_INCREMENT PRIMARY KEY,"
				                           +"name varchar(255),"
				                           +"price varchar(255),"
				                           +"picUrl varchar(255),"
				                           +"category varchar(20),"
				                           +"tag varchar(255),"
				                           +"description varchar(10000),"
				                           +"technicalDetail varchar(500),"
				                           +"source varchar(50)"+")");
		sql_adafruit.execute();*/
		sql_36kr = con.prepareStatement("create table 36kr("
				                           +"id int AUTO_INCREMENT PRIMARY KEY,"
				                           +"title varchar(255) NOT NULL UNIQUE,"
				                           +"author varchar(50),"
				                           +"content varchar(12000),"
				                           +"picUrl varchar(200),"
				                           +"postDate varchar(30),"
				                           +"writeDate Date,"
				                           +"source varchar(100)"+")");
		sql_36kr.execute();
		/*sql_techcrunch = con.prepareStatement("create table techcrunch("
				                           +"id int AUTO_INCREMENT PRIMARY KEY,"
				                           +"title varchar(255) NOT NULL UNIQUE,"
				                           +"author varchar(50),"
				                           +"content varchar(10000),"
				                           +"picUrl varchar(120),"
				                           +"postDate varchar(30),"
				                           +"writeDate Date,"
				                           +"source varchar(300)"+")");
		sql_techcrunch.execute();*/
		
	}

}
