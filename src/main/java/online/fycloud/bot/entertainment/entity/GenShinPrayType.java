package online.fycloud.bot.entertainment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author VarleyT
 *
 */
@AllArgsConstructor
@Getter
public enum GenShinPrayType {
    /**
     * 角色池
     */
    ROLE("/RolePray"),
    /**
     * 武器池
     */
    ARM("/ArmPray"),
    /**
     * 常驻池
     */
    PERM("/PermPray"),
    /**
     * 全角色池
     */
    FULL_ROLE("/FullRolePray"),
    /**
     * 全武器池
     */
    FULL_ARM("/FullArmPray"),
    /**
     * 单抽
     */
    ONE("/PrayOne"),
    /**
     * 十连
     */
    TEN("/PrayTen");

    private final String value;

}
