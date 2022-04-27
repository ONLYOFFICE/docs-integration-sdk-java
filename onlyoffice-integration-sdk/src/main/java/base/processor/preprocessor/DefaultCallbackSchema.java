package base.processor.preprocessor;

import core.model.OnlyofficeModelMutator;
import core.model.callback.Callback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
public class DefaultCallbackSchema {
    private final String key;
    private final String token;
    private OnlyofficeModelMutator<Callback> mutator;
}
