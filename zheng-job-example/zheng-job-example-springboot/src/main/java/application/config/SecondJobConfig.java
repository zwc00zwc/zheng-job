package application.config;

import application.jobs.SecondJob;
import job.JobScheduler;
import job.SpringJobScheduler;
import job.base.BaseJob;
import job.config.JobConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reg.zookeeper.ZookeeperRegistryCenter;

import javax.annotation.Resource;

/**
 * Created by alan.zheng on 2017/2/9.
 */
@Configuration
public class SecondJobConfig {
    @Bean(name = "secondJob")
    public SecondJob secondJob() {
        return new SecondJob();
    }

    @Resource
    public ZookeeperRegistryCenter jobZookeeperRegistryCenter;

    @Bean(initMethod = "init",name = "SecondJobSpringJobScheduler" )
    public JobScheduler dataflowJobScheduler(final BaseJob secondJob) {
        JobConfig jobConfig=new JobConfig("SecondJob", secondJob.getClass().getCanonicalName());
        return new SpringJobScheduler(jobConfig,jobZookeeperRegistryCenter,secondJob);
    }
}
