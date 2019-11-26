package com.esp.project.resources;

import com.codahale.metrics.annotation.Timed;
import com.esp.project.client.ApplicationClient;
import com.esp.project.models.LoanData;
import com.esp.project.models.LoanStatus;
import com.esp.project.models.LoanType;
import org.jvnet.hk2.annotations.Optional;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.esp.project.util.Utils.exception;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Timed
@Path("/approval")
@RolesAllowed("LoanAppAdmin")
public class OnlineLoanServiceApprovalResource {

	private final ApplicationClient applicationClient;

	public OnlineLoanServiceApprovalResource(final ApplicationClient applicationClient) {
		this.applicationClient = applicationClient;
	}

	@Path("/applications")
	@GET
	public Response getLoanApplications(@Optional @QueryParam("type") final LoanType loanType,
                                        @Optional @QueryParam("status") final LoanStatus loanStatus) {
		try {
			final List<LoanData> loanDataList = applicationClient.getLoanApplications(loanType, loanStatus);
			return Response.ok(loanDataList).build();
		} catch (Exception ex) {
			return exception("NewApplicationLoan exception", ex);
		}
	}

	@Path("{loanId}/updateLoanStatus")
	@GET
	public Response updateStatus(@QueryParam("status") final LoanStatus loanStatus, @PathParam("loanId") final long loanId) {
		try {
			applicationClient.updateLoanStatus(loanId,loanStatus);
			return Response.ok().build();
		} catch (Exception ex) {
			return exception("Error while updating the status", ex);
		}
	}
}
