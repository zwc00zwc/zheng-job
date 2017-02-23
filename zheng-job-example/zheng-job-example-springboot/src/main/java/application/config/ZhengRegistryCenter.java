package application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reg.zookeeper.ZookeeperConfig;
import reg.zookeeper.ZookeeperRegistryCenter;

/**
 * Created by alan.zheng on 2017/1/19.
 */
@Configuration
public class ZhengRegistryCenter {
    @Bean(initMethod = "init",name = "jobZookeeperRegistryCenter")
    public ZookeeperRegistryCenter regCenter(@Value("${zhengregCenter.serverList}") final String serverList, @Value("${zhengregCenter.namespace}") final String namespace, @Value("${zhengregCenter.auth}") final String auth) {
        ZookeeperConfig zookeeperConfig=new ZookeeperConfig();
        zookeeperConfig.setServerLists(serverList);
        zookeeperConfig.setNamespace(namespace);
        zookeeperConfig.setAuth(auth);
        return new ZookeeperRegistryCenter(zookeeperConfig);
    }
}
