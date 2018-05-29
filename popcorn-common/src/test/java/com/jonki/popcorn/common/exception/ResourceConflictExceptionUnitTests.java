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
public class ResourceConflictExceptionUnitTests extends RuntimeException {

    private static final String ERROR_MESSAGE = "Conflict";
    private static final IOException IOE = new IOException("IOException");

    /**
     * Test the constructor.
     *
     * @throws ResourceConflictException On conflict
     */
    @Test(expected = ResourceConflictException.class)
    public void testTwoArgConstructor() throws ResourceConflictException {
        final ResourceConflictException re = new ResourceConflictException(ERROR_MESSAGE, IOE);
        Assert.assertEquals(HttpURLConnection.HTTP_CONFLICT, re.getErrorCode());
        Assert.assertEquals(ERROR_MESSAGE, re.getMessage());
        Assert.assertEquals(IOE, re.getCause());
        throw re;
    }

    /**
     * Test the constructor.
     *
     * @throws ResourceConflictException On conflict
     */
    @Test(expected = ResourceConflictException.class)
    public void testMessageArgConstructor() throws ResourceConflictException {
        final ResourceConflictException re = new ResourceConflictException(ERROR_MESSAGE);
        Assert.assertEquals(HttpURLConnection.HTTP_CONFLICT, re.getErrorCode());
        Assert.assertEquals(ERROR_MESSAGE, re.getMessage());
        Assert.assertNull(re.getCause());
        throw re;
    }
}
