package online.fycloud.bot.core.annotation;

import love.forte.simbot.api.message.assists.Permissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author VarleyT
 * @date 2022/10/7
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RobotLimit {
    /**
     * 响应时间(ms)
     *
     * @return
     */
    int time() default 2000;

    /**
     * 响应次数
     *
     * @return
     */
    int count() default 1;

    /**
     * 群开关限制（私聊消息无效）
     *
     * @return
     */
    boolean isBoot() default true;

    /**
     * 权限
     * MEMBER：群成员
     * ADMINISTRATOR：管理员
     * OWNER：群主
     * 私聊消息中 ADMINISTRATOR与OWNER 均指BOT管理员（非群管理员）
     *
     * @return
     */
    Permissions permission() default Permissions.MEMBER;
}
