package com.esp.project.resources;

import com.codahale.metrics.annotation.Timed;
import com.esp.project.auth.AccessTokenPrincipal;
import com.esp.project.client.ApplicationClient;
import com.esp.project.models.FileData;
import com.esp.project.models.LoanData;
import io.dropwizard.auth.Auth;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.esp.project.util.Utils.badRequest;
import static com.esp.project.util.Utils.exception;
import static com.esp.project.util.Utils.forbiddenRequest;

@Produces(MediaType.APPLICATION_JSON)
@Timed
@Path("/applications")
@PermitAll
public class OnlineLoanServiceApplicationsResource {

	private final ApplicationClient applicationClient;

	public OnlineLoanServiceApplicationsResource(final ApplicationClient applicationClient) {
		this.applicationClient = applicationClient;
	}

	@Path("/users/{userId}/loans/new") // New loan Application
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response newLoanApplication(@PathParam("userId") final String userId,
	                                   @FormDataParam("loanData") final LoanData loanData,
	                                   @FormDataParam("files") final List<FormDataBodyPart> bodyParts,
	                                   @FormDataParam("files") final FormDataContentDisposition fileDispositions) {
		try {
			if (loanData != null) {
				LoanData newLoanData = applicationClient.newLoanApplication(userId, loanData,
						bodyParts, fileDispositions);
				return Response.ok(newLoanData).build();
			} else {
				return badRequest("NewApplicationLoan exception", "loanData");
			}
		} catch (Exception ex) {
			return exception("NewApplicationLoan exception", ex);
		}
	}

	@Path("/users/{userId}/loans")
	@GET
	public Response getUserLoanApplications(@Auth final AccessTokenPrincipal tokenPrincipal,
	                                        @PathParam("userId") final String userId) {
		try {
			if (tokenPrincipal.getUserId().equalsIgnoreCase(userId)) {
				final List<LoanData> loanDataList = applicationClient.getUserLoanApplications(userId);
				return Response.ok(loanDataList).build();
			} else {
				return forbiddenRequest();
			}
		} catch (Exception ex) {
			return exception("NewApplicationLoan exception", ex);
		}
	}

	@Path("/users/{userId}/loans/{loanId}")
	@GET
	public Response getLoanApplication(@Auth final AccessTokenPrincipal tokenPrincipal,
	                                   @PathParam("userId") final String userId,
	                                   @PathParam("loanId") final long loanId) {
		try {
			if (tokenPrincipal.getUserId().equalsIgnoreCase(userId)) {
				final LoanData loanDataList = applicationClient.getLoanApplication(loanId);
				return Response.ok(loanDataList).build();
			} else {
				return forbiddenRequest();
			}
		} catch (Exception ex) {
			return exception("NewApplicationLoan exception", ex);
		}
	}

	@Path("/filenames/{loanId}") // See even if this needs some filtering right now user can pull all loans
	@GET
	public Response getLoanApplicationFileNames(@Auth final AccessTokenPrincipal tokenPrincipal,
	                                            @PathParam("loanId") final long loanId) {
		try {
			final String userId = tokenPrincipal.getUserId();
			final boolean isApprover = tokenPrincipal.getGroups().contains("LoanAppAdmin");
			final List<FileData> fileNames = applicationClient.getLoanApplicationFilesWithResourceUrl(loanId, userId, isApprover);
			return Response.ok(fileNames).build();
		} catch (Exception ex) {
			return exception("NewApplicationLoan exception", ex);
		}
	}



//
//
//	@Path("/buckets")
//	@GET
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	public Response getAmazonBucketsList() {
//
//		amazonClient.getS3BucketsList();
//
//		return Response.ok(of("ids", amazonClient.getS3BucketsList())).build();
//	}


}
