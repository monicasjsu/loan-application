package com.esp.project.client;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AmazonClientTest {

	private String bucketName = "testbucket";
	private String keyName = "testKey";

	@Mock
	private AmazonS3 s3Client;
	@Mock
	private PutObjectResult result;
	@Mock
	private URL url;
	@Mock
	private InputStream inputStream;

	private AmazonClient amazonClient;

	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		amazonClient = spy(new AmazonClient(s3Client));
	}

	@Test
	public void testPutS3Object() {
		doReturn(result).when(s3Client).putObject(anyString(), anyString(),
				any(InputStream.class), any(ObjectMetadata.class));

		boolean result = amazonClient.putS3Object(inputStream, bucketName, keyName);

		verify(amazonClient).putS3Object(any(InputStream.class),anyString(), anyString());
		verify(s3Client).putObject(anyString(), anyString(),
				any(InputStream.class), any(ObjectMetadata.class));
		assertTrue(result);
		verifyNoMoreInteractions(amazonClient, s3Client);
	}

	@Test
	public void testPutS3ObjectThrowsException() {
		doThrow(new AmazonServiceException("SomeException")).when(s3Client).putObject(anyString(), anyString(),
				any(InputStream.class), any(ObjectMetadata.class));

		boolean result = amazonClient.putS3Object(inputStream, bucketName, keyName);

		verify(amazonClient).putS3Object(any(InputStream.class),anyString(), anyString());
		verify(s3Client).putObject(anyString(), anyString(),
				any(InputStream.class), any(ObjectMetadata.class));
		assertFalse(result);
		verifyNoMoreInteractions(amazonClient, s3Client);
	}

	@Test
	public void testGetS3ObjectUrlWithExpiration() {
		String testUrl = "SomeUrlString";
		doReturn(url).when(s3Client).generatePresignedUrl(any(GeneratePresignedUrlRequest.class));
		doReturn(testUrl).when(url).toString();

		String testRes = amazonClient.getS3ObjectUrlWithExpiration(bucketName, keyName);

		assertEquals(testUrl, testRes);
		verify(amazonClient).getS3ObjectUrlWithExpiration(anyString(), anyString());
		verify(s3Client).generatePresignedUrl(any(GeneratePresignedUrlRequest.class));
		verifyNoMoreInteractions(amazonClient, s3Client);
	}

	@Test
	public void testGetS3ObjectUrlWithExpirationThrowsException() {
		doThrow(new AmazonServiceException("SomeException")).
				when(s3Client).generatePresignedUrl(any(GeneratePresignedUrlRequest.class));

		String testRes = amazonClient.getS3ObjectUrlWithExpiration(bucketName, keyName);

		assertNull(testRes);
		verify(amazonClient).getS3ObjectUrlWithExpiration(anyString(), anyString());
		verify(s3Client).generatePresignedUrl(any(GeneratePresignedUrlRequest.class));
		verifyNoMoreInteractions(amazonClient, s3Client);
	}
}
