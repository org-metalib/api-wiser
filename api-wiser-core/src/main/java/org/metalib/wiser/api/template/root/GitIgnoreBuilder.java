package org.metalib.wiser.api.template.root;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.stringtemplate.v4.ST;

import static org.metalib.wiser.api.template.ApiWiserFinals.textFromResource;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GitIgnoreBuilder {

    public static String createGitIgnore(ApiWiserBundle bundle) {
        final var st = new ST(textFromResource("/templates/root/gitignore.st"));
        bundle.unwrap().forEach(st::add);
        return st.render();
    }
}
