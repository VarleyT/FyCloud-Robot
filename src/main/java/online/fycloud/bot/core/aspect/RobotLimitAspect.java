package online.fycloud.bot.core.aspect;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.message.assists.Permissions;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import online.fycloud.bot.core.BotCore;
import online.fycloud.bot.core.annotation.RobotLimit;
import online.fycloud.bot.core.config.BotConfig;
import online.fycloud.bot.core.util.MsgUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author VarleyT
 * @date 2022/10/7
 */
@Slf4j
@Aspect
@Component
public class RobotLimitAspect {

    @Autowired
    private BotConfig botConfig;
    private static ExpiringMap<String, Integer> map = ExpiringMap.builder()
            .variableExpiration().expirationPolicy(ExpirationPolicy.CREATED).build();

    @Pointcut("@annotation(online.fycloud.bot.core.annotation.RobotLimit)")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public void around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RobotLimit annotation = method.getAnnotation(RobotLimit.class);
        for (Object arg : args) {
            String code = "";
            if (arg instanceof GroupMsg) {
                GroupMsg msg = (GroupMsg) arg;
                String groupCode = msg.getGroupInfo().getGroupCode();
                if (annotation.isBoot()) {
                    if (!BotCore.BOOT_MAP.get(Long.valueOf(groupCode))) {
                        return;
                    }
                }
                if (annotation.permission() != Permissions.MEMBER) {
                    if (annotation.permission().getLevel() > msg.getAccountInfo().getPermission().getLevel()) {
                        if (!botConfig.ADMINISTRATOR.equals(msg.getAccountInfo().getAccountCode())) {
                            MsgUtil.sendMsgByTime(msg, "抱歉，您没有权限操作！");
                            return;
                        }
                    }
                }
                code = groupCode;
            } else if (arg instanceof PrivateMsg) {
                PrivateMsg msg = (PrivateMsg) arg;
                String accountCode = msg.getAccountInfo().getAccountCode();
                if (annotation.permission() != Permissions.MEMBER) {
                    if (!botConfig.ADMINISTRATOR.equals(accountCode)) {
                        MsgUtil.sendMsgByTime(msg, "抱歉，您没有权限操作！");
                        return;
                    }
                }
                code = "PrivateMsg" + accountCode;
            } else {
                log.error("RobotLimitAspect未找到参数类型<GroupMsg || PrivateMsg>");
                pjp.proceed();
                return;
            }
            String methodName = method.getName();
            final String KEY = code + methodName;
            if (map.get(KEY) == null) {
                map.put(KEY, 1, annotation.time(), TimeUnit.MILLISECONDS);
                pjp.proceed();
                return;
            } else {
                Integer count = map.get(KEY);
                if (count >= annotation.count()) {
//                     TODO: 2022/10/10 复读功能重复请求问题
//                        MsgUtil.sendMsgByTime(msg, "请求频繁");
                    return;
                }
                map.put(KEY, count + 1);
            }
            pjp.proceed();
        }
    }
}
