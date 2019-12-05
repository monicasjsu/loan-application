package com.esp.project.resources;

import com.esp.project.client.ApplicationClient;
import com.esp.project.models.LoanData;
import com.esp.project.models.LoanStatus;
import com.esp.project.models.LoanType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;



public class OnlineLoanServiceApprovalResourceTest {

	private final String approvalPath = "/approval";

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


	private long loanId2 = 456L;
	private String firstName2 = "testFirstName2";
	private String lastName2 = "testLastName2";
	private LoanType loanType2 = LoanType.AUTOMOBILE;
	private LoanStatus loanStatus2 = LoanStatus.IN_PROGRESS;
	private double requestLoanAmount2 = 12345.456D;
	private String userId2 = "testUserId";
	private double salary2 = 1234562334.50D;
	private int creditScore2 = 500;
	private String companyName2 = "testCompany2";

	private static final ApplicationClient APPLICATION_CLIENT = mock(ApplicationClient.class);
	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder()
			.addResource(new OnlineLoanServiceApprovalResource(APPLICATION_CLIENT))
			.build();

	@Test
	public void testGetLoanApplications() throws IOException {
		List<LoanData> result = getTestLoanDatas();
		doReturn(getTestLoanDatas()).when(APPLICATION_CLIENT)
				.getLoanApplications(null, null);
		Response res = resources.target(approvalPath).path("/applications").request().get();
		assertEquals(200, res.getStatus());
		final String body = res.readEntity(String.class);
		List<LoanData> apiResponse = MAPPER.readValue(body, new TypeReference<List<LoanData>>(){});

		assertEquals(apiResponse.get(0), result.get(0));
		assertEquals(apiResponse.get(1), result.get(1));
	}

	@Test
	public void testUpdateStatus() {
		doReturn(1).when(APPLICATION_CLIENT)
				.updateLoanStatus(123, LoanStatus.NEW);
		Response res = resources
				.target(approvalPath)
				.path("123/updateLoanStatus")
				.queryParam("status", LoanStatus.NEW)
				.request().get();
		assertEquals(200, res.getStatus());
	}

	private List<LoanData> getTestLoanDatas() {
		return Arrays.asList(
				new LoanData.LoanDataBuilder()
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
						.build(),

				new LoanData.LoanDataBuilder()
						.withSalary(salary2)
						.withCreditScore(creditScore2)
						.withLoanType(loanType2)
						.withRequestLoanAmount(requestLoanAmount2)
						.withLoanStatus(loanStatus2)
						.withUserId(userId2)
						.withFirstName(firstName2)
						.withLastName(lastName2)
						.withCompanyName(companyName2)
						.withLoanId(loanId2)
						.build());

	}
}
