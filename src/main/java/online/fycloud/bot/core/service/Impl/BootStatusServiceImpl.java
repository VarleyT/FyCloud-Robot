package online.fycloud.bot.core.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import online.fycloud.bot.core.entity.BootStatusInfo;
import online.fycloud.bot.core.mapper.BootStatusMapper;
import online.fycloud.bot.core.service.BootStatusService;
import org.springframework.stereotype.Service;

/**
 * @author VarleyT
 *
 */
@Service
@RequiredArgsConstructor
public class BootStatusServiceImpl extends ServiceImpl<BootStatusMapper, BootStatusInfo> implements BootStatusService {

    private final BootStatusMapper bootStatusMapper;

    @Override
    public void setBootStatus(Long groupCode, boolean status) {
        BootStatusInfo statusInfo = new BootStatusInfo(null, groupCode, status);
        bootStatusMapper.update(statusInfo,
                new QueryWrapper<BootStatusInfo>()
                        .eq("group_code", statusInfo.getGroupCode()));
    }
}
