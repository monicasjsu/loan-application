package com.esp.project.client;

import com.esp.project.db.Queries;
import com.esp.project.models.FileData;
import com.esp.project.models.LoanData;
import com.esp.project.models.LoanStatus;
import com.esp.project.models.LoanType;
import com.esp.project.models.User;
import com.esp.project.models.UserRole;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ApplicationClientTest {

	private long loanId = 123L;
	private String firstName = "testFirstName";
	private String lastName = "testLastName";
	private LoanType loanType = LoanType.EDUCATION;
	private LoanStatus loanStatus = LoanStatus.NEW;
	private double requestLoanAmount = 123.456D;
	private String userId = "testUserId";
	private double salary = 1234567.50D;
	private int creditScore = 123;
	private String companyName = "testCompany";
	private String testFileName = "tetFileName";
	private String email = "testEmail";

	@Mock
	private AmazonClient amazonClient;
	@Mock
	private Queries queries;
	@Mock
	private FormDataBodyPart formDataBodyPart;
	@Mock
	private BodyPartEntity entity;
	@Mock
	private ContentDisposition contentDisposition;
	@Mock
	private InputStream inputStream;

	private ApplicationClient applicationClient;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		applicationClient = spy(new ApplicationClient(amazonClient, queries));
	}

	@Test
	public void testNewLoanApplication() {

		doReturn(loanId).when(queries).newLoanApplicationQuery(anyString(), any(LoanType.class),
				anyDouble(), anyDouble(), anyInt(), anyString());
		doReturn(1 /*someInt*/).when(queries).newLoanFileQuery(anyString(), anyLong(), anyString(),
				anyString(), anyString());
		doReturn(entity).when(formDataBodyPart).getEntity();
		doReturn(contentDisposition).when(formDataBodyPart).getContentDisposition();
		doReturn(testFileName).when(contentDisposition).getFileName();
		doReturn(true).when(applicationClient).saveFile(any(InputStream.class), anyString());
		doReturn(inputStream).when(entity).getInputStream();

		LoanData result = applicationClient.newLoanApplication(userId, getTestNewLoanData(),
				Arrays.asList(formDataBodyPart));

		verify(applicationClient).newLoanApplication(anyString(), any(LoanData.class), anyList());
		verify(queries).newLoanApplicationQuery(anyString(), any(LoanType.class),
				anyDouble(), anyDouble(), anyInt(), anyString());
		verify(formDataBodyPart).getEntity();
		verify(formDataBodyPart).getContentDisposition();
		verify(contentDisposition).getFileName();
		verify(queries).newLoanFileQuery(anyString(), anyLong(), anyString(),
				anyString(), anyString());

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		verify(applicationClient).saveFile(any(InputStream.class), argumentCaptor.capture());
		assertEquals(String.join("_", userId, String.valueOf(loanId), testFileName), argumentCaptor.getValue());
		assertEquals(loanId, result.getLoanId());
		assertEquals(LoanStatus.NEW, result.getLoanStatus());
		verifyNoMoreInteractions(amazonClient, queries);
	}

	@Test
	public void testNewUser() {
		doReturn(1).when(queries).newUserQuery(anyString(), any(UserRole.class), anyString(),
				anyString(),anyString(),anyString(),anyString());

		applicationClient.newUser(getTestUser());

		verify(applicationClient).newUser(any(User.class));
		verify(queries).newUserQuery(anyString(), any(UserRole.class), anyString(),
				anyString(),anyString(),anyString(),anyString());

		verifyNoMoreInteractions(amazonClient, queries);
	}

	@Test
	public void testGetUserLoanApplications() {
		doReturn(Arrays.asList(getTestLoanData())).when(queries).selectUserLoanApplications(anyString());

		List<LoanData> res = applicationClient.getUserLoanApplications(userId);

		assertTrue(res.size() > 0);
		verify(applicationClient).getUserLoanApplications(anyString());
		verify(queries).selectUserLoanApplications(anyString());
		verifyNoMoreInteractions(amazonClient, queries);
		verifyNoMoreInteractions(amazonClient, queries);
	}

	@Test
	public void testGetLoanApplication() {
		doReturn(getTestLoanData()).when(queries).selectLoanApplication(loanId);

		LoanData loanData = applicationClient.getLoanApplication(loanId);

		assertNotNull(loanData);
		assertEquals(loanId, loanData.getLoanId());
		verify(applicationClient).getLoanApplication(loanId);
		verify(queries).selectLoanApplication(loanId);
		verifyNoMoreInteractions(amazonClient, queries);
	}

	@Test
	public void testGetLoanApplicationsWithLoanTypeAndStatus() {
		doReturn(Arrays.asList(getTestLoanData())).when(queries).selectLoanApplicationsWithTypeAndStatus(
				anyString(), anyString(), anyInt());

		List<LoanData> res = applicationClient.getLoanApplications(LoanType.EDUCATION, LoanStatus.NEW);

		assertTrue(res.size() > 0);
		verify(queries).selectLoanApplicationsWithTypeAndStatus(
				anyString(), anyString(), anyInt());
		verify(applicationClient).getLoanApplications(LoanType.EDUCATION, LoanStatus.NEW);
		verifyNoMoreInteractions(applicationClient, queries);
	}

	@Test
	public void testGetLoanApplicationsWithLoanType() {
		doReturn(Arrays.asList(getTestLoanData())).when(queries).selectLoanApplicationsWithType(
				anyString(), anyInt());

		List<LoanData> res = applicationClient.getLoanApplications(LoanType.EDUCATION, null);

		assertTrue(res.size() > 0);
		verify(queries).selectLoanApplicationsWithType(
				anyString(), anyInt());
		verify(applicationClient).getLoanApplications(LoanType.EDUCATION, null);
		verifyNoMoreInteractions(applicationClient, queries);
	}

	@Test
	public void testGetLoanApplicationsWithLoanStatus() {
		doReturn(Arrays.asList(getTestLoanData())).when(queries).selectLoanApplicationsWithStatus(
				anyString(), anyInt());

		List<LoanData> res = applicationClient.getLoanApplications(null, LoanStatus.NEW);

		assertTrue(res.size() > 0);
		verify(queries).selectLoanApplicationsWithStatus(
				anyString(), anyInt());
		verify(applicationClient).getLoanApplications(null, LoanStatus.NEW);
		verifyNoMoreInteractions(applicationClient, queries);
	}

	@Test
	public void testGetLoanApplicationsWithNOLoanStatusAndType() {
		doReturn(Arrays.asList(getTestLoanData())).when(queries).selectLoanApplications(anyInt());

		List<LoanData> res = applicationClient.getLoanApplications(null, null);

		assertTrue(res.size() > 0);
		verify(queries).selectLoanApplications(anyInt());
		verify(applicationClient).getLoanApplications(null, null);
		verifyNoMoreInteractions(applicationClient, queries);
	}

	@Test
	public void testGetLoanApplicationFilesWithResourceUrlForApprover() {
		String fileTempUrl = "fileTempUrl";
		doReturn(Arrays.asList(String.join("_", userId, String.valueOf(loanId), testFileName)))
				.when(applicationClient).getLoanApplicationForApprover(loanId);
		doReturn(fileTempUrl).when(amazonClient).getS3ObjectUrlWithExpiration(anyString(), anyString());

		List<FileData> res = applicationClient.getLoanApplicationFilesWithResourceUrl(loanId, userId,
				true /*isApprover*/);

		assertNotNull(res);
		verify(applicationClient).getLoanApplicationForApprover(loanId);
		verify(amazonClient).getS3ObjectUrlWithExpiration(anyString(), anyString());
		verify(applicationClient).getLoanApplicationFilesWithResourceUrl(loanId, userId,
				true /*isApprover*/);
		verifyNoMoreInteractions(applicationClient, amazonClient);
	}

	@Test
	public void testGetLoanApplicationFilesWithResourceUrlForApplicant() {
		String fileTempUrl = "fileTempUrl";
		doReturn(Arrays.asList(String.join("_", userId, String.valueOf(loanId), testFileName)))
				.when(applicationClient).getLoanApplicationFileKeyNames(loanId, userId);
		doReturn(fileTempUrl).when(amazonClient).getS3ObjectUrlWithExpiration(anyString(), anyString());

		List<FileData> res = applicationClient.getLoanApplicationFilesWithResourceUrl(loanId, userId,
				false /*isApprover*/);

		assertNotNull(res);
		verify(applicationClient).getLoanApplicationFilesWithResourceUrl(loanId, userId, false);
		verify(amazonClient).getS3ObjectUrlWithExpiration(anyString(), anyString());
		verify(applicationClient).getLoanApplicationFileKeyNames(loanId, userId);
		verifyNoMoreInteractions(applicationClient, amazonClient);
	}

	@Test
	public void testGetLoanApplicationForApprover() {
		doReturn(Arrays.asList(testFileName)).when(queries).loanApplicationFileNameForApprover(anyLong());

		List<String> res = applicationClient.getLoanApplicationForApprover(loanId);

		assertTrue(res.size() > 0);
		assertEquals(testFileName, res.get(0));
		verify(queries).loanApplicationFileNameForApprover(anyLong());
		verify(applicationClient).getLoanApplicationForApprover(anyLong());
		verifyNoMoreInteractions(applicationClient, queries);
	}

	@Test
	public void testGetLoanApplicationFileKeyNames() {
		doReturn(Arrays.asList(testFileName)).when(queries).selectLoanApplicationFileKeyName(anyLong(), anyString());

		List<String> res = applicationClient.getLoanApplicationFileKeyNames(loanId, userId);

		assertTrue(res.size() > 0);
		assertEquals(testFileName, res.get(0));
		verify(queries).selectLoanApplicationFileKeyName(anyLong(), anyString());
		verify(applicationClient).getLoanApplicationFileKeyNames(anyLong(), anyString());
		verifyNoMoreInteractions(applicationClient, queries);
	}

	@Test
	public void testSelectUser() {
		doReturn(getTestUser()).when(queries).selectUser(anyString());

		User user = applicationClient.getUser(email);

		assertNotNull(user);
		assertEquals(user.getEmail(), email);
		verify(queries).selectUser(anyString());
		verify(applicationClient).getUser(email);
		verifyNoMoreInteractions(applicationClient, queries);
	}

	@Test
	public void testUpdateUser() {
		doReturn(1).when(queries).updateUserQuery(anyString(), any(UserRole.class), anyString(),
				anyString(), anyString(), anyString(), anyString());

		applicationClient.updateUser(getTestUser());

		verify(queries).updateUserQuery(anyString(), any(UserRole.class), anyString(),
				anyString(), anyString(), anyString(), anyString());
		verify(applicationClient).updateUser(any(User.class));
		verifyNoMoreInteractions(applicationClient, queries);
	}

	@Test
	public void testSaveFile() {
		doReturn(true).when(amazonClient).putS3Object(any(InputStream.class), anyString(), anyString());

		boolean res = applicationClient.saveFile(inputStream, "testKeyName");

		assertTrue(res);
		verify(amazonClient).putS3Object(any(InputStream.class), anyString(), anyString());
		verify(applicationClient).saveFile(any(InputStream.class), anyString());
		verifyNoMoreInteractions(applicationClient, amazonClient, queries);
	}

	@Test
	public void testUpdateLoanStatus() {
		doReturn(1).when(queries).updateLoanApplicationStatus(anyLong(), anyString());

		applicationClient.updateLoanStatus(loanId, LoanStatus.IN_PROGRESS);

		verify(queries).updateLoanApplicationStatus(anyLong(), anyString());
		verify(applicationClient).updateLoanStatus(loanId, LoanStatus.IN_PROGRESS);
		verifyNoMoreInteractions(amazonClient, applicationClient, queries);
	}

	private User getTestUser() {
		return new User.Builder()
				.withUserId(userId)
				.withFirstName(firstName)
				.withLastName(lastName)
				.withUserRole(UserRole.APPLICANT)
				.withAddress("testAdd")
				.withEmail(email)
				.withPhone("testPhone")
				.build();
	}

	private LoanData getTestLoanData() {
		return new LoanData.LoanDataBuilder()
				.withSalary(salary)
				.withCreditScore(creditScore)
				.withLoanType(loanType)
				.withRequestLoanAmount(requestLoanAmount)
				.withLoanStatus(loanStatus)
				.withUserId(userId)
				.withFirstName(firstName)
				.withLastName(lastName)
				.withCompanyName(companyName)
				.withLoanId(loanId)
				.build();
	}

	private LoanData getTestNewLoanData() {
		return new LoanData.LoanDataBuilder()
				.withSalary(salary)
				.withCreditScore(creditScore)
				.withLoanType(loanType)
				.withRequestLoanAmount(requestLoanAmount)
				.withUserId(userId)
				.withFirstName(firstName)
				.withLastName(lastName)
				.withCompanyName(companyName)
				.build();
	}
}
