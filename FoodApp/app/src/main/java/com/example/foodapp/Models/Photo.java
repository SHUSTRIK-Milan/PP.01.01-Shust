package com.example.foodapp.Models;

public class Photo {
    private byte[] imageBytes;
    private String filename;

    public Photo(byte[] imageBytes, String filename) {
        this.imageBytes = imageBytes;
        this.filename = filename;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
