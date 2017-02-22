package job.db.dal;

import job.db.BaseDB;
import job.db.model.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by alan.zheng on 2017/2/9.
 */
public class JobDal {
    public static Job queryByJobName(String jobName){
        Job job=new Job();
        String[] args=new String[]{jobName};
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection= BaseDB.getConnection();
            preparedStatement=connection.prepareStatement("SELECT id,jobName,corn,remark,createTime FROM tb_job WHERE jobName=?");
            BaseDB.query(preparedStatement,args);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                job.setId(resultSet.getLong("id"));
                job.setJobName(resultSet.getString("jobName"));
                job.setCorn(resultSet.getString("corn"));
                job.setCreateTime(resultSet.getDate("createTime"));
                job.setRemark(resultSet.getString("remark"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            BaseDB.dispose(connection,preparedStatement,resultSet);
        }
        return job;
    }
}
