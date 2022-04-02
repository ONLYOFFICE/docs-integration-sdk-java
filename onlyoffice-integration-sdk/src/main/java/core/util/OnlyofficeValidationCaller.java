package core.util;

import exception.OnlyofficeInvalidParameterException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class OnlyofficeValidationCaller {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    /**
     *
     * @param request
     * @throws OnlyofficeInvalidParameterException
     */
    public static void validate(Object request) throws OnlyofficeInvalidParameterException {
        Set<ConstraintViolation<Object>> violations = validator.validate(request);
        if (violations.size() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append("ONLYOFFICE Config validation exception with the following violations:\n");
            for (ConstraintViolation<Object> violation : violations) {
                builder.append(violation.getMessage() + "\n");
            }
            throw new OnlyofficeInvalidParameterException(builder.toString());
        }
    }
}
