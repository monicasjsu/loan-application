package com.esp.project.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AmazonClient {

	final Logger log = LoggerFactory.getLogger(AmazonClient.class);

	public static final String BUCKET_NAME = "sjsu-esp-project";
	private final AmazonS3 s3Client;

	public AmazonClient(final AmazonS3 s3Client) {
		this.s3Client = s3Client;
	}

	public boolean putS3Object(final InputStream inputStream, final String bucketName, final String keyName) {
		log.info("Uploading {} to S3 bucket {}", keyName, bucketName);
		try {
			ObjectMetadata objectMetadata = new ObjectMetadata();
			PutObjectResult res = s3Client.putObject(BUCKET_NAME, keyName, inputStream, objectMetadata);
			return true;
		} catch (AmazonServiceException ex) {
			log.error("Exception uploading S3 object for " + bucketName + " - "+ keyName, ex.getErrorMessage());
			return false;
		}
	}

	// Can also do caching for these request if the request comes before the expiration time.
	public String getS3ObjectUrlWithExpiration(final String bucketName, final String keyName) {
		log.info("Getting resource url {} from S3 bucket {}...\n", keyName, bucketName);
		try {
			// Set the preSigned URL to expire after one hour.
			final Date expiration = new Date();
			long expTimeMillis = expiration.getTime();
			expTimeMillis += 1000 * 60 * 60;
			expiration.setTime(expTimeMillis);

			final GeneratePresignedUrlRequest preSignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, keyName)
					.withMethod(HttpMethod.GET)
					.withExpiration(expiration);
			final URL url = s3Client.generatePresignedUrl(preSignedUrlRequest);
			log.info("Resource url for {} from S3 bucket {} is {}", keyName, bucketName, url.toString());
			return url.toString();
		} catch (AmazonServiceException ex) {
			log.error("Exception getting S3 object url for " + bucketName + " - " + keyName, ex.getErrorMessage());
			return null;
		}
	}

	public void creates3Bucket(final String bucketName) {
		try {
			if (!s3Client.doesBucketExistV2(bucketName)) {
				s3Client.createBucket(new CreateBucketRequest(bucketName));

				String bucketLocation = s3Client.getBucketLocation(new GetBucketLocationRequest(bucketName));
				System.out.println("Bucket location: " + bucketLocation);
			}
		} catch (AmazonServiceException e) {
			log.error("Some amazon service exception", e);
		} catch (SdkClientException e) {
			log.error("Some amazon SDK exception", e);
		}
	}

	public List<Bucket> getS3BucketsList() {
		List<Bucket> buckets = s3Client.listBuckets();
		log.info("Your Amazon S3 buckets are:");
		for (Bucket b : buckets) {
			log.info("* " + b.getName());
		}
		return buckets;
	}

	public void deleteS3Bucket(final String bucketName) {
		VersionListing version_listing = s3Client.listVersions(
				new ListVersionsRequest().withBucketName(bucketName));
		while (true) {
			for (Iterator<?> iterator =
			     version_listing.getVersionSummaries().iterator();
			     iterator.hasNext(); ) {
				S3VersionSummary vs = (S3VersionSummary) iterator.next();
				s3Client.deleteVersion(
						bucketName, vs.getKey(), vs.getVersionId());
			}

			if (version_listing.isTruncated()) {
				version_listing = s3Client.listNextBatchOfVersions(
						version_listing);
			} else {
				break;
			}
		}
	}
}
