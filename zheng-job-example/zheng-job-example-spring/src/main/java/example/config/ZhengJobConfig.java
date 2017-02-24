package example.config;

import example.job.SpringZhengJob;
import job.JobScheduler;
import job.SpringJobScheduler;
import job.base.BaseJob;
import job.config.JobConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reg.zookeeper.ZookeeperRegistryCenter;

import javax.annotation.Resource;

/**
 * Created by alan.zheng on 2017/2/24.
 */
@Configuration
public class ZhengJobConfig {
    @Bean(name = "springZhengJob")
    public SpringZhengJob springZhengJob(){
        return new SpringZhengJob();
    }

    @Resource
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    @Bean(name = "jobConfig")
    public JobConfig jobConfig(final BaseJob springZhengJob) {
        return new JobConfig("springZhengJob", springZhengJob.getClass().getCanonicalName());
    }

    @Bean(initMethod = "init",name = "SecondJobSpringJobScheduler" )
    public JobScheduler dataflowJobScheduler(final BaseJob springZhengJob,final JobConfig jobConfig) {
        return new SpringJobScheduler(jobConfig,zookeeperRegistryCenter,springZhengJob);
    }
}
