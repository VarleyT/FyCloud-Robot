package fycloud.robot.core.entity.chat;

import lombok.Builder;
import lombok.Getter;

/**
 * @author VarleyT
 * @date 2022/5/20 20:05
 */
@Builder
@Getter
public class ChatResourceInfo {
    private String content;
    private String typed;
    private String remark;

    public ChatResourceInfo(String content, String typed, String remark) {
        this.content = content;
        this.typed = typed;
        this.remark = remark;
    }

}
