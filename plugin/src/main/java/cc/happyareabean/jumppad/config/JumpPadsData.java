package cc.happyareabean.jumppad.config;

import cc.happyareabean.jumppad.object.JumpPadsObject;
import de.exlll.configlib.Configuration;
import de.exlll.configlib.YamlConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Getter
@Setter
@SuppressWarnings("FieldMayBeFinal")
public class JumpPadsData {

	private List<JumpPadsObject> jumpPads = new ArrayList<>();

	public static YamlConfigurationProperties PROPERTIES = YamlConfigurationProperties.newBuilder()
            .header(String.join("\n", List.of(
                    "=========================================================================",
                    " ",
                    "JumpPads data files - jumppads.yml",
                    " ",
                    "=========================================================================",
                    " ",
                    "You probably don't need to edit this file directly, use /jumppads command instead!",
                    " ",
                    "========================================================================="
            )))
            .build();
}
