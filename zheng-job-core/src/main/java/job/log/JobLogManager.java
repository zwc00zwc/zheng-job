package job.log;

import com.mongodb.client.MongoCollection;
import common.mongodb.MongodbManager;
import common.utility.DateUtility;
import org.bson.Document;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 任务日志类
 * Created by alan.zheng on 2017/1/19.
 */
public class JobLogManager {
    public static void log(String jobName,String log,Date logTime){
        try {
            String collectionname= DateUtility.getStrFromDate(new Date(),"yyyyMMdd")+"_log";
            MongoCollection collection= MongodbManager.getDatabase("JobLog").getCollection(collectionname);
            Document document=new Document();
            document.append("jobName",jobName);
            document.append("log",log);
            Calendar calendar   =   new GregorianCalendar();
            calendar.setTime(logTime);
            calendar.add(Calendar.HOUR,8);//时区关系加8小时
            document.append("createTime",calendar.getTime());
            collection.insertOne(document);
        } catch (Exception e) {
        }
    }
}
