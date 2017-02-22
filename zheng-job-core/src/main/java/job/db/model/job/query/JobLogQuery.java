package job.db.model.job.query;

import job.db.model.QueryPageModel;

import java.util.Date;

/**
 * Created by Administrator on 2017/1/18.
 */
public class JobLogQuery extends QueryPageModel {
    /**
     * 查询日期
     */
    private Date queryDate;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    public Date getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
