package com.esp.project.db.mappers;

import com.esp.project.models.LoanData;
import com.esp.project.models.LoanStatus;
import com.esp.project.models.LoanType;
import com.esp.project.models.User;
import com.esp.project.models.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skife.jdbi.v2.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class UserMapperTest {

	private long loanId = 123L;
	private String firstName = "testFirstName";
	private String lastName = "testLastName";
	private LoanType loanType = LoanType.EDUCATION;
	private LoanStatus loanStatus = LoanStatus.NEW;
	private String userId = "testUserId";
	private UserRole userRole = UserRole.APPLICANT;
	private String phone = "testPhone";
	private String address = "testAddress";
	private String email = "testEmail";

	@Mock
	private ResultSet resultSet;
	@Mock
	private StatementContext statementContext;

	private UserMapper userMapper;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		userMapper = spy(new UserMapper());
	}

	@Test
	public void testMap() throws SQLException {
		doReturn(userId).when(resultSet).getString("id");
		doReturn(firstName).when(resultSet).getString("firstname");
		doReturn(lastName).when(resultSet).getString("lastname");
		doReturn(userRole.getUserRole()).when(resultSet).getString("user_role");
		doReturn(email).when(resultSet).getString("email");
		doReturn(phone).when(resultSet).getString("phone");
		doReturn(address).when(resultSet).getString("address");
		doReturn(new Timestamp(System.currentTimeMillis())).when(resultSet).getTimestamp("created");
		doReturn(new Timestamp(System.currentTimeMillis())).when(resultSet).getTimestamp("last_updated");

		User user = userMapper.map(0, resultSet, statementContext);

		assertEquals(email, user.getEmail());
		assertEquals(userId, user.getUserId());
		assertEquals(userRole, user.getUserRole());

		verify(userMapper).map(0, resultSet, statementContext);
		verifyNoMoreInteractions(userMapper);
	}

	@Test(expected = SQLException.class)
	public void testMapWithException() throws SQLException{
		doThrow(new SQLException("Some Ex")).when(resultSet).getString("id");

		userMapper.map(0, resultSet, statementContext);

		verifyNoMoreInteractions(userMapper);
	}
}
