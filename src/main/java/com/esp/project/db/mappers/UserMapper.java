package com.esp.project.db.mappers;

import com.esp.project.models.User;
import com.esp.project.models.UserRole;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements ResultSetMapper<User> {

	public User map(final int index, final ResultSet r, final StatementContext ctx) throws SQLException {
		return new User.Builder()
				.withUserId(r.getString("id"))
				.withUserRole(UserRole.fromString(r.getString("user_role")))
				.withFirstName(r.getString("firstname"))
				.withLastName(r.getString("lastname"))
				.withEmail(r.getString("email"))
				.withPhone(r.getString("phone"))
				.withAddress(r.getString("address"))
				.withCreated(r.getTimestamp("created"))
				.withCreated(r.getTimestamp("last_updated"))
				.build();
	}
}
