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

//    @Test
//    public void testCreateInitialsImage() {
//        String initials = "JD";
//        Bitmap image = InitialsPictureGenerator.createInitialsImage(initials);
//        assertNotNull(image);
//        assertEquals(100, image.getWidth());
//        assertEquals(100, image.getHeight());
//    }

}
