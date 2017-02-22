package job.config;

/**
 * Created by alan.zheng on 2017/1/20.
 */
public enum JobCommand {
    /**
     * 启动
     */
    START("START"),
    /**
     * 执行(应用于下次执行时间还未到时手动执行一次)
     */
    EXECUTE("EXECUTE"),
    /**
     * 暂停
     */
    PAUSE("PAUSE"),
    /**
     * 恢复
     */
    RESUME("RESUME"),
    /**
     * 停止
     */
    SHUTDOWN("SHUTDOWN"),
    /**
     * 修改
     */
    EDIT("EDIT"),
    /**
     * 删除
     */
    DELETE("DELETE")
    ;

    private String command;

    private JobCommand(String _command){
        command=_command;
    }

    public String getCommand() {
        return command;
    }
}
