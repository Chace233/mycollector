package webcollector;		


import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.util.JsoupUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class KrCrawler extends BreadthCrawler {
	static String fileName;
	
    public KrCrawler(String crawlPath, boolean autoParse) {
	    super(crawlPath, autoParse);
	    this.addSeed("http://36kr.com");
	    this.addRegex("http://36kr.com/p/.*html");
	    this.addRegex("-.*\\.(jpg|png|gif).*");
	    this.addRegex("-.*#.*");
    }

    @Override
    public void visit(Page page, CrawlDatums next) {
    	ArrayList<String> list = Functions.getUrls(page.getHtml(), "http://36kr.com/p/.*?html");
    	
    	//参数的意义依次是，当前的URL，当前获取到的列表，需要点击加载跟多次数
    	//Functions.MAX_CLICK_TIMES 最大点击加载更多的次数，默认32，按需设置
    	//Functions.ENABLE_AJAX_SLEEP 是否模拟点击睡眠，默认false，按需设置
    	//Functions.AJAX_SLEEP_TIME  每次模拟点击睡眠时长，默认10ms，单位ms，按需更改
    	Functions.patcOf36Kr(page.getUrl(), list, 12);
    	
    	System.out.println(list);
    	for(String url : list){
    		try {
				try {
					JsoupConnect(url);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    	public static void downloadImg(String src) throws IOException{ 
        fileName = Functions.getFileName(src);
    	URL url = new URL(src);
    	URLConnection uc = url.openConnection();
    	InputStream in = uc.getInputStream();
    	File file = new File("download/36kr/"+fileName);
    	@SuppressWarnings("resource")   
		FileOutputStream out = new FileOutputStream(file);
    	int i = 0;
    	while ((i = in.read()) != -1){
    		out.write(i);
    	}
    	in.close();
    }
    
    
    public void JsoupConnect(String url) throws IOException, ClassNotFoundException, SQLException{
    	Document doc = null;
    	String res = Functions.preloading(url);
    	doc = Jsoup.parse(res);
    	//获取文章内容
    	String src = "";
    	String content = "";	
    	Date todaydate= new java.sql.Date(new java.util.Date().getTime());
    	String title = doc.select("h1").text();
    	String author = doc.select("span[class=name]").text();
    	author = author.substring(0,author.indexOf(" "));
    	String source = url;
    	System.out.println("title: "+title); 
    	Elements ps = doc.select(".textblock p");
        for(Element p:ps){
        	String tmpp = "<p>"+p.text()+"</p>";
        	content += tmpp;
        }
    	Elements imgs = doc.select("div[class=mobile_article] img"); 
    	for(Element img : imgs){
    		src = img.attr("src");
    	        break;//只要一张图片
    	}
    	String picName = Functions.getFileName(src);
    	
    	WriteInDB.writeInDB_36kr(title,author,content,picName,todaydate,url);	
    	
        if(Functions.RegexUrl(src, ".*heading")){  //判断是否是文章内的图片（文章内的图片都是以!heading结尾）
		downloadImg(src);	
		}   	
    }


    public static void main(String[] args) throws Exception {
    	KrCrawler crawler = new KrCrawler("crawl", true);
	    crawler.setThreads(50);
	    crawler.setTopN(20);
	    crawler.start(4);
    }

}

