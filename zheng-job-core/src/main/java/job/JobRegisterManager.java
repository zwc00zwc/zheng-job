package job;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/19.
 */
public class JobRegisterManager {
    private static JobRegisterManager jobRegisterManager;

    private Map<String, JobScheduleController> jobMap=new HashMap<String, JobScheduleController>();

    public static JobRegisterManager instance(){
        if (jobRegisterManager==null){
            jobRegisterManager=new JobRegisterManager();
        }
        return jobRegisterManager;
    }

    /**
     * 添加作业调度控制器.
     *
     * @param jobName 作业名称
     * @param jobScheduleController 作业调度控制器
     */
    public void addJobScheduleController(final String jobName, final JobScheduleController jobScheduleController) {
        jobMap.put(jobName, jobScheduleController);
    }

    /**
     * 获取作业调度控制器.
     *
     * @param jobName 作业名称
     * @return 作业调度控制器
     */
    public JobScheduleController getJobScheduleController(final String jobName) {
        return jobMap.get(jobName);
    }

    /**
     * 移除作业调度控制器
     * @param jobName
     */
    public void removeJobScheduleController(final String jobName) {
        jobMap.remove(jobName);
    }
}
