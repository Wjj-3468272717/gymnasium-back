package com.v1.api.image;

public interface ImageRpcService {
    String uploadImage(byte[] fileBytes, String fileName, String contentType);
}
