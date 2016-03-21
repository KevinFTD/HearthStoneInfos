package Crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;


/**
 * Created by kevinftd on 16/3/16.
 */
public class HearthStoneCrawler implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    private final String LIST_PAGE_URL = "(http://db\\.duowan\\.com/lushi/card/list/.+html)";
    private final String DETAILS_PAGE_URL = "(http://db\\.duowan\\.com/lushi/card/detail/.+.html)";
    private final String[] ATTRS = {"ch_name", "en_name", "occupation", "rarity", "type",
            "mana_cost", "health", "attack", "special", "desc"};

    @Override
    public void process(Page page) {
        if(page.getUrl().regex(DETAILS_PAGE_URL).match()) {

            page.putField("pic", page.getHtml().css("table.table-s3.full tr:eq(0) td:eq(0)")
                    .xpath("//img[@class='img-rounded']/@src").toString());
            page.putField("ch_name", page.getHtml().css("table.table-s3.full tr:eq(0) td:eq(2)").xpath("/td/text()").toString());
            for(int i = 1; i < ATTRS.length; i++) {
                page.putField(ATTRS[i], page.getHtml().css("table.table-s3.full tr:eq(" + i + ") td:eq(1)")
                        .xpath("/td/text()").toString());
            }
        }
        else{
            page.addTargetRequests(page.getHtml().links().regex(LIST_PAGE_URL).all());
            page.addTargetRequests(page.getHtml().links().regex(DETAILS_PAGE_URL).all());
            page.setSkip(true);
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws Exception {


        Spider spider = Spider.create(new HearthStoneCrawler())
                .addUrl("http://db.duowan.com/lushi")
                .addPipeline(new MySQLPipeline("./img"))
                .thread(5);
        //SpiderMonitor.instance().register(spider);
        spider.run();
    }
}