package com.employeehub.domain;

// Handed to the frontend so it can PUT the file bytes straight to S3
// (uploadUrl), then POST back the s3Key alongside the rest of the
// Document metadata to record it — see DocumentController.
public record PresignedUpload(String uploadUrl, String s3Key) {
}
