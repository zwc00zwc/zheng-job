package controller;

import annotation.Auth;
import common.JsonResult;
import common.utility.DateUtility;
import job.config.JobCommand;
import job.db.model.PageModel;
import job.db.model.job.Job;
import job.db.model.job.JobLog;
import job.db.model.job.query.JobLogQuery;
import job.db.model.job.query.JobQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import services.JobServices;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by alan.zheng on 2017/1/18.
 */
@Controller
public class JobController extends BaseController {
    @Autowired
    private JobServices jobServices;

    @Auth(rule = "/job/index")
    @RequestMapping(value = "/job/index")
    public String index(Model model,HttpSession httpSession,Integer currPage){
        JobQuery query=new JobQuery();
        if (currPage!=null){
            query.setCurrPage(currPage);
        }
        PageModel<Job> jobs= jobServices.queryPageList(query);
        model.addAttribute("jobs",jobs);
        model.addAttribute("user",getAuthUser(httpSession));
        return "/job/index";
    }

    @Auth(rule = "/job/add")
    @RequestMapping(value = "/job/add")
    public String add(){
        return "/job/add";
    }

    @Auth(rule ="/job/add")
    @ResponseBody
    @RequestMapping(value = "/job/adding")
    public JsonResult adding(Job job){
        job.setCreateTime(new Date());
        try {
            if (jobServices.insertJob(job)){
                return jsonResult(1,"新增成功");
            }
        } catch (Exception e) {
            return jsonResult(-1,"新增失败");
        }
        return jsonResult(-1,"新增失败");
    }

    @Auth(rule ="/job/log")
    @RequestMapping(value = "/job/log")
    public String log(Model model,String queryDate,String startTime,String endTime,Integer currPage,HttpSession httpSession){
        JobLogQuery query =new JobLogQuery();
        query.setQueryDate(DateUtility.getDateFromStr(queryDate,"yyyy-MM-dd"));
        if (currPage!=null&&currPage>0){
            query.setCurrPage(currPage);
        }
        if (StringUtils.isNotEmpty(startTime)){
            query.setStartTime(DateUtility.getDateFromStr(queryDate +" "+ startTime,"yyyy-MM-dd hh:mm:ss"));
        }
        if (StringUtils.isNotEmpty(endTime)){
            query.setEndTime(DateUtility.getDateFromStr(queryDate +" "+ endTime,"yyyy-MM-dd hh:mm:ss"));
        }
        PageModel<JobLog> logs=jobServices.queryPageJobLog(query);
        model.addAttribute("queryDate",queryDate);
        model.addAttribute("startTime",startTime);
        model.addAttribute("endTime",endTime);
        model.addAttribute("logs",logs);
        model.addAttribute("user",getAuthUser(httpSession));
        return "/job/log";
    }

    @Auth(rule = "/job/command" )
    @ResponseBody
    @RequestMapping(value = "/job/command")
    public JsonResult command(@RequestParam(value = "jobid") Long jobid,@RequestParam(value = "command")String command){
        if (JobCommand.EXECUTE.getCommand().equals(command)){
            jobServices.jobCommand(jobid, JobCommand.EXECUTE);
            return jsonResult(1,"已执行");
        }
        if (JobCommand.PAUSE.getCommand().equals(command)){
            jobServices.jobCommand(jobid, JobCommand.PAUSE);
            return jsonResult(1,"已执行暂停");
        }
        if (JobCommand.RESUME.getCommand().equals(command)){
            jobServices.jobCommand(jobid, JobCommand.RESUME);
            return jsonResult(1,"已执行启动");
        }
        if (JobCommand.SHUTDOWN.getCommand().equals(command)){
            jobServices.jobCommand(jobid, JobCommand.SHUTDOWN);
            return jsonResult(1,"已执行启动");
        }
        if (JobCommand.EDIT.getCommand().equals(command)){
            jobServices.jobCommand(jobid, JobCommand.EDIT);
            return jsonResult(1,"已执行启动");
        }
        return jsonResult(-1,"未知命令");
    }

    @Auth(rule = "/job/delete" )
    @ResponseBody
    @RequestMapping(value = "/job/delete")
    public JsonResult delete(@RequestParam(value = "jobid") Long jobid){
        Job job= jobServices.queryById(jobid);
        if (job==null){
            return jsonResult(-1,"任务不存在");
        }
        if (job.getStatus()==1){
            return jsonResult(-1,"任务正在运行中");
        }
        if (jobServices.deleteJob(jobid)){
            return jsonResult(1,"删除成功");
        }
        return jsonResult(-1,"删除失败");
    }
}
