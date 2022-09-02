package online.fycloud.bot.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import online.fycloud.bot.core.entity.BootStatusInfo;

/**
 * @author VarleyT
 *
 */
public interface BootStatusService extends IService<BootStatusInfo> {
    /**
     * 设置群开关机状态
     * @param groupCode 群号
     * @param status 状态
     */
    void setBootStatus(Long groupCode,boolean status);
}
