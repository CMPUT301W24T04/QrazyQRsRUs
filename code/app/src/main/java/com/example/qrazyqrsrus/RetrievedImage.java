package com.example.qrazyqrsrus;

import android.graphics.Bitmap;

public class RetrievedImage {
    private String documentId;
    private Bitmap bitmap;
    private Boolean isEvent;

    public RetrievedImage(String documentId, Bitmap bitmap, Boolean isEvent) {
        this.documentId = documentId;
        this.bitmap = bitmap;
        this.isEvent = isEvent;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Boolean getEvent() {
        return isEvent;
    }

    public void setEvent(Boolean event) {
        isEvent = event;
    }
}
