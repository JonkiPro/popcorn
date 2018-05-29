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
public class ResourceNotFoundExceptionUnitTests extends RuntimeException {

    private static final String ERROR_MESSAGE = "Not Found";
    private static final IOException IOE = new IOException("IOException");

    /**
     * Test the constructor.
     *
     * @throws ResourceNotFoundException When not found
     */
    @Test(expected = ResourceNotFoundException.class)
    public void testTwoArgConstructor() throws ResourceNotFoundException {
        final ResourceNotFoundException re = new ResourceNotFoundException(ERROR_MESSAGE, IOE);
        Assert.assertEquals(HttpURLConnection.HTTP_NOT_FOUND, re.getErrorCode());
        Assert.assertEquals(ERROR_MESSAGE, re.getMessage());
        Assert.assertEquals(IOE, re.getCause());
        throw re;
    }

    /**
     * Test the constructor.
     *
     * @throws ResourceNotFoundException When not found
     */
    @Test(expected = ResourceNotFoundException.class)
    public void testMessageArgConstructor() throws ResourceNotFoundException {
        final ResourceNotFoundException re = new ResourceNotFoundException(ERROR_MESSAGE);
        Assert.assertEquals(HttpURLConnection.HTTP_NOT_FOUND, re.getErrorCode());
        Assert.assertEquals(ERROR_MESSAGE, re.getMessage());
        Assert.assertNull(re.getCause());
        throw re;
    }
}
