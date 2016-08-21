package webcollector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.sql.Date;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

public class XincailiaoCrawler extends BreadthCrawler {
    public XincailiaoCrawler(String crawlPath, boolean autoParse) {
	    super(crawlPath, autoParse);	    
    }
    @SuppressWarnings("deprecation")
	@Override
    public void visit(Page page, CrawlDatums next){
	    if (page.matchUrl("http://www.xincailiao.com/html/weizixun/xinxinggongnencailiao/.*html")||page.matchUrl("http://www.xincailiao.com/html/weizixun/xianjinjiegoucailiao/.*html")) {
	        Document doc = page.getDoc();
            String absUrl="";
            String content = "";
            String tmpAbsUrl = "";
	        String tmp = page.select("div[class=list1_detail]").text();
	        String author= tmp.substring(tmp.indexOf("来源")+3,tmp.indexOf("|")-1);
	        String date = tmp.substring(tmp.indexOf("发表")+5,tmp.indexOf("点击")-2);
	        String summary = page.select("div[class=list1_detail1]").text();
	        String title = page.select("div[class=list1_detail] strong").text();
	        //String content = page.select("div[class=list1_detail2] p").text()+"\n";
	        String url = page.getUrl();
	        Elements ps = doc.select("div[class=list1_detail2] p");
	        for(Element p:ps){
	        	String tmpp = "<p>"+p.text()+"</p>";
	        	content += tmpp;
	        }
	        Elements elements = doc.select("div[class=list1_detail2] img");
	       for(Element element: elements){
	        	 tmpAbsUrl = element.attr("abs:src");
	        	 absUrl = StringUtils.substringAfterLast(tmpAbsUrl, "/");
	        	 break;
	       }
	       Date todaydate= new java.sql.Date(new java.util.Date().getTime());
	        try {
				WriteInDB.writeInDB_xincailiao(title,author,summary,date,todaydate,content,url,absUrl);
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        	 if(Functions.RegexUrl(tmpAbsUrl,"/uploads/.*")){
	        		 try{
	        			 downloadImg(tmpAbsUrl);
	        		 }catch (IOException e) {
	 					e.printStackTrace();
	 				}	        		 
	        	 }
	        }		
	    }
	public static void downloadImg(String src) throws IOException{ 
    	String fileName = Functions.getFileName(src);
    	URL url = new URL(src);
    	URLConnection uc = url.openConnection();
    	InputStream in = uc.getInputStream();
    	File file = new File("download/xincailiao/"+fileName);
    	@SuppressWarnings("resource")
		FileOutputStream out = new FileOutputStream(file);
    	int i = 0;
    	while ((i = in.read()) != -1){
    		out.write(i);
    	}
    	in.close();
    }
	public static void main(String[] args) throws Exception{
    	XincailiaoCrawler crawler = new XincailiaoCrawler("crawl", true);
    	String[] data = Functions.getDate_xincailiao();
    	crawler.addSeed("http://www.xincailiao.com/html/weizixun/");//"/"+data+"/
    	String regex = "http://www.xincailiao.com/html/weizixun/[a-z]+/"+Functions.getYear()+"/("
    	                                  +data[0]+
    			                      "|"+data[1]+
    			                      "|"+data[2]+
    			                      "|"+data[3]+
    			                      "|"+data[4]+
    			                      "|"+data[5]+
    			                      "|"+data[6]+
    			                      ").*html";
    	//System.out.println("###regext:"+regex);
    	crawler.addRegex(regex);
		crawler.addRegex("-.*\\.(jpg|png|gif).*");
		crawler.addRegex("-.*#.*");
	    crawler.setThreads(50);
	    crawler.setTopN(100);
	    crawler.start(4);
    }
}

