package com.jonki.popcorn.common.exception;

import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;

/**
 * Test the constructors of the ResourceException.
 */
@Category(UnitTest.class)
public class ResourceExceptionUnitTests extends RuntimeException {

    private static final int ERROR_CODE = 404;
    private static final String ERROR_MESSAGE = "Not Found";
    private static final IOException IOE = new IOException("IOException");

    /**
     * Test the constructor.
     *
     * @throws ResourceException On exception
     */
    @Test(expected = ResourceException.class)
    public void testThreeArgConstructor() throws ResourceException {
        final ResourceException re = new ResourceException(ERROR_CODE, ERROR_MESSAGE, IOE);
        Assert.assertEquals(ERROR_CODE, re.getErrorCode());
        Assert.assertEquals(ERROR_MESSAGE, re.getMessage());
        Assert.assertEquals(IOE, re.getCause());
        throw re;
    }

    /**
     * Test the constructor.
     *
     * @throws ResourceException On exception
     */
    @Test(expected = ResourceException.class)
    public void testTwoArgConstructorWithMessage() throws ResourceException {
        final ResourceException re = new ResourceException(ERROR_CODE, ERROR_MESSAGE);
        Assert.assertEquals(ERROR_CODE, re.getErrorCode());
        Assert.assertEquals(ERROR_MESSAGE, re.getMessage());
        Assert.assertNull(re.getCause());
        throw re;
    }
}
