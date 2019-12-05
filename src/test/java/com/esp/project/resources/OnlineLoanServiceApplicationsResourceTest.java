package com.esp.project.resources;


import com.esp.project.auth.AccessTokenPrincipal;
import com.esp.project.auth.OktaOAuthAuthenticator;
import com.esp.project.auth.RoleAuthorization;
import com.esp.project.client.ApplicationClient;
import com.esp.project.models.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.okta.jwt.JoseException;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerifier;
import com.okta.jwt.impl.DefaultJwt;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static com.esp.project.util.Utils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class OnlineLoanServiceApplicationsResourceTest {

    private final String applicationPath = "/applications";

    private String firstName = "testFirstName";
    private String lastName = "testLastName";
    private String userId = "testUserId";
    private String email = "testEmail";
    private long loanId = 123L;
    private LoanType loanType = LoanType.EDUCATION;
    private LoanStatus loanStatus = LoanStatus.NEW;
    private double requestLoanAmount = 123.456D;
    private double salary = 1234567.50D;
    private int creditScore = 123;
    private String companyName = "testCompany";
    private String testFileName = "tetFileName";

    private static final ApplicationClient APPLICATION_CLIENT = mock(ApplicationClient.class);

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static final JwtVerifier JWT_VERIFIER = mock(JwtVerifier.class);

    private final Jwt jwt = new DefaultJwt("TOKEN", Instant.EPOCH, Instant.EPOCH,
            ImmutableMap.of("name", "testName",
                    "email", email,
                    "groups", Arrays.asList("Everyone", "loanAppAdmin"),
                    "uid", userId));

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addProvider(new AuthDynamicFeature(new OAuthCredentialAuthFilter.Builder<AccessTokenPrincipal>()
                    .setAuthenticator(new OktaOAuthAuthenticator(JWT_VERIFIER))
                    .setAuthorizer(new RoleAuthorization())
                    .setRealm("SUPER SECRET STUFF")
                    .setPrefix("Bearer")
                    .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(AccessTokenPrincipal.class))
            .addResource(new OnlineLoanServiceApplicationsResource(APPLICATION_CLIENT))
            .addProvider(MultiPartFeature.class)
            .build();

//    @Path("/users/{userId}/loans/new") // New loan Application
//    @POST
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    public Response newLoanApplication(@PathParam("userId") final String userId,
//                                       @FormDataParam("loanData") final LoanData loanData,
//                                       @FormDataParam("files") final List<FormDataBodyPart> bodyParts) {
//        try {
//            if (loanData != null) {
//                LoanData newLoanData = applicationClient.newLoanApplication(userId, loanData,
//                        bodyParts);
//                return Response.ok(newLoanData).build();
//            } else {
//                return badRequest("NewApplicationLoan exception", "loanData");
//            }
//        } catch (Exception ex) {
//            return exception("NewApplicationLoan exception", ex);
//        }
//    }

    @Test
    public void testNewLoanApplication() throws JoseException, IOException {
        doReturn(jwt).when(JWT_VERIFIER).decodeAccessToken(anyString());
        doReturn(getTestLoanData()).when(APPLICATION_CLIENT).newLoanApplication(anyString(),
                any(LoanData.class), any());

        LoanData loanData = getTestLoanData();
        final FormDataMultiPart multiPart = new FormDataMultiPart()
                .field("loanData", loanData, MediaType.APPLICATION_JSON_TYPE)
                .field("file", "someTest");

        final Response response = resources
                .target(applicationPath)
                .path("/users/" + userId +"/loans/new")
                .register(MultiPartFeature.class)
                .request()
                .header("Authorization", "Bearer TOKEN")
                .post(Entity.entity(multiPart, multiPart.getMediaType()));

        assertEquals(200, response.getStatus());
        String body = response.readEntity(String.class);
        LoanData apiResponse = MAPPER.readValue(body, LoanData.class);
        assertEquals(getTestLoanData(), apiResponse);
    }

    @Test
    public void testGetLoanApplication() throws JoseException, IOException {
        doReturn(jwt).when(JWT_VERIFIER).decodeAccessToken(anyString());
        doReturn(getTestLoanData()).when(APPLICATION_CLIENT).getLoanApplication(anyLong());

        final Response response = resources
                .target(applicationPath)
                .path("/users/" + userId + "/loans/" + loanId)
                .request()
                .header("Authorization", "Bearer TOKEN")
                .get();
        assertEquals(200, response.getStatus());
        String body = response.readEntity(String.class);
        LoanData apiResponse = MAPPER.readValue(body, LoanData.class);
        assertEquals(getTestLoanData(), apiResponse);
    }

    @Test
    public void testGetLoanApplications() throws JoseException, IOException {
        doReturn(jwt).when(JWT_VERIFIER).decodeAccessToken(anyString());
        doReturn(Arrays.asList(getTestLoanData())).when(APPLICATION_CLIENT).getUserLoanApplications(anyString());

        final Response response = resources
                .target(applicationPath)
                .path("/users/" + userId + "/loans")
                .request()
                .header("Authorization", "Bearer TOKEN")
                .get();
        assertEquals(200, response.getStatus());
        String body = response.readEntity(String.class);
        List<LoanData> apiResponse = MAPPER.readValue(body, new TypeReference<List<LoanData>>(){});
        assertEquals(getTestLoanData(), apiResponse.get(0));
    }

    @Test
    public void testGetLoanApplicationFileNames() throws JoseException {
        doReturn(Arrays.asList(new FileData("testFileName", "testFilePath")))
                .when(APPLICATION_CLIENT).getLoanApplicationFilesWithResourceUrl(anyLong(), anyString(), anyBoolean());
        doReturn(jwt).when(JWT_VERIFIER).decodeAccessToken(anyString());
        final Response response = resources
                .target(applicationPath)
                .path("/filenames/123")
                .request()
                .header("Authorization", "Bearer TOKEN")
                .get();
        assertEquals(200, response.getStatus());
    }

    private LoanData getTestLoanData() {
        return new LoanData.LoanDataBuilder()
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
                .build();
    }

}
