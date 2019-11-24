package com.esp.project.client;

import com.esp.project.db.Queries;
import com.esp.project.models.FileData;
import com.esp.project.models.LoanData;
import com.esp.project.models.LoanStatus;
import com.esp.project.models.LoanType;
import com.esp.project.models.User;
import com.google.common.base.Strings;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.NotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.esp.project.client.AmazonClient.BUCKET_NAME;

public class ApplicationClient {

	private static final int FETCH_SIZE = 50;

	private final AmazonClient amazonClient;
	private final Queries dbQueries;

	public ApplicationClient(final AmazonClient amazonClient,
                             final Queries queries) {
		this.amazonClient = amazonClient;
		this.dbQueries = queries;
	}

	public LoanData newLoanApplication(final String userId,
	                                   final LoanData loanData,
	                                   final List<FormDataBodyPart> bodyParts,
	                                   final FormDataContentDisposition fileDispositions) {
		long loanId = dbQueries.newLoanApplicationQuery(userId, loanData.getLoanType(),
				loanData.getRequestLoanAmount(), loanData.getSalary(), loanData.getCreditScore(),
				loanData.getCompanyName());
		if (loanId != 0) {
			for (int i = 0; i < bodyParts.size(); i++) {
				/*
				 * Casting FormDataBodyPart to BodyPartEntity, which can give us
				 * InputStream for uploaded file
				 */
				final BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
				final String fileName = bodyParts.get(i).getContentDisposition().getFileName();
				final String fileKeyName = String.join("_", userId, String.valueOf(loanId), fileName);
				final boolean isUploadSuccessful = saveFile(bodyPartEntity.getInputStream(), fileKeyName);
				if (isUploadSuccessful) { // Can do retries fi need be
					final String uuid = UUID.randomUUID().toString();
					dbQueries.newLoanFileQuery(uuid, loanId, fileKeyName, fileName);

				}
			}
		}
		loanData.setLoanId(loanId);
		loanData.setLoanStatus(LoanStatus.NEW);

		return loanData;
//		final LoanData res = new LoanData.LoanDataBuilder()
//				.withLoanId(loanId)
//				.withLoanType(loanData.getLoanType())
//				.withLoanStatus(LoanStatus.NEW)
//				.withRequestLoanAmount(loanData.getRequestLoanAmount())
//				.withSalary(loanData.getSalary())
//				.withCreditScore(loanData.getCreditScore())
//				.build();
//		return res;
	}

	public void newUser(final User user) {
		dbQueries.newUserQuery(user.getUserId(), user.getUserRole(), user.getFirstName(),
				user.getLastName(), user.getEmail(), user.getPhone(), user.getAddress());
	}

	public List<LoanData> getUserLoanApplications(final String userId) {
		return dbQueries.selectUserLoanApplications(userId);
	}

	public LoanData getLoanApplication(final long loanId) {
		return dbQueries.selectLoanApplication(loanId);
	}

	public List<LoanData> getLoanApplications(final LoanType loanType, final  LoanStatus loanStatus) {
		if (loanStatus != null && loanType != null) {
			return dbQueries.selectLoanApplicationsWithTypeAndStatus(loanType.getLoanType(),
					loanStatus.getLoanStatus(), FETCH_SIZE);
		} else if (loanStatus != null) {
			return dbQueries.selectLoanApplicationsWithStatus(loanStatus.getLoanStatus(), FETCH_SIZE);
		} else if (loanType != null) {
			return dbQueries.selectLoanApplicationsWithType(loanType.getLoanType(), FETCH_SIZE);
		}
		return dbQueries.selectLoanApplications(FETCH_SIZE /* limit */);
	}

	public List<FileData> getLoanApplicationFilesWithResourceUrl(final long loanId) {
		final List<String> fileKeyNames = getLoanApplicationFileKeyNames(loanId);
		final List<FileData> fileDataList = new ArrayList<>();
		for (String fileKeyName : fileKeyNames) {
			fileDataList.add(new FileData(fileKeyName.substring(fileKeyName.indexOf("_") + 1),
					amazonClient.getS3ObjectUrlWithExpiration(BUCKET_NAME, fileKeyName)));
		}
		return fileDataList;
	}

	public List<String> getLoanApplicationFileKeyNames(final long loanId) {
		return dbQueries.selectLoanApplicationFileKeyName(loanId);
	}

	public User selectUser(final String email) {
		if (!Strings.isNullOrEmpty(email)) {
			return dbQueries.selectUser(email);
		}
		throw new NotFoundException("Not an existing user");
	}

	public int userUpdate(final User user) {
		return dbQueries.updateUserQuery(user.getUserId(), user.getUserRole(), user.getFirstName(),
				user.getLastName(), user.getEmail(), user.getPhone(), user.getAddress());
	}

	private boolean saveFile(final InputStream file, final String keyName) {
		return amazonClient.putS3Object(file, "", keyName);
	}

	private String getLoanApplicationUserId(final long loanId) {
		return dbQueries.selectLoanApplicationUserId(loanId);
	}

	public int updateLoanStatus(final long loanId, final LoanStatus loanStatus) {
		return dbQueries.updateLoanApplicationStatus(loanId,loanStatus.getLoanStatus());
	}
}
