package com.azurestorage.spring.controller;


import com.azurestorage.spring.service.BlobStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/azurestore")
public class FileUploadController {

    private final BlobStorageService blobStorageService;

    @Autowired
    public FileUploadController(BlobStorageService blobStorageService){
        this.blobStorageService = blobStorageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload a file to Azure Blob Storage",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "object", format = "binary")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
                    @ApiResponse(responseCode = "500", description = "File upload failed")
            }
    )
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file){
        try{
            String fileUrl = blobStorageService.uploadFile(file);
            return new ResponseEntity<>("File upload successfully: " + fileUrl, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
