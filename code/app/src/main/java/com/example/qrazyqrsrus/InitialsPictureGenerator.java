package com.example.qrazyqrsrus;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class InitialsPictureGenerator {

    /**
     * Extracts the initials from a given name. Each part of the name separated by whitespace
     * contributes the first character to the initials, which are then combined into a single string.
     *
     * @param name The full name from which to extract initials. It can contain multiple words separated by spaces.
     * @return A string containing the initials of the provided name, with each initial in uppercase.
     *         If the name is empty or only whitespace, an empty string is returned.
     */
    public static String getInitials(String name) {
        String[] parts = name.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                initials.append(part.substring(0, 1).toUpperCase());
            }
        }
        return initials.toString();
    }

    /**
     * Creates a bitmap image containing the provided initials. The initials are centered within
     * the image and drawn with a specified paint configuration.
     *
     * @param initials The initials to be drawn on the bitmap image. This should be a short string,
     *                 typically 1-3 characters, representing the initials extracted from a full name.
     * @return A {@link Bitmap} object representing a 100x100 pixel image with a white background,
     *         containing the provided initials in blue color. The text is bold and centered both
     *         horizontally and vertically within the image.
     */
    public static Bitmap createInitialsImage(String initials) {
        Bitmap image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawColor(Color.WHITE);
        float centerX = (100 - paint.measureText(initials)) / 2;
        float centerY = (100 - ((paint.descent() + paint.ascent()) / 2)) / 2;
        canvas.drawText(initials, centerX, centerY, paint);
        return image;
    }
}
