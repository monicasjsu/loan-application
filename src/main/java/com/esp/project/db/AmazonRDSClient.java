package com.esp.project.db;

import com.amazonaws.regions.Regions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class AmazonRDSClient {

	final Logger log = LoggerFactory.getLogger(AmazonRDSClient.class);

	//Configuration parameters for the generation of the IAM Database Authentication token
	private static final String RDS_INSTANCE_HOSTNAME = "loan-application.c0fglzqcbfzm.us-east-1.rds.amazonaws.com";
	private static final int RDS_INSTANCE_PORT = 3306;
	private static final String REGION_NAME = Regions.US_WEST_1.name();
	private static final String DB_USER = "admin";
	private static final String DB_PASSWORD = "password";
	private static final String JDBC_URL = "jdbc:mysql://" + RDS_INSTANCE_HOSTNAME + ":" + RDS_INSTANCE_PORT;
	public static final int MYSQL_BATCH_SIZE = 10000;

	private static final String SSL_CERTIFICATE = "/rds-ca-2019-us-west-1.pem";

	private static final String KEY_STORE_TYPE = "JKS";
	private static final String KEY_STORE_PROVIDER = "SUN";
	private static final String KEY_STORE_FILE_PREFIX = "sys-connect-via-ssl-test-cacerts";
	private static final String KEY_STORE_FILE_SUFFIX = ".jks";
	private static final String DEFAULT_KEY_STORE_PASSWORD = "monica";

//	public static void main(String[] args) throws Exception {
//		//get the connection
//		Connection connection = getDBConnectionUsingIam();
//
//		//verify the connection is successful
//		Statement stmt= connection.createStatement();
//		ResultSet rs=stmt.executeQuery("SELECT 'Success!' FROM DUAL;");
//		while (rs.next()) {
//			String id = rs.getString(1);
//			System.out.println(id); //Should print "Success!"
//		}
//
//		//close the connection
//		stmt.close();
//		connection.close();
//
//		clearSslProperties();
//
//	}
//
//	/**
//	 * This method returns a connection to the db instance authenticated using IAM Database Authentication
//	 * @return
//	 * @throws Exception
//	 */
//	private static Connection getDBConnectionUsingIam() throws Exception {
////		setSslProperties();
//		return DriverManager.getConnection(JDBC_URL, setMySqlConnectionProperties());
//	}
//
//	/**
//	 * This method sets the mysql connection properties which includes the IAM Database Authentication token
//	 * as the password. It also specifies that SSL verification is required.
//	 * @return
//	 */
//	private static Properties setMySqlConnectionProperties() {
//		Properties mysqlConnectionProperties = new Properties();
//		mysqlConnectionProperties.setProperty("verifyServerCertificate","true");
//		mysqlConnectionProperties.setProperty("useSSL", "false");
//		mysqlConnectionProperties.setProperty("user", DB_USER);
//		mysqlConnectionProperties.setProperty("password", DB_PASSWORD);
//		return mysqlConnectionProperties;
//	}
//
//	/**
//	 * This method generates the IAM Auth Token.
//	 * An example IAM Auth Token would look like follows:
//	 * btusi123.cmz7kenwo2ye.rds.cn-north-1.amazonaws.com.cn:3306/?Action=connect&DBUser=iamtestuser&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20171003T010726Z&X-Amz-SignedHeaders=host&X-Amz-Expires=899&X-Amz-Credential=AKIAPFXHGVDI5RNFO4AQ%2F20171003%2Fcn-north-1%2Frds-db%2Faws4_request&X-Amz-Signature=f9f45ef96c1f770cdad11a53e33ffa4c3730bc03fdee820cfdf1322eed15483b
//	 * @return
//	 */
//	private static String generateAuthToken() {
//		BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIATCCR6X6IGFIFGCZR",
//				"C863Flmca9aGAyzc5gF5xFwz6ZpcilHmydjcitxF");
//
//		RdsIamAuthTokenGenerator generator = RdsIamAuthTokenGenerator.builder()
//				.credentials(new AWSStaticCredentialsProvider(awsCreds)).region(REGION_NAME).build();
//		return generator.getAuthToken(GetIamAuthTokenRequest.builder()
//				.hostname(RDS_INSTANCE_HOSTNAME).port(RDS_INSTANCE_PORT).userName(DB_USER).build());
//	}
//
//	/**
//	 * This method sets the SSL properties which specify the key store file, its type and password:
//	 * @throws Exception
//	 */
//	private static void setSslProperties() throws Exception {
//		System.setProperty("javax.net.ssl.trustStore", createKeyStoreFile());
//		System.setProperty("javax.net.ssl.trustStoreType", KEY_STORE_TYPE);
//		System.setProperty("javax.net.ssl.trustStorePassword", DEFAULT_KEY_STORE_PASSWORD);
//	}
//
//	/**
//	 * This method returns the path of the Key Store File needed for the SSL verification during the IAM Database Authentication to
//	 * the db instance.
//	 * @return
//	 * @throws Exception
//	 */
//	private static String createKeyStoreFile() throws Exception {
//		return createKeyStoreFile(createCertificate()).getPath();
//	}
//
//	/**
//	 *  This method generates the SSL certificate
//	 * @return
//	 * @throws Exception
//	 */
//	private static X509Certificate createCertificate() throws Exception {
//		CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
//		URL url = new File("/Users/bguntamadugu/esp-project/src/main/resources/rds-ca-2019-us-west-1.pem").toURI().toURL();
//		if (url == null) {
//			throw new Exception();
//		}
//		try (InputStream certInputStream = url.openStream()) {
//			return (X509Certificate) certFactory.generateCertificate(certInputStream);
//		}
//	}
//
//	/**
//	 * This method creates the Key Store File
//	 * @param rootX509Certificate - the SSL certificate to be stored in the KeyStore
//	 * @return
//	 * @throws Exception
//	 */
//	private static File createKeyStoreFile(X509Certificate rootX509Certificate) throws Exception {
//		File keyStoreFile = File.createTempFile(KEY_STORE_FILE_PREFIX, KEY_STORE_FILE_SUFFIX);
//		try (FileOutputStream fos = new FileOutputStream(keyStoreFile.getPath())) {
//			KeyStore ks = KeyStore.getInstance(KEY_STORE_TYPE, KEY_STORE_PROVIDER);
//			ks.load(null);
//			ks.setCertificateEntry("rootCaCertificate", rootX509Certificate);
//			ks.store(fos, DEFAULT_KEY_STORE_PASSWORD.toCharArray());
//		}
//		return keyStoreFile;
//	}

//	/**
//	 * This method clears the SSL properties.
//	 * @throws Exception
//	 */
//	private static void clearSslProperties() throws Exception {
//		System.clearProperty("javax.net.ssl.trustStore");
//		System.clearProperty("javax.net.ssl.trustStoreType");
//		System.clearProperty("javax.net.ssl.trustStorePassword");
//	}

	public boolean persistData(final String insertSql,
	                           final List<Map<Integer, Object>> dataList) {
		PreparedStatement preparedStatement = null;
		Connection conn;
		try {
			conn = getConnection();
			log.info("Connection to MySQL database established.", "JdbcUrl", "User", JDBC_URL, DB_USER);
		} catch (Exception ex) {
			log.error("Exception in getting the MySql connection", ex);
			return false;
		}
		try {
			preparedStatement = conn.prepareStatement(insertSql);
			log.info("Executing mysql batch statement", "Query", insertSql) ;
			int totalRecords = 0;
			int size = 0;
			for (Map<Integer, Object> data : dataList) {
				try {
					for (Map.Entry<Integer, Object> column : data.entrySet()) {
						if (column.getValue() instanceof String) {
							preparedStatement.setString(column.getKey(), (String) column.getValue());
						} else if (column.getValue() instanceof Integer) {
							preparedStatement.setInt(column.getKey(), (Integer) column.getValue());
						} else if (column.getValue() instanceof Float) {
							preparedStatement.setFloat(column.getKey(), (Float) column.getValue());
						} else if (column.getValue() instanceof Double) {
							preparedStatement.setDouble(column.getKey(), (Double) column.getValue());
						} else if (column.getValue() instanceof Boolean) {
							preparedStatement.setBoolean(column.getKey(), (Boolean) column.getValue());
						} else if (column.getValue() instanceof java.sql.Date) {
							preparedStatement.setDate(column.getKey(), (java.sql.Date) column.getValue());
						} else if (column.getValue() instanceof java.sql.Timestamp) {
							preparedStatement.setTimestamp(column.getKey(), (java.sql.Timestamp) column.getValue());
						} else if (column.getValue() == null) {
							preparedStatement.setString(column.getKey(), (String) column.getValue());
						}
					}
					preparedStatement.addBatch();
					size++;
					if (size >= MYSQL_BATCH_SIZE) {
						totalRecords += size;
						preparedStatement.executeBatch();
						preparedStatement.close();
						preparedStatement = conn.prepareStatement(insertSql);
						size = 0;
					}
				} catch (Exception ex) {
					log.error("Exception in executing batch request", ex);
					return false;
				}
			}
			if (size > 0) {
				preparedStatement.executeBatch();
			}
			log.info("Batch inserted size: " + totalRecords + size);
			return true;
		} catch (Exception ex) {
			log.error("Exception in creating a PreparedStatement for MySql", ex);
			return false;
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} catch (Exception ex) {
				log.warn("Exception in closing MySql connection.");
				return true;
			}
		}
	}

	public ResultSet getData(String sql) {
		try {
			Connection dbConnection = getConnection();
			Statement stmt = dbConnection.createStatement();
			return stmt.executeQuery(sql);
		} catch (SQLException ex) {
			throw new RuntimeException("There is some DB problem executing + \n" + sql, ex);
		}
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
	}
}
