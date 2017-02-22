package job.db.model;

import java.util.Date;

/**
 * Created by alan.zheng on 2017/2/9.
 */
public class Job {
    /**
     * id
     */
    private Long id;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * quartz表达式
     */
    private String corn;
    /**
     * 任务运行状态 (1：正在运行 -1：未运行 -2：已暂停 )
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 备注
     */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCorn() {
        return corn;
    }

    public void setCorn(String corn) {
        this.corn = corn;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
