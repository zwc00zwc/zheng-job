package reg.listener;

import job.config.JobConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import reg.base.AbstractListener;
import reg.zookeeper.ZookeeperRegistryCenter;

/**
 * Created by alan.zheng on 2017/1/20.
 */
public class ListenerManager {
    private final JobConfig jobConfig;
    private final ZookeeperRegistryCenter zookeeperRegistryCenter;
    public ListenerManager(ZookeeperRegistryCenter _zookeeperRegistryCenter,JobConfig _jobConfig){
        zookeeperRegistryCenter=_zookeeperRegistryCenter;
        jobConfig=_jobConfig;
    }

    public void addListener(final AbstractListener abstractListener){
        CuratorFramework curatorFramework=(CuratorFramework) zookeeperRegistryCenter.getRawClient();
        TreeCache treeCache=new TreeCache(curatorFramework,"/"+jobConfig.getJobName()+"");
        treeCache.getListenable().addListener(abstractListener);
    }
}
