package com.example.qrazyqrsrus;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InitialsPictureGeneratorTests {

    @Test
    public void testGetInitials() {
        String name = "John Doe";
        String expectedInitials = "JD";
        String actualInitials = InitialsPictureGenerator.getInitials(name);
        assertEquals(expectedInitials, actualInitials);
    }

}
