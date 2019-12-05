package com.esp.project.db;

import com.esp.project.db.mappers.LoanDataMapper;
import com.esp.project.db.mappers.UserMapper;
import com.esp.project.models.LoanData;
import com.esp.project.models.LoanType;
import com.esp.project.models.User;
import com.esp.project.models.UserRole;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

public interface Queries {

	@SqlUpdate("INSERT INTO file_temp " +
			"(id, lid, file_key_name, file_name, user_id) " +
			"VALUES (:id, :lid, :file_key_name, :file_name, :user_id);")
	int newLoanFileQuery(@Bind("id") String uuid,
	                     @Bind("lid") long loanId,
	                     @Bind("user_id") String user_id,
	                     @Bind("file_key_name") String fileKeyName,
	                     @Bind("file_name") String fileName);

	@GetGeneratedKeys
	@SqlUpdate("INSERT INTO loan_temp " +
			"(uid, loan_type, request_amount, salary, credit_score, company_name) " +
			"VALUES (:uid, :loan_type, :request_amount, :salary, :credit_score, :company_name);")
	long newLoanApplicationQuery(@Bind("uid") String uid,
	                            @Bind("loan_type") LoanType loanType,
	                            @Bind("request_amount") double requestAmount,
	                            @Bind("salary") double salary,
	                            @Bind("credit_score") int creditScore,
	                            @Bind("company_name") String companyName);

	@SqlUpdate("INSERT INTO user_temp " +
			"(id, user_role, firstname, lastname, email, phone, address) " +
			"VALUES (:userId, :user_role, :firstname, :lastname, :email, :phone, :address);")
	int newUserQuery(@Bind("userId") String userId,
	                    @Bind("user_role") UserRole userRole,
	                    @Bind("firstname") String firstName,
	                    @Bind("lastname") String lastName,
	                    @Bind("email") String email,
	                    @Bind("phone") String phone,
	                    @Bind("address") String address);


	@SqlUpdate("UPDATE user_temp " +
			"SET user_role = :user_role, firstname = :firstname, lastname = :lastname, phone = :phone, address = :address " +
			"where id = :userId;")
	int updateUserQuery(@Bind("userId") String userId,
						 @Bind("user_role") UserRole userRole,
						 @Bind("firstname") String firstName,
						 @Bind("lastname") String lastName,
						 @Bind("email") String email,
						 @Bind("phone") String phone,
						 @Bind("address") String address);


	@RegisterMapper(UserMapper.class)
	@SqlQuery("select * from user_temp")
	List<User> selectUsers();

	@RegisterMapper(UserMapper.class)
	@SqlQuery("select * from user_temp where email = :email")
	User selectUser(@Bind("email") String email);

	@SqlQuery("select id from user_temp where id = :userId")
	String containsUser(@Bind("userId") String userId);

	@RegisterMapper(LoanDataMapper.class)
	@SqlQuery("SELECT loan_temp.id, loan_temp.uid, loan_temp.loan_type, loan_temp.loan_status,"+
			"loan_temp.request_amount,loan_temp.salary,loan_temp.credit_score,loan_temp.company_name,"+
			"loan_temp.created, loan_temp.last_updated, user_temp.firstname, user_temp.lastname "+
			"FROM loan_temp "+
			"INNER JOIN user_temp " +
			"ON loan_temp.uid = user_temp.id "+
			"WHERE loan_temp.uid = :uid")
	List<LoanData> selectUserLoanApplications(@Bind("uid") String uid);

	@RegisterMapper(LoanDataMapper.class)
	@SqlQuery("SELECT loan_temp.id, loan_temp.uid, loan_temp.loan_type, loan_temp.loan_status,"+
			"loan_temp.request_amount,loan_temp.salary,loan_temp.credit_score,loan_temp.company_name,"+
			"loan_temp.created, loan_temp.last_updated, user_temp.firstname, user_temp.lastname "+
			"FROM loan_temp "+
			"INNER JOIN user_temp " +
			"ON loan_temp.uid = user_temp.id "+
			"WHERE loan_temp.loan_type=:loan_type and loan_temp.loan_status=:loan_status limit :size;")
	List<LoanData> selectLoanApplicationsWithTypeAndStatus(@Bind("loan_type") String loanType,
	                                                       @Bind("loan_status") String loanStatus,
	                                                       @Bind("size") int size);

	@RegisterMapper(LoanDataMapper.class)
	@SqlQuery("SELECT loan_temp.id, loan_temp.uid, loan_temp.loan_type, loan_temp.loan_status,"+
			"loan_temp.request_amount,loan_temp.salary,loan_temp.credit_score,loan_temp.company_name,"+
			"loan_temp.created, loan_temp.last_updated, user_temp.firstname, user_temp.lastname "+
			"from loan_temp "+
			"INNER JOIN user_temp " +
			"ON loan_temp.uid = user_temp.id " +
			"WHERE loan_temp.loan_type=:loan_type limit :size;")
	List<LoanData> selectLoanApplicationsWithType(@Bind("loan_type") String loanType,
	                                              @Bind("size") int size);

	@RegisterMapper(LoanDataMapper.class)
	@SqlQuery("SELECT loan_temp.id, loan_temp.uid, loan_temp.loan_type, loan_temp.loan_status,"+
			"loan_temp.request_amount,loan_temp.salary,loan_temp.credit_score,loan_temp.company_name,"+
			"loan_temp.created, loan_temp.last_updated, user_temp.firstname, user_temp.lastname "+
			"from loan_temp "+
			"INNER JOIN user_temp " +
			"ON loan_temp.uid = user_temp.id "+
			"WHERE loan_temp.loan_status=:loan_status limit :size;")
	List<LoanData> selectLoanApplicationsWithStatus(@Bind("loan_status") String loanStatus,
	                                                @Bind("size") int size);

    @RegisterMapper(LoanDataMapper.class)
	@SqlQuery("SELECT loan_temp.id, loan_temp.uid, loan_temp.loan_type, loan_temp.loan_status,"+
			"loan_temp.request_amount,loan_temp.salary,loan_temp.credit_score,loan_temp.company_name,"+
			"loan_temp.created, loan_temp.last_updated, user_temp.firstname, user_temp.lastname "+
			"FROM loan_temp "+
			"INNER JOIN user_temp " +
			"ON loan_temp.uid=user_temp.id;")
	List<LoanData> selectLoanApplications(@Bind("size") int size);

	@RegisterMapper(LoanDataMapper.class)
	@SqlQuery("SELECT loan_temp.id, loan_temp.uid, loan_temp.loan_type, loan_temp.loan_status,"+
			"loan_temp.request_amount,loan_temp.salary,loan_temp.credit_score,loan_temp.company_name,"+
			"loan_temp.created, loan_temp.last_updated, user_temp.firstname, user_temp.lastname "+
			"FROM loan_temp "+
			"INNER JOIN user_temp " +
			"ON loan_temp.uid=user_temp.id " +
			"WHERE loan_temp.id = :id")
	LoanData selectLoanApplication(@Bind("id") long lid);

	@SqlQuery("select file_key_name from file_temp where lid= :lid")
	List<String> loanApplicationFileNameForApprover(@Bind("lid") long lid);

	@SqlQuery("select file_key_name from file_temp where lid= :lid and user_id = :user_id")
	List<String> selectLoanApplicationFileKeyName(@Bind("lid") long lid ,
												  @Bind("user_id") String user_id);

	@SqlQuery("select uid from loan_temp where id= :id")
	String selectLoanApplicationUserId(@Bind("id") long lid);

	@SqlUpdate("update loan_temp set loan_status = :loan_status where id = :id" )
	int updateLoanApplicationStatus(@Bind("id") long lid,
									@Bind("loan_status") String loanStatus);
}
