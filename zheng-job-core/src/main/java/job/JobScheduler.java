package job;

import com.google.common.base.Optional;
import job.base.BaseJob;
import job.config.JobConfig;
import job.db.dal.JobDal;
import job.log.JobLogManager;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.Job;
import org.quartz.impl.StdSchedulerFactory;
import reg.zookeeper.ZookeeperRegistryCenter;

import java.util.Date;

/**
 * 作业任务调度器
 * Created by alan.zheng on 2017/1/16.
 */
public class JobScheduler {
    private final JobConfig jobConfig;
    private final ZookeeperRegistryCenter zookeeperRegistryCenter;
    public JobScheduler(final JobConfig _jobConfig, ZookeeperRegistryCenter _zookeeperRegistryCenter){
        jobConfig=_jobConfig;
        zookeeperRegistryCenter=_zookeeperRegistryCenter;
    }
    /**
     * 初始化作业.
     */
    public void init() {
        try {
            JobDetail jobDetail = createJobDetail(jobConfig.getJavaClass());
            Scheduler scheduler=null;
            try {
                StdSchedulerFactory factory = new StdSchedulerFactory();
                factory.initialize();
                scheduler = factory.getScheduler();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            JobScheduleController jobScheduleController=new JobScheduleController(scheduler,jobDetail,jobConfig.getJobName());
            job.db.model.Job job= JobDal.queryByJobName(jobConfig.getJobName());
            if (job!=null && !StringUtils.isEmpty(job.getCorn())){
                jobScheduleController.scheduleJob(job.getCorn());
                JobRegisterManager.instance().addJobScheduleController(jobConfig.getJobName(),jobScheduleController);
            }
        } catch (Exception e) {
            JobLogManager.log(jobConfig.getJobName(),e.toString(),new Date());
        }
    }

    private JobDetail createJobDetail(final String javaClass){
        JobDetail jobDetail = JobBuilder.newJob(AbstractJob.class).withIdentity(jobConfig.getJobName()).build();
        jobDetail.getJobDataMap().put("jobConfig", jobConfig);
        jobDetail.getJobDataMap().put("zookeeperRegistryCenter", zookeeperRegistryCenter);
        Optional<BaseJob> baseJobInstance = createBaseJobInstance();
        if (baseJobInstance.isPresent()) {
            jobDetail.getJobDataMap().put("baseJob", baseJobInstance.get());
        }else {
            try {
                jobDetail.getJobDataMap().put("baseJob",Class.forName(javaClass).newInstance());
            } catch (InstantiationException e) {
                JobLogManager.log(jobConfig.getJobName(),e.toString(),new Date());
            } catch (IllegalAccessException e) {
                JobLogManager.log(jobConfig.getJobName(),e.toString(),new Date());
            } catch (ClassNotFoundException e) {
                JobLogManager.log(jobConfig.getJobName(),e.toString(),new Date());
            }
        }
        return jobDetail;
    }

    protected Optional<BaseJob> createBaseJobInstance() {
        return Optional.absent();
    }

    /**
     * 调度作业
     */
    public static final class AbstractJob implements Job{
        private JobConfig jobConfig;
        private BaseJob baseJob;
        private ZookeeperRegistryCenter zookeeperRegistryCenter;
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            try {
                if (baseJob instanceof BaseJob){
                    new SimpleJobExecutor(jobConfig,(BaseJob) baseJob,zookeeperRegistryCenter).excute();
                }
            } catch (Exception e) {
                JobLogManager.log(jobConfig.getJobName(),e.toString(),new Date());
            }
        }

        public void setBaseJob(BaseJob baseJob) {
            this.baseJob = baseJob;
        }

        public void setJobConfig(JobConfig jobConfig) {
            this.jobConfig = jobConfig;
        }

        public void setZookeeperRegistryCenter(ZookeeperRegistryCenter zookeeperRegistryCenter) {
            this.zookeeperRegistryCenter = zookeeperRegistryCenter;
        }
    }
}
