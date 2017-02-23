package job.log;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import common.mongodb.MongodbManager;
import common.utility.DateUtility;
import job.db.model.PageModel;
import job.db.model.job.JobLog;
import job.db.model.job.query.JobLogQuery;
import org.bson.Document;

import java.util.*;

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

    public static PageModel<JobLog> queryPageJobLog(JobLogQuery query) {
        String collectionname="";
        if (query.getQueryDate()==null){
            collectionname= DateUtility.getStrFromDate(new Date(),"yyyyMMdd")+"_log";
        }else {
            collectionname= DateUtility.getStrFromDate(query.getQueryDate(),"yyyyMMdd")+"_log";
        }
        MongoCollection collection= MongodbManager.getDatabase("JobLog").getCollection(collectionname);
        List<JobLog> list=new ArrayList<JobLog>();
        BasicDBObject basicDBObject=new BasicDBObject();
        BasicDBObject timebasesic=new BasicDBObject();
        Calendar calendar  =  new GregorianCalendar();
        if (query.getStartTime()!=null){
            calendar.setTime(query.getStartTime());
            calendar.add(Calendar.HOUR,8);//时区关系加8小时
            timebasesic.append("$gte",calendar.getTime());
        }
        if (query.getEndTime()!=null){
            calendar.setTime(query.getEndTime());
            calendar.add(Calendar.HOUR,8);//时区关系加8小时
            timebasesic.append("$lte",calendar.getTime());
        }
        if (timebasesic.size()>0){
            basicDBObject.put("createTime",timebasesic);
        }
        MongoCursor mongoCursor = collection.find(basicDBObject).sort(new BasicDBObject("createTime", -1)).skip(query.getStartRow()).limit(query.getPageSize()).iterator();
        while (mongoCursor.hasNext()){
            Document document=(Document) mongoCursor.next();
            JobLog jobLog= new JobLog();
            jobLog.setJogName(document.get("jobName").toString());
            jobLog.setLog(document.get("log").toString());
            calendar.setTime((Date) document.get("createTime"));
            calendar.add(Calendar.HOUR,-8);//时区关系加8小时
            jobLog.setCreateTime(calendar.getTime());
            list.add(jobLog);
        }
        int i=0;
        MongoCursor mongoCursor1 = collection.find(basicDBObject).sort(new BasicDBObject("createTime", -1)).iterator();
        while (mongoCursor1.hasNext()){
            i++;
            mongoCursor1.next();
        }
        PageModel<JobLog> pageModel=new PageModel<JobLog>(list,query.getCurrPage(),i,query.getPageSize());
        return pageModel;
    }
}
