package services;

import job.config.JobCommand;
import job.db.model.PageModel;
import job.db.model.job.Job;
import job.db.model.job.JobLog;
import job.db.model.job.query.JobLogQuery;
import job.db.model.job.query.JobQuery;

import java.util.List;

/**
 * Created by alan.zheng on 2017/2/22.
 */
public interface JobServices {
    Job queryById(Long jobId);

    List<Job> queryList();

    PageModel<Job> queryPageList(JobQuery query);

    boolean insertJob(Job job);

    PageModel<JobLog> queryPageJobLog(JobLogQuery query);

    void jobCommand(Long jobId,JobCommand command);

    boolean deleteJob(Long jobId);
}
