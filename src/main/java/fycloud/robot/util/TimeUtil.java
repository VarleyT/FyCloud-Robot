package fycloud.robot.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import fycloud.robot.FyRobotApp;

import java.util.concurrent.TimeUnit;

/**
 * @author VarleyT
 * @date 2022/5/30 0:04
 */
public class TimeUtil {
    public static String getRuntime(){
        DateTime NOW_TIME = DateTime.now();
        DateTime START_TIME = FyRobotApp.ROBOT_CORE.START_TIME;
        StringBuilder sb = new StringBuilder();
        sb.append(DateUtil.between(START_TIME, NOW_TIME, DateUnit.DAY))
                .append("天")
                .append(DateUtil.between(START_TIME, NOW_TIME, DateUnit.HOUR) % 24)
                .append("时")
                .append(DateUtil.between(START_TIME, NOW_TIME, DateUnit.MINUTE) % 60)
                .append("分")
                .append(DateUtil.between(START_TIME, NOW_TIME, DateUnit.SECOND) % 60)
                .append("秒");
        return sb.toString();
    }
}
