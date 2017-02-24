package example.config;

import common.utility.PropertiesUtility;
import reg.zookeeper.ZookeeperConfig;
import reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Created by alan.zheng on 2017/1/19.
 */
@Configuration
public class ZhengRegistryCenter {
    @Resource
    private ZookeeperConfig zookeeperConfig;
    @Bean(initMethod = "init",name = "jobZookeeperRegistryCenter")
    public ZookeeperRegistryCenter regCenter() {
        return new ZookeeperRegistryCenter(zookeeperConfig);
    }
}
