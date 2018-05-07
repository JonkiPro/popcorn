package com.jonki.popcorn;

import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Unit tests for the Popcorn war class.
 */
@Category(UnitTest.class)
public class PopcornWarUnitTests {

    /**
     * Configure PopcornWar.
     */
    @Test
    public void canConfigure() {
        final SpringApplicationBuilder builder = Mockito.mock(SpringApplicationBuilder.class);
        final PopcornWar war = new PopcornWar();
        war.configure(builder);
        Mockito.verify(builder, Mockito.times(1)).sources(PopcornWeb.class);
    }
}
