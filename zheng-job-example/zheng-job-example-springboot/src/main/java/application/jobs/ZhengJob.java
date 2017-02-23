package application.jobs;

import common.utility.DateUtility;
import job.base.BaseJob;
import java.util.Date;

/**
 * Created by alan.zheng on 2017/1/18.
 */
public class ZhengJob implements BaseJob {
    public void execute() {
        System.out.print("我在测试ZhengJob"+DateUtility.getStrFromDate(new Date(),""));
    }
}
