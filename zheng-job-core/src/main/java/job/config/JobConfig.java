package job.config;

/**
 * Created by alan.zheng on 2017/1/17.
 */
public class JobConfig {
    public JobConfig(String _jobName,String _javaClass){
        jobName=_jobName;
        javaClass=_javaClass;
    }
    private final String jobName;

    private final String javaClass;

    public String getJobName() {
        return jobName;
    }

    public String getJavaClass() {
        return javaClass;
    }
}
