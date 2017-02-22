package job;

import com.google.common.base.Optional;
import job.base.BaseJob;
import job.config.JobConfig;
import reg.zookeeper.ZookeeperRegistryCenter;

/**
 * Created by alan.zheng on 2017/1/18.
 */
public class SpringJobScheduler extends JobScheduler {
    private final BaseJob baseJob;
    public SpringJobScheduler(JobConfig jobConfig, ZookeeperRegistryCenter zookeeperRegistryCenter, final BaseJob baseJob){
        super(jobConfig,zookeeperRegistryCenter);
        this.baseJob=baseJob;
    }

    @Override
    protected Optional<BaseJob> createBaseJobInstance() {
        return Optional.fromNullable(baseJob);
    }
}
