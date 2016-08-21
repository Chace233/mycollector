package webcollector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

public class AdafruitCrawler extends BreadthCrawler{

	public AdafruitCrawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		
	}

	@Override
	public void visit(Page page, CrawlDatums next) {
		ArrayList<String> list = getUrl(page);
		for(int i = 0; i < list.size(); i++){
			try {
				crawlerOnePage(list.get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<String> getUrl(Page page){
		ArrayList<String> list = new ArrayList<String>();
		Document doc = page.getDoc();
		Elements as = doc.select("div[id=productListing] a");
		for(Element a : as){
			String c = a.attr("href");
			if(isUrl(c, "/products/[0-9]+")){
				String href = "https://www.adafruit.com"+c;
				if(!list.contains(href))
					list.add(href);
			}
		}
		return list;
	}
	
	public boolean isUrl(String href, String regex){
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(href);
		if(m.find()){
			return true;
		}
		return false;
	}
	
	public void crawlerOnePage(String url) throws IOException, ClassNotFoundException, SQLException{
		Document doc = Jsoup.connect(url).timeout(100000).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
		String src = getImgSrc(doc);
		downloadImg(src);
		String source = url;
		String picurl = getFileName(src);
		String category = doc.select("div[class=container] a").get(0).text();
		String tag = doc.select("div[class=container] a").get(1).text();
		String name = doc.select("div[id=prod-right-side] h1").text();
		String price = doc.select("div[id=prod-price]").text();
		price = price.substring(0, price.indexOf(" "));
		String description = doc.select("div[id=description]").text();
		//System.out.println("$$$"+description);
		String technical_details = doc.select("div[id=technical-details]").text();
		WriteInDB.writeInDB_adafruit(name,price,category,tag,picurl,technical_details,source);
		//System.out.println("##tag: "+tag);
	}
	
	public String getImgSrc(Document doc){
		Elements eles = doc.select("div[id=prod-primary-img-container]");
		String src = "";
		for(Element ele : eles){
			Elements as = ele.select("a");
			for(Element a : as){
				src = a.select("img").attr("src");
				break;
			}
		}
		return src;
	}
	
	public void downloadImg(String src) throws IOException{
		String fileName = getFileName(src);
		URL url = new URL(src);
    	URLConnection uc = url.openConnection();
    	uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)"); 
    	InputStream in = uc.getInputStream();
    	File file = new File("download/"+fileName);
    	@SuppressWarnings("resource")
		FileOutputStream out = new FileOutputStream(file);
    	int i = 0;
    	while ((i = in.read()) != -1){
    		out.write(i);
    	}
    	in.close();
	}
	
	public String getFileName(String url){
		int i = url.length()-1;
		for(;i >=0 ;i--){
			if(url.charAt(i) == '/'){
				break;
			}
		}
		String fileName = url.substring(i+1);
		return fileName;
	}
	

	
	public static void main(String[] args) throws Exception{
		AdafruitCrawler crawler = new AdafruitCrawler("crawler", true);
		ArrayList<String> list = Functions.getCategories("https://www.adafruit.com/categories");
		for(String li : list){
			crawler.addSeed(li);
			crawler.addRegex("https://www.adafruit.com/products/[0-9]+");
			crawler.setThreads(50);
		    crawler.setTopN(100);
		    crawler.start(5);
		}
	}
	
}
