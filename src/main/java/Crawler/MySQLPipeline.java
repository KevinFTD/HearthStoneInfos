package Crawler;

import model.Card;
import org.apache.commons.httpclient.util.URIUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kevinftd on 16/3/18.
 */
public class MySQLPipeline implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(MySQLPipeline.class);

    private final String imgDir;

    private static SqlSessionFactory sqlSessionFactory;
    private static CloseableHttpClient picDownloadClient;
    static {
        String resource = "mybatis-config.xml";
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        PoolingHttpClientConnectionManager conMgr = new PoolingHttpClientConnectionManager();
        picDownloadClient = HttpClientBuilder.create().setConnectionManager(conMgr).build();

    }

    public MySQLPipeline(String imgDir){
        this.imgDir = imgDir;

        //目标目录
        File desPathFile = new File(this.imgDir);
        if (!desPathFile.exists()) {
            desPathFile.mkdirs();
        }
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

        Card card = new Card.CardBuilder()
                .chname((String)resultItems.get("ch_name"))
                .enname((String)resultItems.get("en_name"))
                .occupation((String)resultItems.get("occupation"))
                .rarity((String)resultItems.get("rarity"))
                .type((String)resultItems.get("type"))
                .manaCost(Integer.valueOf((String)resultItems.get("mana_cost")))
                .health(Integer.valueOf((String)resultItems.get("health")))
                .attack(Integer.valueOf((String)resultItems.get("attack")))
                .special((String)resultItems.get("special"))
                .desc((String)resultItems.get("desc"))
                .build();

        SqlSession session = sqlSessionFactory.openSession();
        int result = -1;
        try {
            result = session.insert("mybatis.CardMapper.insertCard", card); // 返回值为受影响行数
            session.commit();

            logger.info("insert result: {}", result);
        }
        finally {
            session.close();
        }


        String picUrl = (String)resultItems.get("pic");
        logger.info("pic url: {}", picUrl);

        try{
            downloadPic(picUrl, card.getEnName());
        }
        catch(IOException e){
            e.printStackTrace();
        }


    }

    private void downloadPic(String url, String fileName) throws IOException {
        String[] tokens = url.split("/");
        if(tokens.length < 1) {
            logger.error("Invalid download url: {}", url);
            return;
        }

        // get pic type. png? jpg? or something else
        String lastToken = tokens[tokens.length-1];
        int index = lastToken.lastIndexOf(".");
        String postfix = "";
        if(index > 0) {
            postfix = lastToken.substring(index);
        }

        HttpGet httpget = new HttpGet(URIUtil.encodePath(url));
        HttpResponse response = picDownloadClient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();

        logger.info("writing {} into directory", fileName);
        try {
            FileOutputStream out = new FileOutputStream(new File(this.imgDir+File.separator+fileName+postfix));
            int length = -1;
            byte[] tmp = new byte[1024];
            while ((length = in.read(tmp)) != -1) {
                out.write(tmp, 0, length);
            }
            out.flush();
            out.close();
        } finally {
            in.close();
        }
    }
}
