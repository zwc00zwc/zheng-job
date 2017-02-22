package services.impl;

import job.config.JobCommand;
import job.db.model.PageModel;
import job.db.model.job.Job;
import job.db.model.job.JobLog;
import job.db.model.job.query.JobLogQuery;
import job.db.model.job.query.JobQuery;
import org.springframework.stereotype.Service;
import services.JobServices;

import java.util.List;

/**
 * Created by alan.zheng on 2017/2/22.
 */
@Service
public class JobServicesImpl implements JobServices {
    public boolean deleteJob(Long jobId) {
        return false;
    }

    public void jobCommand(Long jobId, JobCommand command) {

    }

    public PageModel<JobLog> queryPageJobLog(JobLogQuery query) {
        return null;
    }

    public boolean insertJob(Job job) {
        return false;
    }

    public PageModel<Job> queryPageList(JobQuery query) {
        return null;
    }

    public List<Job> queryList() {
        return null;
    }

    public Job queryById(Long jobId) {
        return null;
    }
}
