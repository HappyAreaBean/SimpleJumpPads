package cc.happyareabean.jumppad.config;

import de.exlll.configlib.Configuration;
import de.exlll.configlib.YamlConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Configuration
@Getter
@Setter
@SuppressWarnings("FieldMayBeFinal")
public class JumpPadsConfig {

    public static YamlConfigurationProperties PROPERTIES = YamlConfigurationProperties.newBuilder()
            .header(String.join("\n", List.of(
                    "=========================================================================",
                    " ",
                    "JumpPads config files - config.yml",
                    " ",
                    "========================================================================="
            )))
            .build();
}
