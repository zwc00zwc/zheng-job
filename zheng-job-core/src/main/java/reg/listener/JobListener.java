package reg.listener;

import job.JobRegisterManager;
import job.JobScheduleController;
import job.config.JobCommand;
import job.db.dal.JobDal;
import job.db.model.job.Job;
import job.log.JobLogManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import reg.base.AbstractListener;

import java.util.Date;

/**
 * Created by alan.zheng on 2017/1/20.
 */
public class JobListener extends AbstractListener {
    private final String jobName;
    public JobListener(String _jobName){
        jobName=_jobName;
    }
    @Override
    public void changed(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
        String eventstr;
        try {
            eventstr=new String(treeCacheEvent.getData().getData());
        } catch (Exception e) {
            eventstr=null;
        }
        if (StringUtils.isNotEmpty(eventstr)){
            JobScheduleController jobScheduleController= JobRegisterManager.instance().getJobScheduleController(jobName);
            if (jobScheduleController==null){
                JobLogManager.log("System","任务不存在【"+jobName+"】",new Date());
                return;
            }
            if (JobCommand.PAUSE.getCommand().equals(eventstr)){
                jobScheduleController.pauseSingleJob();
            }
            if (JobCommand.RESUME.getCommand().equals(eventstr)){
                jobScheduleController.resumeSingleJob();
            }
            if (JobCommand.EXECUTE.getCommand().equals(eventstr)){
                jobScheduleController.triggerJob();
            }
            if (JobCommand.EDIT.getCommand().equals(eventstr)){
                Job job= JobDal.queryByJobName(jobName);
                if (job!=null && !StringUtils.isEmpty(job.getCorn())){
                    jobScheduleController.rescheduleJob(job.getCorn());
                }
            }
            if (JobCommand.SHUTDOWN.getCommand().equals(eventstr)){
                jobScheduleController.shutdown();
                JobRegisterManager.instance().removeJobScheduleController(jobName);
            }
        }
    }
}
