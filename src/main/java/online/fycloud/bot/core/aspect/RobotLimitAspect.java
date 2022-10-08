package online.fycloud.bot.core.aspect;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.message.events.GroupMsg;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import online.fycloud.bot.core.BotCore;
import online.fycloud.bot.core.annotation.RobotLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
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
    private static ExpiringMap<String, Integer> map = ExpiringMap.builder()
            .variableExpiration().expirationPolicy(ExpirationPolicy.CREATED).build();

    @Pointcut("@annotation(online.fycloud.bot.core.annotation.RobotLimit)")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public void around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if (arg instanceof GroupMsg) {
                String groupCode = ((GroupMsg) arg).getGroupInfo().getGroupCode();
                Method method = ((MethodSignature) pjp.getSignature()).getMethod();
                RobotLimit annotation = method.getAnnotation(RobotLimit.class);
                if (annotation.boot()) {
                    if (!BotCore.BOOT_MAP.get(Long.valueOf(groupCode))) {
                        return;
                    }
                }
                String methodName = method.getName();
                final String KEY = groupCode + methodName;
                if (map.get(KEY) == null) {
                    map.put(KEY, 1, annotation.time(), TimeUnit.MILLISECONDS);
                    pjp.proceed();
                    return;
                } else {
                    Integer count = map.get(KEY);
                    if (count >= annotation.count()) {
                        return;
                    }
                    map.put(KEY, count + 1);
                }
                pjp.proceed();
                return;
            }
        }
        log.error("RobotLimitAspect未找到参数类型<GroupMsg>");
        pjp.proceed();
    }
}
