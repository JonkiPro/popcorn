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
public class ResourceForbiddenExceptionUnitTests extends RuntimeException {

    private static final String ERROR_MESSAGE = "Forbidden";
    private static final IOException IOE = new IOException("IOException");

    /**
     * Test the constructor.
     *
     * @throws ResourceForbiddenException When access is forbidden
     */
    @Test(expected = ResourceForbiddenException.class)
    public void testTwoArgConstructor() throws ResourceForbiddenException {
        final ResourceForbiddenException re = new ResourceForbiddenException(ERROR_MESSAGE, IOE);
        Assert.assertEquals(HttpURLConnection.HTTP_FORBIDDEN, re.getErrorCode());
        Assert.assertEquals(ERROR_MESSAGE, re.getMessage());
        Assert.assertEquals(IOE, re.getCause());
        throw re;
    }

    /**
     * Test the constructor.
     *
     * @throws ResourceForbiddenException When access is forbidden
     */
    @Test(expected = ResourceForbiddenException.class)
    public void testMessageArgConstructor() throws ResourceForbiddenException {
        final ResourceForbiddenException re = new ResourceForbiddenException(ERROR_MESSAGE);
        Assert.assertEquals(HttpURLConnection.HTTP_FORBIDDEN, re.getErrorCode());
        Assert.assertEquals(ERROR_MESSAGE, re.getMessage());
        Assert.assertNull(re.getCause());
        throw re;
    }
}
