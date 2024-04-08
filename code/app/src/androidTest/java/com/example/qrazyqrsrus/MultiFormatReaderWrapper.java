package com.example.qrazyqrsrus;

import android.graphics.Bitmap;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;

/**
 * This class acts as a wrapper for MultiFormatReader, so that we can mock it's behaviour
 */
public class MultiFormatReaderWrapper {

    private final MultiFormatReader multiFormatReader;

    public MultiFormatReaderWrapper(MultiFormatReader multiFormatReader){
        this.multiFormatReader = multiFormatReader;
    }

    public Result decode(BinaryBitmap bitmap){
        try{
            return multiFormatReader.decode(bitmap);
        } catch (NotFoundException e){
            return null;
        }
    }
}
