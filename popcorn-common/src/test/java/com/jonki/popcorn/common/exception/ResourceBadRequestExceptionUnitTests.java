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
public class ResourceBadRequestExceptionUnitTests extends RuntimeException {

    private static final String ERROR_MESSAGE = "Bad Request";
    private static final IOException IOE = new IOException("IOException");

    /**
     * Test the constructor.
     *
     * @throws ResourceBadRequestException For a bad request
     */
    @Test(expected = ResourceBadRequestException.class)
    public void testTwoArgConstructor() throws ResourceBadRequestException {
        final ResourceBadRequestException re = new ResourceBadRequestException(ERROR_MESSAGE, IOE);
        Assert.assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, re.getErrorCode());
        Assert.assertEquals(ERROR_MESSAGE, re.getMessage());
        Assert.assertEquals(IOE, re.getCause());
        throw re;
    }

    /**
     * Test the constructor.
     *
     * @throws ResourceBadRequestException For a bad request
     */
    @Test(expected = ResourceBadRequestException.class)
    public void testMessageArgConstructor() throws ResourceBadRequestException {
        final ResourceBadRequestException re = new ResourceBadRequestException(ERROR_MESSAGE);
        Assert.assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, re.getErrorCode());
        Assert.assertEquals(ERROR_MESSAGE, re.getMessage());
        Assert.assertNull(re.getCause());
        throw re;
    }
}
