package com.esp.project.db.mappers;


import com.esp.project.models.LoanData;
import com.esp.project.models.LoanStatus;
import com.esp.project.models.LoanType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class LoanDataMapperTest {

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
	private ResultSet resultSet;
	@Mock
	private StatementContext statementContext;

	private LoanDataMapper loanDataMapper;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		loanDataMapper = spy(new LoanDataMapper());
	}

	@Test
	public void testMap() throws SQLException {
		doReturn(loanId).when(resultSet).getLong("id");
		doReturn(firstName).when(resultSet).getString("firstname");
		doReturn(lastName).when(resultSet).getString("lastname");
		doReturn(userId).when(resultSet).getString("uid");
		doReturn(loanType.getLoanType()).when(resultSet).getString("loan_type");
		doReturn(loanStatus.getLoanStatus()).when(resultSet).getString("loan_status");
		doReturn(requestLoanAmount).when(resultSet).getDouble("request_amount");
		doReturn(creditScore).when(resultSet).getInt("credit_score");
		doReturn(salary).when(resultSet).getDouble("salary");
		doReturn(new Timestamp(System.currentTimeMillis())).when(resultSet).getTimestamp("created");
		doReturn(new Timestamp(System.currentTimeMillis())).when(resultSet).getTimestamp("last_updated");

		LoanData loanData = loanDataMapper.map(0, resultSet, statementContext);

		assertEquals(loanId, loanData.getLoanId());
		assertEquals(userId, loanData.getUserId());
		assertEquals(loanType, loanData.getLoanType());
		assertEquals(loanStatus, loanData.getLoanStatus());

		verify(loanDataMapper).map(0, resultSet, statementContext);
		verifyNoMoreInteractions(loanDataMapper);
	}

	@Test(expected = SQLException.class)
	public void testMapWithException() throws SQLException{
		doThrow(new SQLException("Some Ex")).when(resultSet).getLong("id");

		loanDataMapper.map(0, resultSet, statementContext);

		verifyNoMoreInteractions(loanDataMapper);
	}
}
