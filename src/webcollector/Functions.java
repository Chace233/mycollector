package webcollector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Functions {
	
	public static int 		MAX_CLICK_TIMES 	= 32;
	public static boolean 	ENABLE_AJAX_SLEEP 	= false;
	public static int 		AJAX_SLEEP_TIME   	= 10;
	
	public static String preloading(String url){ 
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		HtmlPage page = null ;
		try{
			page = webClient.getPage(url);
		}catch(Exception e){
			System.out.println("js代码有错误！");
		}
		
		return page.asXml();
	}
	//下载图片
	public static void downloadImg(String src) throws IOException{ 
    	String fileName = getFileName(src);
    	URL url = new URL(src);
    	URLConnection uc = url.openConnection();
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
	
	//判断是否与正则表达式相符
    public static boolean RegexUrl(String url, String regex){  
    	
    	Pattern pattern = Pattern.compile(regex);
    	Matcher m = pattern.matcher(url);
    	if(m.find()) return true;
    	return false;
    }
    
  //得到文件名 （如果想改直接改这里）
   public static String getFileName(String src){  
    	String fileName = "";
    	int a = 0,b = src.length();
    	for(int i = src.length()-1; i >=0 ;i --){
    		if(src.charAt(i) == '!') b = i;
    		if(src.charAt(i) == '/'){
    			a = i;
    			break;
    		}
    	}
    	fileName = src.substring(a+1, b);
    	return fileName;
    }
    
    //文件写入
    public static void WriteFile(String url) throws IOException{ 
    	File file = new File("D://content4.txt");
    	FileWriter fw = new FileWriter(file,true);
    	BufferedWriter bw = new BufferedWriter(fw);
    	bw.write(url+"\n");
    	bw.close();
    }
    
    public static JSONObject getJsonPart(String html ) {
    	String res = "";
    	String regex1 = "<script>var props=";
    	String regex2 = "}}}</script";
    	Pattern pattern = Pattern.compile(regex1);
    	Matcher matcher = pattern.matcher(html);
    	int i = 0;
    	if(matcher.find()){
    		 i = matcher.end();
    		System.out.println(i);
    		System.out.println("####"+html.charAt(i));
    	}
    	
    	pattern = Pattern.compile(regex2);
    	matcher = pattern.matcher(html);
    	int j = 0;
    	if(matcher.find()){
    		 j = matcher.start()+3;
    		System.out.println(j);
    		System.out.println("####"+html.charAt(j));
    	}
    	res=html.substring(i, j);
    	//System.out.println("ssss:  "+res.charAt(16876));
  
    	JSONObject json = new JSONObject(res);
    	return json;    	
    }
    
    public static ArrayList<String> getUrls(String html, String regex){
    		Pattern p = Pattern.compile(regex);
    		Matcher m = p.matcher(html);
    		ArrayList<String> URLs = new ArrayList<>();
    		while(m.find()){
    			URLs.add(m.group());
    		}
    		System.out.println(URLs.size());
    		return URLs;
    }
    
    public static String getYear(){
    	int y;
    	Calendar c = Calendar.getInstance();
    	y = c.get(Calendar.YEAR);
    	String year = y+"";
    	return year;
    }


	public static String[] getDate_xincailiao(){
    	int y,m,d;
    	Calendar c = Calendar.getInstance();
    	m = c.get(Calendar.MONTH)+1;
    	d = c.get(Calendar.DATE);
    	String[] date = new String[7];
    	for(int i = 0; i < 7; i++){
    		if(d-i > 0){
    			date[i] = String.format("%02d", m)+String.format("%02d", d-i);
    		}
    		else if(d-i <= 0){
    			if((m-1==1)||(m-1==3)||(m-1)==5||(m-1==7)||(m-1==8)||(m-1==10)||(m-1==12)){
    				date[i] = String.format("%02d", m-1)+String.format("%02d", 31+d-i);
    			}
    			else if((((m-1)%4==0)||((m-1)%400 == 0))&&(m-1)==2){
    				date[i] = String.format("%02d",m-1) + String.format("%02d", 29+d-i);
    			}
    			else if((m-1==4)||(m-1==6)||(m-1==9)||(m-1==11)){
    				date[i] = String.format("%02d", m-1)+String.format("%2d", 30+d-i);
    			}
    			else {
    				date[i] = String.format("%02d",m-1)+String.format("%2d",28+d-i);
    			}
    		}
    	}
    	return date;
    }
	
	public static String[][] getDate_techcrunch(){
    	int y,m,d;
    	Calendar c = Calendar.getInstance();
    	m = c.get(Calendar.MONTH)+1;
    	d = c.get(Calendar.DATE);
    	String[][] date = new String[7][2];
    	for(int i = 0; i < 7; i++){
    		if(d-i > 0){
    			date[i][0] = String.format("%02d", m);
    			date[i][1] = String.format("%02d", d-i);
    		}
    		else if(d-i <= 0){
    			if((m-1==1)||(m-1==3)||(m-1)==5||(m-1==7)||(m-1==8)||(m-1==10)||(m-1==12)){
    				date[i][0] = String.format("%02d", m-1);
    				date[i][1] = String.format("%02d", 31+d-i);
    			}
    			else if((((m-1)%4==0)||((m-1)%400 == 0))&&(m-1)==2){
    				date[i][0] = String.format("%02d",m-1);
    				date[i][1] = String.format("%02d", 29+d-i);
    			}
    			else if((m-1==4)||(m-1==6)||(m-1==9)||(m-1==11)){
    				date[i][0] = String.format("%02d", m-1);
    				date[i][1] = String.format("%2d", 30+d-i);
    			}
    			else {
    				date[i][0] = String.format("%02d",m-1);
    				date[i][1] = String.format("%2d",28+d-i);
    			}
    		}
    	}
    	return date;
    }
	
	
	//获取adafruit网站SHOP目录下分类的网址
	public static ArrayList<String> getCategories(String url) throws IOException{
		System.out.println("获取第一级url。。。。。");
		ArrayList<String> res = new ArrayList<String>();
		Document doc = Jsoup.connect(url).timeout(60000).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
		Elements eles = doc.select("div[class=col-lg-11 col-md-11 col-sm-11 col-xs-11] h3");
		for(Element ele : eles){
			String name = ele.text();
			//System.out.println("###name+ "+name);
			String src = "https://www.adafruit.com/"+ele.select("a").attr("href");
			//System.out.println("----src: "+src);
			res.add(src);
		}
		return res;
	}
	
	
	/**
	 * 对于36Kr页面少的补丁函数
	 * @param url 当前的Url
	 * @param parent 目前的节点
	 * @param times 点击加载更多的次数
	 */
	public static void patcOf36Kr(String url,ArrayList<String> parent,int times){
    	if (Functions.is36Kr(url)){
			String lastPageUrl = parent.get(parent.size()-1);
			String lastPageId  = lastPageUrl.substring(
					lastPageUrl.lastIndexOf("/")+1, 
					lastPageUrl.lastIndexOf("."));
			
			try {
				ArrayList<String> l = loadMoreIn36Kr(Integer.parseInt(lastPageId), times);
				for(String u : l){
					parent.add("http://36kr.com/p/"+u+".html");
		    	}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
    	}
    }
	
	public static ArrayList<String> loadMoreIn36Kr(int nowid,int times) throws Exception{
    	if (times > Functions.MAX_CLICK_TIMES){
    		throw new Exception("it would be easy to be forbidden,if times was great!");
    	}
    	if (times <= 0){
    		return null;
    	}
    	
    	String aimUrl = "http://36kr.com/api/info-flow/main_site/posts?b_id="+
    					nowid+"&per_page=20";
    	if (ENABLE_AJAX_SLEEP){
    		Thread.sleep(AJAX_SLEEP_TIME);
    	}
    	String res    = sendGet(aimUrl);
    	
    	ArrayList<String> retList   = new ArrayList<>();
    	
    	retList = Functions.get36KrClickedUrls(res);
    	
    	ArrayList<String> childList = loadMoreIn36Kr(
			    			Integer.parseInt( retList.get(retList.size()-1) ), 
			    			times - 1);
    	
    	if (childList != null)
    		for(String l:childList){
    			retList.add(l);
    		}
    	
    	return retList;
    }
	
	public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
	
	public static ArrayList<String> get36KrClickedUrls(String json){
		ArrayList<String> list = new ArrayList<>();
		Pattern p = Pattern.compile("\"id\":[\\d\\D]{2,10},\"column");
		Matcher m = p.matcher(json);
		while( m.find() ){
			String text = m.group();
			text = text.substring(5, text.lastIndexOf(","));
			list.add(text);
		}
		return list;
	}
	
	public static 	ArrayList<String> get36KrClickedUrls2(String res){
		System.out.println(res.length());
		
		//res = res.substring(27);
		ArrayList<String> list = new ArrayList<>();
		JSONObject json = new JSONObject(res);
		//JSONObject data = json.getJSONObject("data");
		//JSONObject items = json.getJSONObject("items");
		System.out.println(json);
		return list;
	}
	
	
    public static boolean isInOneWeek(Document doc) throws ParseException{
    	String time = doc.select("abbr[class=time]").text();
    	time = time.substring(0, 16);
    	System.out.println("%%%time "+time);
		long day=0;
	    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");    
	    java.util.Date beginDate;
	    java.util.Date utildate=new java.util.Date();
	    java.sql.Date  endDate =new java.sql.Date(utildate.getTime());;
	    beginDate = format.parse(time);  
	    day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);    
	    System.out.println("相隔的天数="+day);
	    if(day < 8)return true;
    	return false;
    }
    
	
	public static boolean is36Kr(String url){
		return url.matches("http://36kr.com.*");
	}
    
    public static void main(String[] args) throws IOException{
    	String[][] data = Functions.getDate_techcrunch();
    	for(int i = 0; i < data.length; i++){
    		System.out.print(data[i][0]+"/"+data[i][1]+" ");
    	}
    }
}
