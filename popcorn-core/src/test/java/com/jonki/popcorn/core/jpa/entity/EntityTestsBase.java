package com.jonki.popcorn.core.jpa.entity;

import org.junit.BeforeClass;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Base class for all test classes for entities in the model package.
 */
public class EntityTestsBase {

    private static Validator validator;

    /**
     * Setup the validator.
     */
    @BeforeClass
    public static void setupClass() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Get the validator object.
     *
     * @param <E>    The type of entity to validate
     * @param entity The entity to validate
     */
    protected <E> void validate(final E entity) {
        final Set<ConstraintViolation<E>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}