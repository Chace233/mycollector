package webcollector;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {
	
	public static void timer() {  
		final long period = 24 * 60 * 60 * 1000 * 7;
        Timer timer = new Timer();  
        timer.schedule(new TimerTask() {  
            public void run() {  
            	XincailiaoCrawler crawler = new XincailiaoCrawler("crawl", true);
            	TechcrunchCrawler crawler1 = new TechcrunchCrawler("crawl",true);
        	    crawler.setThreads(50);
        	    crawler.setTopN(100);
        	    try {
					crawler.start(4);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }  
        }, 0, period);  //第一个时间是函数运行的延迟时间，第二参数是循环启动函数相隔的时间（直接改第二个就好）
    }  
	
	@SuppressWarnings("static-access")
	public static void main(String[] args){
		TimerTest tt = new TimerTest();
		tt.timer();
	}
}
