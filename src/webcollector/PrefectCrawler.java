package webcollector;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

public class PrefectCrawler extends BreadthCrawler{

    public PrefectCrawler(String crawlPath, boolean autoParse) {
	    super(crawlPath, autoParse);
	    /*start page*/
	    this.addSeed("http://36kr.com/news");
	    /*fetch url like http://news.hfut.edu.cn/show-xxxxxxhtml*/
	    this.addRegex("http://36kr.com/p/.*html");
	    /*do not fetch jpg|png|gif*/
	    this.addRegex("-.*\\.(jpg|png|gif).*");
	    /*do not fetch url contains #*/
	    this.addRegex("-.*#.*");
    }

	@Override
    public void visit(Page page, CrawlDatums next) {
	    //String url = page.getUrl();
	    /*if page is news page*/
	    System.out.println("Hello"+page.getUrl());
	    ArrayList<String> Urls = patchOfJs(page.getHtml(),"http://36kr.com/p/.*?html");
		Urls.add(page.getUrl());
	    
		for(String url:Urls){
			System.out.println("every url:"+url);
			try {
				JsoupConnector(url);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
    public ArrayList<String> patchOfJs(String html,String regex){
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(html);
		ArrayList<String> URLs = new ArrayList<>();
		while(m.find()){
			URLs.add(m.group());
		}
		System.out.println(URLs.size());
		return URLs;
	}
    
    public void JsoupConnector(String pages) throws ClassNotFoundException, SQLException{
    	/*we use jsoup to parse page*/
        String   res = getHtmlAfterJSAction(pages);
        if (res != null){
            Document doc = Jsoup.parse(res);

            /*extract title and content of news by css selector*/
            String  title = doc.title();
            String content = doc.select(".textblock").text();

           // System.out.println("URL:\n" + url);
          System.out.println("title:\n" + title);
            System.out.println("content:\n" + content);
            System.out.println("-------------------------------------------------");
            //WriteInDB.writeInDB_36kr(title, content);
        }
	}
    
    @SuppressWarnings("resource")
	public String getHtmlAfterJSAction(String url){
    	//使用火狐v45版的内核
    	final WebClient webClient=new WebClient(BrowserVersion.FIREFOX_45);
    	//禁止CSS，看情况打开
    	webClient.getOptions().setCssEnabled(false);
    	//允许执行JS
    	webClient.getOptions().setJavaScriptEnabled(true);
    	//JS容错函数
    	webClient.getOptions().setThrowExceptionOnScriptError(false);
    	HtmlPage page = null;
    	try {
        	page=webClient.getPage(url);
		} catch (Exception e) {
			System.out.println("竟然有Bug,快通知站长修复Bug");
		}
    	if(page != null){
        	//System.out.println(page.asText());
        	//System.out.println(page.asXml());//这是网页的源码
    		return page.asXml();
    	}
    	return null;
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("content:开始");
    	PrefectCrawler crawler = new PrefectCrawler("crawl", true);
	    crawler.setThreads(50);
	    crawler.setTopN(100);
	    crawler.start(4);
    }
}
