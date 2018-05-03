package com.war;

import com.test.category.UnitTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Unit tests for the war class.
 */
@Category(UnitTest.class)
public class WarApplicationUnitTests {

    /**
     * Configure WarApplication.
     */
    @Test
    public void canConfigure() {
        final SpringApplicationBuilder builder = Mockito.mock(SpringApplicationBuilder.class);
        final WarApplication war = new WarApplication();
        war.configure(builder);
        Mockito.verify(builder, Mockito.times(1)).sources(WarApplication.class);
    }
}
