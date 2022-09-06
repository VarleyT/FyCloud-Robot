package online.fycloud.bot.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author VarleyT
 */
@Component
public class BotConfig {
    @Value("${bot.admin}")
    public String ADMINISTRATOR;
}
