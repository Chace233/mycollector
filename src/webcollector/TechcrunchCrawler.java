package webcollector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

public class TechcrunchCrawler extends BreadthCrawler{

	public TechcrunchCrawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
	}
	@Override
	public void visit(Page page, CrawlDatums next) {
		String url = page.getUrl();
		if(page.matchUrl("http://techcrunch.cn/[0-9]+/[0-9]+/[0-9]+/.*")){
			System.out.println("##URL:"+ url);
			Document doc = page.getDoc();
			String content ="";
			String picurl = "";
			Date todaydate= new java.sql.Date(new java.util.Date().getTime());
			String title = page.select("h1[class=alpha tweet-title]").text();
			String postdate = page.select("div[class=byline] time").text();
			postdate = postdate.substring(0,postdate.indexOf(" "));
			System.out.println(postdate);
			String author = getAuthor(page.select("div[class=byline] a").text());
			Elements texts = page.select("div[class=article-entry text]");
	        Elements ps = doc.select("div[class=article-entry text] p");
	        for(Element p:ps){
	        	String tmpp = "<p>"+p.text()+"<p>";
	        	content += tmpp;
	        }
			//ArrayList<String> contents = new ArrayList<String>(); 
	       // WriteInDB.writeInDB_techcrunch(title, author, content, picUrl_s, postDate_s, writeDate_s, source_s);
			for(Element text : texts){
				//下载图片
				Elements imgs = text.select("img");
		    	for(Element img : imgs){
		    		String src = img.attr("abs:src");
                    picurl = getFileName(src);
		    		if(Functions.RegexUrl(src, "http://files.techcrunch.cn/.*w=738")){
		    			String url1 = src;
		    			try {
							downloadImg(url1);
						} catch (IOException e) {
							e.printStackTrace();
						}
		    		}
		    		break;
		    	}
		    	break;
			}
			try {
				WriteInDB.writeInDB_techcrunch(title, author, content, picurl, postdate, todaydate, url);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

		
	}
	
	public String getAuthor(String auther){
		String res = auther;
		if(auther.indexOf('@') > 0){
			res = auther.substring(0,auther.indexOf('@'));
		}
		return res;
	}
	
	//下载图片
	public static void downloadImg(String src) throws IOException{ 
    	String fileName = getFileName(src);
    	URL url = new URL(src);
    	URLConnection uc = url.openConnection();
    	uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)"); 
    	InputStream in = uc.getInputStream();
    	File file = new File("download/techcrunch/"+fileName);
    	FileOutputStream out = new FileOutputStream(file);
    	int i = 0;
    	while ((i = in.read()) != -1){
    		out.write(i);
    	}
    	in.close();
    }
	//得到文件名 （如果想改直接改这里）
   private static String getFileName(String src){  
    	String fileName = "";
    	int a = 0,b = src.length();
    	for(int i = src.length()-1; i >=0 ;i --){
    		if(src.charAt(i) == '?') b = i;
    		if(src.charAt(i) == '/'){
    			a = i;
    			break;
    		}
    	}
    	fileName = src.substring(a+1, b);
    	//System.out.println("####FileName: "+fileName);
    	return fileName;
    }
	public static void main(String[] args) throws Exception{
		TechcrunchCrawler crawler = new TechcrunchCrawler("crawl",true);
		
		String[][] data = Functions.getDate_techcrunch();
		for(int i = 1; i <= 5; i++){
			if(1 == i){
				crawler.addSeed("http://techcrunch.cn/");
			}
			else{
				crawler.addSeed("http://techcrunch.cn/page/"+i);
			}
			i++;
		}	
		
		String regex = "http://techcrunch.cn/"+Functions.getYear()+"/("+data[0][0]+"|"+
			    data[1][0]+"|"+
			    data[2][0]+"|"+
			    data[3][0]+"|"+
			    data[4][0]+"|"+
			    data[5][0]+"|"+
			    data[6][0]+")/("+
			    data[0][1]+"|"+
			    data[1][1]+"|"+
			    data[2][1]+"|"+
			    data[3][1]+"|"+
			    data[4][1]+"|"+
			    data[5][1]+"|"+
			    data[6][1]+")/.*";
		crawler.addRegex(regex);
		crawler.addRegex("-.*\\.(jpg|png|gif).*");
		crawler.addRegex("-.*#.*");
		
		crawler.setThreads(50);
		crawler.setTopN(100);
		crawler.start(5);
	}

}
