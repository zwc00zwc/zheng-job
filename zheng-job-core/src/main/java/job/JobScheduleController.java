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
        cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
//        withMisfireHandlingInstructionDoNothing  不触发立即执行 等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
//        withMisfireHandlingInstructionIgnoreMisfires  以错过的第一个频率时间立刻开始执行 重做错过的所有频率周期后 当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
//        withMisfireHandlingInstructionFireAndProceed  以当前时间为触发频率立刻触发一次执行 然后按照Cron频率依次执行
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
     * 暂停所有作业.
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
     * 暂停单个作业.
     */
    public void pauseSingleJob() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.pauseJob(jobDetail.getKey());
            }
        } catch (final SchedulerException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 恢复所有作业.
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
     * 恢复单个作业.
     */
    public void resumeSingleJob() {
//        try {
//            if (!scheduler.isShutdown()) {
//                scheduler.resumeJob(jobDetail.getKey());
//            }
//        } catch (final SchedulerException ex) {
//            ex.printStackTrace();
//        }
        try {
            if (!scheduler.isShutdown()){
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(TriggerKey.triggerKey(triggerIdentity));
                scheduler.rescheduleJob(TriggerKey.triggerKey(triggerIdentity), createTrigger(trigger.getCronExpression()));
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
