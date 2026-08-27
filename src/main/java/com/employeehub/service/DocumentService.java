package com.employeehub.service;

import com.employeehub.config.TenantContext;
import com.employeehub.domain.DownloadLink;
import com.employeehub.domain.PresignedUpload;
import com.employeehub.exception.ResourceNotFoundException;
import com.employeehub.model.Document;
import com.employeehub.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private S3Client s3Client;
    @Autowired
    private S3Presigner s3Presigner;

    // One bucket per tenant (see S3Config) — same "bean"/"intellan"/
    // "kkassociates" keys TenantRoutingDataSource already uses for the
    // JDBC side, just applied to a bucket name instead of a DB URL.
    private String bucketForCurrentTenant() {
        String tenant = TenantContext.get();
        return "employeehub-documents-" + tenant;
    }

    public PresignedUpload presignUpload(String entityType, Long entityId, String fileName, String contentType) {
        String s3Key = entityType.toLowerCase() + "/" + entityId + "/" + UUID.randomUUID() + "-" + fileName;
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketForCurrentTenant())
                .key(s3Key)
                .contentType(contentType)
                .build();
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putRequest)
                .build();
        String url = s3Presigner.presignPutObject(presignRequest).url().toString();
        return new PresignedUpload(url, s3Key);
    }

    public Document confirm(Document meta) {
        meta.setUploadedDate(LocalDateTime.now());
        return documentRepository.save(meta);
    }

    public List<Document> listForEntity(String entityType, Long entityId) {
        return documentRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    public DownloadLink presignDownload(Long id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not exist with id: " + id));
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucketForCurrentTenant())
                .key(doc.getS3Key())
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(getRequest)
                .build();
        String url = s3Presigner.presignGetObject(presignRequest).url().toString();
        return new DownloadLink(url);
    }

    public void delete(Long id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not exist with id: " + id));
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketForCurrentTenant())
                .key(doc.getS3Key())
                .build());
        documentRepository.delete(doc);
    }
}
