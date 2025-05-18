package org.metalib.wiser.api.template.root;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.stringtemplate.v4.ST;

import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.MD;
import static org.metalib.wiser.api.template.ApiWiserFinals.README;
import static org.metalib.wiser.api.template.ApiWiserFinals.ROOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.SLASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.ST_EXT;
import static org.metalib.wiser.api.template.ApiWiserFinals.TEMPLATES;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadMeBuilder {

    public static String createProjectReadMe(ApiWiserBundle bundle) {
        final var st = new ST(string(SLASH + TEMPLATES + SLASH + ROOT + SLASH+ README + DOT + MD + DOT+ ST_EXT));
        bundle.unwrap().forEach(st::add);
        return st.render();
    }

    @SneakyThrows
    static String string(final String resourcePath) {
        try (final var inputStream = ReadMeBuilder.class.getResourceAsStream(resourcePath)) {
            return new String(Objects.requireNonNull(inputStream).readAllBytes(), StandardCharsets.UTF_8);
        }
    }

}
