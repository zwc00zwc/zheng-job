package services.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import common.mongodb.MongodbManager;
import common.utility.DateUtility;
import common.utility.PropertiesUtility;
import job.config.JobCommand;
import job.db.dal.JobDal;
import job.db.model.PageModel;
import job.db.model.job.Job;
import job.db.model.job.JobLog;
import job.db.model.job.query.JobLogQuery;
import job.db.model.job.query.JobQuery;
import job.log.JobLogManager;
import org.bson.Document;
import org.springframework.stereotype.Service;
import reg.zookeeper.ZookeeperConfig;
import reg.zookeeper.ZookeeperRegistryCenter;
import services.JobServices;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by alan.zheng on 2017/2/22.
 */
@Service
public class JobServicesImpl implements JobServices {
    @Resource
    private JobDal jobDal;

    public boolean deleteJob(Long jobId) {
        return jobDal.deleteJob(jobId);
    }

    public void jobCommand(Long jobId, JobCommand command) {
        Job job= jobDal.queryById(jobId);
        if (job!=null){
            ZookeeperConfig zookeeperConfig=new ZookeeperConfig();
            PropertiesUtility propertiesUtility=new PropertiesUtility("zookeeper.properties");
            zookeeperConfig.setServerLists(propertiesUtility.getProperty("zk.serverList"));
            zookeeperConfig.setNamespace(propertiesUtility.getProperty("zk.namespace"));
            zookeeperConfig.setAuth(propertiesUtility.getProperty("zk.auth"));
            ZookeeperRegistryCenter zookeeperRegistryCenter= null;
            try {
                zookeeperRegistryCenter = new ZookeeperRegistryCenter(zookeeperConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
            zookeeperRegistryCenter.init();
            if (zookeeperRegistryCenter.isExisted("/"+job.getJobName()+"")){
                String zkvalue = zookeeperRegistryCenter.get("/"+job.getJobName()+"");
                zookeeperRegistryCenter.update("/"+job.getJobName()+"",command.getCommand());
                if (JobCommand.EXECUTE.equals(command)){
                    zookeeperRegistryCenter.update("/"+job.getJobName()+"",zkvalue);
                }
                if (JobCommand.SHUTDOWN.equals(command)){
                    zookeeperRegistryCenter.remove("/"+job.getJobName());
                }
            }
            zookeeperRegistryCenter.close();
        }
    }

    public PageModel<JobLog> queryPageJobLog(JobLogQuery query) {
        try {
            return JobLogManager.queryPageJobLog(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertJob(Job job) {
        return jobDal.insertJob(job);
    }

    public PageModel<Job> queryPageList(JobQuery query) {
        PageModel<Job> pageModel=jobDal.queryPageList(query);
        if (pageModel!=null&&pageModel.getModel()!=null&&pageModel.getModel().size()>0){
            ZookeeperConfig zookeeperConfig=new ZookeeperConfig();
            PropertiesUtility propertiesUtility=new PropertiesUtility("zookeeper.properties");
            zookeeperConfig.setServerLists(propertiesUtility.getProperty("zk.serverList"));
            zookeeperConfig.setNamespace(propertiesUtility.getProperty("zk.namespace"));
            zookeeperConfig.setAuth(propertiesUtility.getProperty("zk.auth"));
            ZookeeperRegistryCenter zookeeperRegistryCenter= null;
            try {
                zookeeperRegistryCenter = new ZookeeperRegistryCenter(zookeeperConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
            zookeeperRegistryCenter.init();
            for (int i=0;i<pageModel.getModel().size();i++){
                Job job=(Job) pageModel.getModel().get(i);
                if (zookeeperRegistryCenter.isExisted("/"+job.getJobName()+"")){
                    String zkvalue= zookeeperRegistryCenter.get("/"+job.getJobName()+"");
                    if (JobCommand.PAUSE.getCommand().equals(zkvalue)){
                        job.setStatus(-2);
                    }else {
                        job.setStatus(1);
                    }
                }else {
                    job.setStatus(-1);
                }
            }
            zookeeperRegistryCenter.close();
        }
        return pageModel;
    }

    public List<Job> queryList() {
        return null;
    }

    public Job queryById(Long jobId) {
        Job job=jobDal.queryById(jobId);
        if (job==null){
            return null;
        }
        ZookeeperConfig zookeeperConfig=new ZookeeperConfig();
        PropertiesUtility propertiesUtility=new PropertiesUtility("zookeeper.properties");
        zookeeperConfig.setServerLists(propertiesUtility.getProperty("zk.serverList"));
        zookeeperConfig.setNamespace(propertiesUtility.getProperty("zk.namespace"));
        zookeeperConfig.setAuth(propertiesUtility.getProperty("zk.auth"));
        ZookeeperRegistryCenter zookeeperRegistryCenter= null;
        try {
            zookeeperRegistryCenter = new ZookeeperRegistryCenter(zookeeperConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        zookeeperRegistryCenter.init();
        if (zookeeperRegistryCenter.isExisted("/"+job.getJobName()+"")){
            job.setStatus(1);
        }else {
            job.setStatus(-1);
        }
        zookeeperRegistryCenter.close();
        return job;
    }
}
