package com.jonki.popcorn.core.util;

import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.Arrays;

/**
 * Unit tests for FileUtils.
 */
@Category(UnitTest.class)
public class FileUtilsUnitTests {

    private static final String PATH_TO_TEST_FILE_IMAGE = "resources/test-image.png";

    /**
     * Test the validation of the image file.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void testValidImage() throws ResourceException {
        FileUtils.validImage(new File(PATH_TO_TEST_FILE_IMAGE));
    }

    /**
     * Test the validation of the image file list
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void testValidImageList() throws ResourceException {
        FileUtils.validImage(
                Arrays.asList(
                        new File(PATH_TO_TEST_FILE_IMAGE),
                        new File(PATH_TO_TEST_FILE_IMAGE)
                )
        );
    }
}
