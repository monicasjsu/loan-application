package com.esp.project;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.esp.project.auth.AccessTokenPrincipal;
import com.esp.project.auth.OktaOAuthAuthenticator;
import com.esp.project.auth.RoleAuthorization;
import com.esp.project.client.AmazonClient;
import com.esp.project.client.ApplicationClient;
import com.esp.project.config.AwsConfig;
import com.esp.project.config.OktaOAuthConfig;
import com.esp.project.db.Queries;
import com.esp.project.resources.OnlineLoanServiceApplicationsResource;
import com.esp.project.resources.OnlineLoanServiceApprovalResource;
import com.esp.project.resources.OnlineLoanServiceAuthResource;
import com.esp.project.resources.OnlineLoanServiceHomeResource;
import com.esp.project.resources.OnlineLoanServiceUserResource;
import com.okta.jwt.JwtHelper;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;

public class OnlineLoanServiceApplication extends Application<OnlineLoanServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new OnlineLoanServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "OnlineLoanService";
    }

    @Override
    public void initialize(final Bootstrap<OnlineLoanServiceConfiguration> bootstrap) {
        bootstrap.addBundle(new MultiPartBundle());
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addBundle(new DBIExceptionsBundle());
    }

    @Override
    public void run(final OnlineLoanServiceConfiguration configuration,
                    final Environment env) {
        // TODO: implement application

        final AmazonS3 amazonS3 = getS3Client(configuration);
        final AmazonClient amazonClient = new AmazonClient(amazonS3);

        final DBIFactory factory = new DBIFactory();
        final DBI database = factory.build(env, configuration.getDataSourceFactory(), "mysql");
	    final Queries dbQueries = database.onDemand(Queries.class);

        final ApplicationClient applicationClient = new ApplicationClient(amazonClient, dbQueries);

        configureOAuth(configuration, env);

        env.jersey().setUrlPattern("/api/*");

        env.jersey().register(new OnlineLoanServiceHomeResource());
        env.jersey().register(new OnlineLoanServiceApplicationsResource(applicationClient));
        env.jersey().register(new OnlineLoanServiceApprovalResource(applicationClient));
        env.jersey().register(new OnlineLoanServiceAuthResource(configuration.getAuthConfig()));
        env.jersey().register(new OnlineLoanServiceUserResource(applicationClient));

    }

    private AmazonS3 getS3Client(final OnlineLoanServiceConfiguration configuration) {
        final Regions clientRegion = Regions.US_WEST_1;
        final AwsConfig awsConfig = configuration.getAwsConfig();
        final BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsConfig.getAccessKey(),
                awsConfig.getSecretKey());
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(clientRegion)
                .build();
    }

    private void configureOAuth(final OnlineLoanServiceConfiguration configuration,
                                final Environment environment) {
        try {
            final OktaOAuthConfig widgetConfig = configuration.getAuthConfig();
            // Configure the JWT Validator, it will validate Okta's JWT access tokens
            final JwtHelper helper = new JwtHelper()
                    .setIssuerUrl(widgetConfig.getIssuer())
                    .setClientId(widgetConfig.getClientId());

            // set the audience only if set, otherwise the default is: api://default
            final String audience = widgetConfig.getAudience();
            if (StringUtils.isNotEmpty(audience)) {
                helper.setAudience(audience);
            }

            // register the OktaOAuthAuthenticator
            environment.jersey().register(new AuthDynamicFeature(
                    new OAuthCredentialAuthFilter.Builder<AccessTokenPrincipal>()
                            .setAuthenticator(new OktaOAuthAuthenticator(helper.build()))
                            .setAuthorizer(new RoleAuthorization())
                            .setPrefix("Bearer")
                            .buildAuthFilter()));

            // Bind our custom principal to the @Auth annotation
            environment.jersey().register(new AuthValueFactoryProvider.Binder<>(AccessTokenPrincipal.class));
            // To use the annotation like @PermitAll and @RolesAllowed("")
            environment.jersey().register(RolesAllowedDynamicFeature.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to configure JwtVerifier", e);
        }
    }

}
