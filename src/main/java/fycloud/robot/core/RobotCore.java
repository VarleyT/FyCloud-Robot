package fycloud.robot.core;

import java.util.concurrent.*;

/**
 * @author 19634
 * @version 1.0
 * @date 2022/5/5 12:50
 */
public class RobotCore {
    public static final ExecutorService THREAD_POOL;

    static {
        THREAD_POOL = Executors.newFixedThreadPool(10);
    }

    public RobotCore(){

    }
}
