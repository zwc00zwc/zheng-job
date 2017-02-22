package job;


import job.base.BaseJob;
import job.config.JobConfig;
import reg.zookeeper.ZookeeperRegistryCenter;

/**
 * Created by alan.zheng on 2017/1/17.
 */
public class SimpleJobExecutor extends JobExecutor {
    private final BaseJob simpleJob;

    public SimpleJobExecutor(JobConfig jobConfig, final BaseJob _simpleJob, ZookeeperRegistryCenter zookeeperRegistryCenter){
        super(jobConfig,zookeeperRegistryCenter);
        simpleJob=_simpleJob;
    }

    @Override
    protected void process() {
        simpleJob.execute();
    }
}
