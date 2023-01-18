package core.processor;

import core.model.config.Config;

public interface OnlyofficeEditorPostProcessor extends OnlyofficePostProcessor<Config> {
    void setJwtSecret(String jwtSecret);
}
