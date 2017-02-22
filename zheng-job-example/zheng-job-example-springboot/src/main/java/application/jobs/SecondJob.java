package application.jobs;

import common.utility.DateUtility;
import job.base.BaseJob;

import java.util.Date;

/**
 * Created by alan.zheng on 2017/2/9.
 */
public class SecondJob implements BaseJob {
    public void execute() {
        System.out.print("我在测试SecondJob"+ DateUtility.getStrFromDate(new Date(),""));
    }
}
