package core.util;

import exception.OnlyofficeInvalidParameterRuntimeException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class OnlyofficeModelValidator {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    /**
     *
     * @param request
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public static void validate(Object request) throws OnlyofficeInvalidParameterRuntimeException {
        Set<ConstraintViolation<Object>> violations = validator.validate(request);
        if (violations.size() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append("ONLYOFFICE Config validation exception with the following violations:\n");
            for (ConstraintViolation<Object> violation : violations) {
                builder.append(violation.getMessage() + "\n");
            }
            throw new OnlyofficeInvalidParameterRuntimeException(builder.toString());
        }
    }
}
