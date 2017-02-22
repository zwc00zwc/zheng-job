package job.db.dal;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import common.mongodb.MongodbManager;
import common.utility.DateUtility;
import job.db.BaseDB;
import job.db.model.PageModel;
import job.db.model.job.Job;
import job.db.model.job.JobLog;
import job.db.model.job.query.JobLogQuery;
import job.db.model.job.query.JobQuery;
import org.bson.Document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by alan.zheng on 2017/2/9.
 */
public class JobDal {
    public static Job queryByJobName(String jobName){
        Job job=new Job();
        String[] args=new String[]{jobName};
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection= BaseDB.getConnection();
            preparedStatement=connection.prepareStatement("SELECT id,jobName,corn,remark,createTime FROM tb_job WHERE jobName=?");
            BaseDB.query(preparedStatement,args);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                job.setId(resultSet.getLong("id"));
                job.setJobName(resultSet.getString("jobName"));
                job.setCorn(resultSet.getString("corn"));
                job.setCreateTime(resultSet.getDate("createTime"));
                job.setRemark(resultSet.getString("remark"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            BaseDB.dispose(connection,preparedStatement,resultSet);
        }
        return job;
    }

    public Job queryById(Long jobId){
        Job job=new Job();
        String[] args=new String[]{jobId.toString()};
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection= BaseDB.getConnection();
            preparedStatement=connection.prepareStatement("SELECT id,jobName,corn,remark,createTime FROM tb_job WHERE id=?");
            BaseDB.query(preparedStatement,args);
            resultSet=preparedStatement.executeQuery();
            List<Job> list= resultToJob(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            BaseDB.dispose(connection,preparedStatement,resultSet);
        }
        return job;
    }

    public List<Job> queryList(){
        List<Job> list=null;
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection= BaseDB.getConnection();
            preparedStatement=connection.prepareStatement("SELECT id,jobName,corn,remark,createTime FROM tb_job");
            resultSet=preparedStatement.executeQuery();
            list= resultToJob(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            BaseDB.dispose(connection,preparedStatement,resultSet);
        }
        return list;
    }

    public PageModel<Job> queryPageList(JobQuery query){
        List<Job> list= new ArrayList<Job>();
        int count=0;

        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection= BaseDB.getConnection();
            preparedStatement=connection.prepareStatement("SELECT COUNT(*) FROM tb_job");
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                count=resultSet.getInt(1);
            }
            if (count>0){
                preparedStatement=connection.prepareStatement("SELECT id,jobName,corn,remark,createTime FROM tb_job limit ?,?");
                int i=1;
                int m=2;
                preparedStatement.setInt(i,query.getStartRow());
                preparedStatement.setInt(m,query.getPageSize());
                resultSet=preparedStatement.executeQuery();
                list=resultToJob(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            BaseDB.dispose(connection,preparedStatement,resultSet);
        }

        PageModel<Job> pageModel=new PageModel<Job>(list,query.getCurrPage(),count,query.getPageSize());
        return pageModel;
    }

    public boolean insertJob(Job job){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        connection=BaseDB.getConnection();
        try {
            preparedStatement=connection.prepareStatement("INSERT INTO tb_job(jobName,corn,remark,createTime) VALUES (?,?,?,?)");
            preparedStatement.setString(1,job.getJobName());
            preparedStatement.setString(2,job.getCorn());
            preparedStatement.setString(3,job.getRemark());
            preparedStatement.setString(4, DateUtility.getStrFromDate(new Date(),""));
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDB.dispose(connection,preparedStatement,null);
        }
        return false;
    }

    public PageModel<JobLog> queryJobLogList(JobLogQuery query){
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

    public boolean deleteJob(Long jobId){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        connection=BaseDB.getConnection();
        try {
            preparedStatement=connection.prepareStatement("DELETE FROM tb_job WHERE id=?");
            preparedStatement.setLong(1,jobId);
            if (preparedStatement.executeUpdate()>0){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDB.dispose(connection,preparedStatement,null);
        }
        return false;
    }

    private List<Job> resultToJob(ResultSet resultSet){
        List<Job> list=new ArrayList<Job>();
        Job job=null;
        try {
            while (resultSet.next()){
                job=new Job();
                job.setId(resultSet.getLong("id"));
                job.setJobName(resultSet.getString("jobName"));
                job.setCorn(resultSet.getString("corn"));
                job.setCreateTime(resultSet.getDate("createTime"));
                job.setRemark(resultSet.getString("remark"));
                list.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
