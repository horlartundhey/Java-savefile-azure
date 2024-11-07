package com.azurestorage.spring.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//@AllArgsConstructor
@Service
public class BlobStorageService {

    private final BlobServiceClient blobServiceClient;

    @Value("${azure.storage.container-name}")
    private String containerName;

    public BlobStorageService(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    public String uploadFile(MultipartFile file) throws IOException{
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

//        let's make sure the container exist
        if(!containerClient.exists()){
            containerClient.create();
        }

        BlobClient blobClient = containerClient.getBlobClient(file.getOriginalFilename());

        blobClient.upload(file.getInputStream(), file.getSize(), true);

        blobClient.setHttpHeaders(new BlobHttpHeaders().setContentType(file.getContentType()));

        return blobClient.getBlobUrl();
    }

}
