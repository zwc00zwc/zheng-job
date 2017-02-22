package reg.listener;

import job.log.JobLogManager;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import reg.base.AbstractConnectListener;

import java.util.Date;

/**
 * Created by alan.zheng on 2017/1/23.
 */
public class ConnectListener extends AbstractConnectListener {
    @Override
    public void changed(CuratorFramework curatorFramework, ConnectionState connectionState) {
        JobLogManager.log("System","与zookeeper连接状态发生变化:【"+connectionState+"】",new Date());
    }
}
