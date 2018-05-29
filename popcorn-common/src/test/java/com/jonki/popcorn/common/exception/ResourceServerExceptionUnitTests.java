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
public class ResourceServerExceptionUnitTests extends RuntimeException {

    private static final String ERROR_MESSAGE = "Internal Server Error";
    private static final IOException IOE = new IOException("IOException");

    /**
     * Test the constructor.
     *
     * @throws ResourceServerException When internal server error
     */
    @Test(expected = ResourceServerException.class)
    public void testTwoArgConstructor() throws ResourceServerException {
        final ResourceServerException re = new ResourceServerException(ERROR_MESSAGE, IOE);
        Assert.assertEquals(HttpURLConnection.HTTP_INTERNAL_ERROR, re.getErrorCode());
        Assert.assertEquals(ERROR_MESSAGE, re.getMessage());
        Assert.assertEquals(IOE, re.getCause());
        throw re;
    }

    /**
     * Test the constructor.
     *
     * @throws ResourceServerException When internal server error
     */
    @Test(expected = ResourceServerException.class)
    public void testMessageArgConstructor() throws ResourceServerException {
        final ResourceServerException re = new ResourceServerException(ERROR_MESSAGE);
        Assert.assertEquals(HttpURLConnection.HTTP_INTERNAL_ERROR, re.getErrorCode());
        Assert.assertEquals(ERROR_MESSAGE, re.getMessage());
        Assert.assertNull(re.getCause());
        throw re;
    }
}
