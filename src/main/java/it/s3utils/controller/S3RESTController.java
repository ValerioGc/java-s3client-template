package it.s3utils.controller;

import java.io.IOException;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.micrometer.common.util.StringUtils;
import it.s3utils.client.S3ClientUtil;

@RestController
@RequestMapping("/s3")
public class S3RESTController {

    private final S3ClientUtil s3Client;

    public S3RESTController(S3ClientUtil s3Client) {
        this.s3Client = s3Client;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestParam MultipartFile file) throws IOException {
        if (file.isEmpty()
                || StringUtils.isBlank(file.getOriginalFilename())
                || file.getSize() == 0) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("File is not valid");
        }

        String key = file.getOriginalFilename().replace(" ", "_");
        s3Client.uploadFile(key, file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("File uploaded with key: " + key);
    }

    @GetMapping("/read")
    public ResponseEntity<byte[]> read(@RequestParam String key) throws Exception {
        if (StringUtils.isBlank(key)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        String path = "images/" + key;
        byte[] fileData = s3Client.downloadFile(path);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(key).build());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(fileData);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam String key) {
        s3Client.deleteFile(key);
        return ResponseEntity
                .ok("File deleted with key: " + key);
    }
}
