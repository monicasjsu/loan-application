package com.esp.project.db;

import com.esp.project.db.mappers.LoanDataMapper;
import com.esp.project.db.mappers.UserMapper;
import com.esp.project.models.LoanData;
import com.esp.project.models.LoanType;
import com.esp.project.models.User;
import com.esp.project.models.UserRole;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

public interface Queries {

	@SqlUpdate("INSERT INTO file_temp " +
			"(id, lid, file_key_name, file_name) " +
			"VALUES (:id, :lid, :file_key_name, :file_name);")
	int newLoanFileQuery(@Bind("id") String uuid,
	                     @Bind("lid") long loanId,
	                     @Bind("file_key_name") String fileKeyName,
	                     @Bind("file_name") String fileName);

	@GetGeneratedKeys
	@SqlUpdate("INSERT INTO loan_temp " +
			"(uid, loan_type, request_amount, salary, credit_score, company_name) " +
			"VALUES (:uid, :loan_type, :request_amount, :salary, :credit_score, :company_name);")
	int newLoanApplicationQuery(@Bind("uid") String uid,
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

	@RegisterMapper(UserMapper.class)
	@SqlQuery("select * from user_temp")
	List<User> selectUsers();

	@RegisterMapper(UserMapper.class)
	@SqlQuery("select * from user_temp where id = :id")
	User selectUser(@Bind("id") long id);

	@SqlQuery("select id from user_temp where id = :userId")
	String containsUser(@Bind("userId") String userId);

	@RegisterMapper(LoanDataMapper.class)
	@SqlQuery("select * from loan_temp where uid = :uid")
	List<LoanData> selectUserLoanApplications(@Bind("uid") String uid);

	@RegisterMapper(LoanDataMapper.class)
	@SqlQuery("select * from loan_temp where loan_type= :loan_type and loan_status = :loan_status limit = :size")
	List<LoanData> selectLoanApplicationsWithTypeAndStatus(@Bind("loan_type") String loanType,
	                                                       @Bind("loan_status") String loanStatus,
	                                                       @Bind("size") int size);

	@RegisterMapper(LoanDataMapper.class)
	@SqlQuery("select * from loan_temp where loan_type= :loan_type limit :size")
	List<LoanData> selectLoanApplicationsWithType(@Bind("loan_type") String loanType,
	                                              @Bind("size") int size);

	@RegisterMapper(LoanDataMapper.class)
	@SqlQuery("select * from loan_temp where loan_status = :loan_status limit :size")
	List<LoanData> selectLoanApplicationsWithStatus(@Bind("loan_status") String loanStatus,
	                                                @Bind("size") int size);

    @RegisterMapper(LoanDataMapper.class)
	@SqlQuery("select * from loan_temp limit :size")
	List<LoanData> selectLoanApplications(@Bind("size") int size);

	@RegisterMapper(LoanDataMapper.class)
	@SqlQuery("select * from loan_temp where id = :id")
	LoanData selectLoanApplication(@Bind("id") long lid);

	@SqlQuery("select file_name from file_temp where lid= :lid")
	List<String> selectLoanApplicationFileName(@Bind("lid") long lid);

	@SqlQuery("select file_key_name from file_temp where lid= :lid")
	List<String> selectLoanApplicationFileKeyName(@Bind("lid") long lid);

	@SqlQuery("select uid from loan_temp where id= :id")
	String selectLoanApplicationUserId(@Bind("id") long lid);
}
