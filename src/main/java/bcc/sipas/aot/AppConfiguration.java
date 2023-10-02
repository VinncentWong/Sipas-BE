package bcc.sipas.aot;

import bcc.sipas.util.QueryUtils;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;

// will be continued on migration to Spring Native branch :D
@Configuration
@ImportRuntimeHints(AppConfiguration.AppRuntimeHintsRegistrar.class)
public class AppConfiguration {

    public static class AppRuntimeHintsRegistrar implements RuntimeHintsRegistrar{

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            // register resource
            hints.resources()
                    .registerResource(new ClassPathResource("application.yaml"));

            // register reflection
            hints.reflection()
                    .registerType(
                            QueryUtils.class,
                            builder -> {
                                builder.withMembers(
                                        MemberCategory.PUBLIC_FIELDS,
                                        MemberCategory.INVOKE_PUBLIC_METHODS,
                                        MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS
                                );
                            }
                    );
        }
    }
}
