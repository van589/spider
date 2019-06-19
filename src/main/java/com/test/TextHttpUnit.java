package com.test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.pojo.Movie;
import com.service.MovieService;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * @author 梁家铭
 *
 * https://github.com/van589/Spider
 */
public class TextHttpUnit {

    @Test
    public void mainTest() throws Exception {
        ApplicationContext ctx = null;
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        MovieService movieService = (MovieService) ctx.getBean("MovieService");

        String url = "https://www.dytt8.net/?tdsourcetag=s_pctim_aiomsg";
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //屏蔽日志信息
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        //支持JavaScript
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        //去拿网页
        HtmlPage rootPage = webClient.getPage(url);
        //设置一个运行JavaScript的时间
        String html = rootPage.asXml();
        Document document = Jsoup.parse(html);
        /**
         * 中间部分电影在 class：bd3rl 中，每个电影模块都包含在了 div.co_area2 中。
         * 拿到每个 co_area2 中 tbody 的 tr 再进行遍历
         */
        Elements commentList = document.getElementsByClass("bd3rl").select("div.co_area2");
        Elements tr = commentList.select("tbody").select("tr");
        List<Movie> list = new ArrayList<Movie>();
        /**
         * 排除广告从i从1开始
         * 拿到每个tr标签里所需信息并放入bean里用list保存
         * tr里包括了name、url、type、upLoadDate
         */
        for (int i = 1; i < tr.size(); i++) {
            Movie movie = new Movie();
            Elements td = tr.get(i).getElementsByTag("td");
            //第一个td里面存放了电影类型、名称、url
            Elements a = td.get(0).select("a[href]");
            String type = a.get(0).text();
            String name = a.get(1).text();
            String movieUrl = a.get(1).attr("href");
            //第二个td里面存放了上传日期
            String uploadDate = td.get(1).text();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            movie.setName(name);
            movie.setType(type);
            movie.setUrl(movieUrl);
            movie.setUploadDate(format.parse(uploadDate));
            //生成一个创建时间
            movie.setCreateTime(new Date());
            list.add(movie);
            System.out.println("type = " + type + "\tname = " + name + "\tuploadDate = " + uploadDate + "\tmovieUrl = " + movieUrl);
        }
        movieService.insertMovie(list);
        webClient.close();
    }
}
