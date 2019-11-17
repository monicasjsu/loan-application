package com.esp.project.db.mappers;

import com.esp.project.models.LoanData;
import com.esp.project.models.LoanStatus;
import com.esp.project.models.LoanType;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoanDataMapper implements ResultSetMapper<LoanData> {

	public LoanData map(final int index, final ResultSet r, final StatementContext ctx) throws SQLException {
		return new LoanData.LoanDataBuilder()
				.withLoanId(r.getLong("id"))
				.withUserId(r.getString("uid"))
				.withLoanType(LoanType.fromString(r.getString("loan_type")))
				.withLoanStatus(LoanStatus.fromString(r.getString("loan_status")))
				.withRequestLoanAmount(r.getDouble("request_amount"))
				.withCreditScore(r.getInt("credit_score"))
				.withSalary(r.getDouble("salary"))
				.withCreated(r.getTimestamp("created"))
				.withLastUpdated(r.getTimestamp("last_updated"))
				.build();
	}
}
