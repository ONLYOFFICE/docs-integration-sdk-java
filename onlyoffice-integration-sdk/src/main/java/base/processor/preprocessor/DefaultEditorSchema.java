package base.processor.preprocessor;

import core.model.OnlyofficeModelMutator;
import core.model.config.Config;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
public class DefaultEditorSchema {
    private final String key;
    private final String token;
    private OnlyofficeModelMutator<Config> mutator;
}
