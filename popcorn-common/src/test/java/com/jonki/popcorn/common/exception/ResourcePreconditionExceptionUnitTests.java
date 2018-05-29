package com.jonki.popcorn.common.exception;

import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Test the constructors of the ResourceException.
 */
@Category(UnitTest.class)
public class ResourcePreconditionExceptionUnitTests extends RuntimeException {

    private static final String ERROR_MESSAGE = "Precondition";
    private static final IOException IOE = new IOException("IOException");

    /**
     * Test the constructor.
     *
     * @throws ResourcePreconditionException On a precondition issue
     */
    @Test(expected = ResourcePreconditionException.class)
    public void testTwoArgConstructor() throws ResourcePreconditionException {
        final ResourcePreconditionException re = new ResourcePreconditionException(ERROR_MESSAGE, IOE);
        Assert.assertEquals(HttpURLConnection.HTTP_PRECON_FAILED, re.getErrorCode());
        Assert.assertEquals(ERROR_MESSAGE, re.getMessage());
        Assert.assertEquals(IOE, re.getCause());
        throw re;
    }

    /**
     * Test the constructor.
     *
     * @throws ResourcePreconditionException On a precondition issue
     */
    @Test(expected = ResourcePreconditionException.class)
    public void testMessageArgConstructor() throws ResourcePreconditionException {
        final ResourcePreconditionException re = new ResourcePreconditionException(ERROR_MESSAGE);
        Assert.assertEquals(HttpURLConnection.HTTP_PRECON_FAILED, re.getErrorCode());
        Assert.assertEquals(ERROR_MESSAGE, re.getMessage());
        Assert.assertNull(re.getCause());
        throw re;
    }
}
