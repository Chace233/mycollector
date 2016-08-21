package webcollector;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.SQLException;



public class WriteInDB {	
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/crawl?characterEncoding=utf8&useSSL=true","root","12345");
		return con;
	}	
	public static void writeInDB_xincailiao(String title_s,String author_s,String summary_s,String postdate_s,Date writeDate_s,String content_s,String source_s,String picUrl_s) throws ClassNotFoundException, SQLException{
		WriteInDB c = new WriteInDB();
		Connection con = c.getConnection();
		PreparedStatement sql;
		sql = con.prepareStatement("Insert into xincailiao(title,author,summary,postDate,writeDate,content,source,picUrl) value('"+title_s+"','"+author_s+"','"+summary_s+"','"+postdate_s+"','"+writeDate_s+"','"+content_s+"','"+source_s+"','"+picUrl_s+"')");
		sql.executeUpdate();
		}
	public static void writeInDB_techcrunch(String title_s,String author_s,String content_s,String picUrl_s,String postDate_s, Date writeDate_s,String source_s) throws ClassNotFoundException, SQLException{
		WriteInDB c = new WriteInDB();
		Connection con = c.getConnection();
		PreparedStatement sql;
		sql = con.prepareStatement("Insert into techcrunch(title,author,content,picUrl,postDate,writeDate,source) value('"+title_s+"','"+author_s+"','"+content_s+"','"+picUrl_s+"','"+postDate_s+"','"+writeDate_s+"','"+source_s+"')");
		sql.executeUpdate();
		}
/*	public static void writeInDB_36kr(String title, String content, String author, String picUrl, String postDate, Date writeDate, String source) throws ClassNotFoundException, SQLException{
		WriteInDB c = new WriteInDB();
		Connection con = c.getConnection();
		PreparedStatement sql;
		sql = con.prepareStatement("Insert into 36kr(title, author , content, picUrl, postDate, writeDate, source) value('"+title+"','"+author+"','"+content+"','"+picUrl+"','"+postDate+"','"+writeDate+"','"+source+"')");
		sql.executeUpdate();
		}*/
	public static void writeInDB_36kr(String title, String author,String content,String picurl,Date writedate,String source) throws ClassNotFoundException, SQLException{
		WriteInDB c = new WriteInDB();
		Connection con = c.getConnection();
		PreparedStatement sql;
		sql = con.prepareStatement("Insert into 36kr(title,author,content,picUrl,writeDate,source) value('"+title+"','"+author+"','"+content+"','"+picurl+"','"+writedate+"','"+source+"')");
		sql.executeUpdate();
		}
	public static void writeInDB_adafruit(String name_s,String price_s,String category_s,String tag_s,String picurl_s,String technicalDetail_s,String source_s) throws ClassNotFoundException, SQLException{
		WriteInDB c = new WriteInDB();
		Connection con = c.getConnection();
		PreparedStatement sql;
		sql = con.prepareStatement("Insert into adafruit(name,price,category,tag,picUrl,technicalDetail,source) value('"+name_s+"','"+price_s+"','"+category_s+"','"+tag_s+"','"+picurl_s+"','"+technicalDetail_s+"','"+source_s+"')");
		sql.executeUpdate();
		}
	}
