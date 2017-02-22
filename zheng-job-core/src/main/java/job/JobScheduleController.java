package job;

import org.quartz.*;

import java.util.Date;
import java.util.List;

/**
 * 作业调度控制器
 * Created by XR on 2016/12/22.
 */
public class JobScheduleController {
    private Scheduler scheduler;

    private JobDetail jobDetail;

//    private final SchedulerFacade schedulerFacade;

    private String triggerIdentity;

    public JobScheduleController(Scheduler _scheduler,JobDetail _jobDetail,String _triggerIdentity){
        scheduler=_scheduler;
        jobDetail=_jobDetail;
        triggerIdentity=_triggerIdentity;
    }
    /**
     * 调度作业.
     *
     * @param cron CRON表达式
     */
    public void scheduleJob(final String cron) {
        try {
            if (!scheduler.checkExists(jobDetail.getKey())) {
                scheduler.scheduleJob(jobDetail, createTrigger(cron));
            }
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

    /**
     * 重新调度作业.
     *
     * @param cron CRON表达式
     */
    public void rescheduleJob(final String cron) {
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(TriggerKey.triggerKey(triggerIdentity));
            if (!scheduler.isShutdown() && null != trigger && !cron.equals(trigger.getCronExpression())) {
                scheduler.rescheduleJob(TriggerKey.triggerKey(triggerIdentity), createTrigger(cron));
            }
        } catch (final SchedulerException ex) {
            ex.printStackTrace();
        }
    }

    private CronTrigger createTrigger(final String cron) {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
//        if (schedulerFacade.loadJobConfiguration().getTypeConfig().getCoreConfig().isMisfire()) {
//            cronScheduleBuilder = cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
//        } else {
//            cronScheduleBuilder = cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
//        }
        return TriggerBuilder.newTrigger()
                .withIdentity(triggerIdentity)
                .withSchedule(cronScheduleBuilder).build();
    }

    /**
     * 获取下次作业触发时间.
     *
     * @return 下次作业触发时间
     */
    public Date getNextFireTime() {
        List<? extends Trigger> triggers;
        try {
            triggers = scheduler.getTriggersOfJob(jobDetail.getKey());
        } catch (final SchedulerException ex) {
            return null;
        }
        Date result = null;
        for (Trigger each : triggers) {
            Date nextFireTime = each.getNextFireTime();
            if (null == nextFireTime) {
                continue;
            }
            if (null == result) {
                result = nextFireTime;
            } else if (nextFireTime.getTime() < result.getTime()) {
                result = nextFireTime;
            }
        }
        return result;
    }

    /**
     * 暂停作业.
     */
    public void pauseJob() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.pauseAll();
            }
        } catch (final SchedulerException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 恢复作业.
     */
    public void resumeJob() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.resumeAll();
            }
        } catch (final SchedulerException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 立刻启动作业.
     */
    public void triggerJob() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.triggerJob(jobDetail.getKey());
            }
        } catch (final SchedulerException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 关闭调度器.
     */
    public void shutdown() {
//        schedulerFacade.releaseJobResource();
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}
