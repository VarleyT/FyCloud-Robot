package online.fycloud.bot.entertainment.entity;

import lombok.Builder;
import lombok.Getter;

/**
 * @author VarleyT
 *
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
