package job.db.model.job;
import job.db.model.BaseModel;

import java.util.Date;

/**
 * Created by Administrator on 2017/1/18.
 */
public class JobLog extends BaseModel {
    /**
     * 任务名称
     */
    private String jogName;
    /**
     * 日志信息
     */
    private String log;
    /**
     * 日志创建时间
     */
    private Date createTime;

    public String getJogName() {
        return jogName;
    }

    public void setJogName(String jogName) {
        this.jogName = jogName;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
