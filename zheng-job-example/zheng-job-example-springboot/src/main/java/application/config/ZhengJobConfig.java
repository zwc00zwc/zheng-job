package application.config;

import application.jobs.ZhengJob;
import job.JobScheduler;
import job.SpringJobScheduler;
import job.base.BaseJob;
import job.config.JobConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reg.zookeeper.ZookeeperRegistryCenter;

import javax.annotation.Resource;

/**
 * 造轮子的任务配置类，注入spring启动
 * Created by alan.zheng on 2017/1/18.
 */
@Configuration
public class ZhengJobConfig {
    @Bean(name = "zhengJob")
    public ZhengJob zhengJob() {
        return new ZhengJob();
    }

    @Resource
    public ZookeeperRegistryCenter jobZookeeperRegistryCenter;

    @Bean(initMethod = "init",name = "zhengJobSpringJobScheduler")
    public JobScheduler dataflowJobScheduler(final BaseJob zhengJob) {
        JobConfig jobConfig=new JobConfig("zhengJob", zhengJob.getClass().getCanonicalName());
        return new SpringJobScheduler(jobConfig,jobZookeeperRegistryCenter,zhengJob);
    }
}
