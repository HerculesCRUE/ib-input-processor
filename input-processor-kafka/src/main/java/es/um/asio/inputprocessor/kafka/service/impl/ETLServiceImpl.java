package es.um.asio.inputprocessor.kafka.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import es.um.asio.abstractions.constants.Constants;
import es.um.asio.domain.importetlresult.ImportEtlResult;
import es.um.asio.inputprocessor.kafka.model.ETLJobResponse;
import es.um.asio.inputprocessor.kafka.service.ETLService;
import es.um.asio.inputprocessor.service.exeption.InputProcessorException;
import es.um.asio.inputprocessor.service.repository.ImportEtlResultRepository;

/**
 * Service that run job of ETL.
 */
@Service
public class ETLServiceImpl implements ETLService {

	/**
	 * Logger
	 */
	private final Logger logger = LoggerFactory.getLogger(ETLServiceImpl.class);

	@Autowired(required = true)
	private ImportEtlResultRepository importEtlResult;

	/**
	 * The rest template.
	 */
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * The ETL service endpoint.
	 */
	@Value("${app.services.etl.endpoint}")
	private String endPoint;

	/**
	 * The ETL job.
	 */
	@Value("${app.services.etl.job}")
	private String job;

	/**
	 * The ETL username.
	 */
	@Value("${app.services.etl.username}")
	private String username;

	/**
	 * The ETL password.
	 */
	@Value("${app.services.etl.password}")
	private String password;

	@Value("${app.management-system.url}")
	private String urlmanagementsystem;

	/**
	 * Run ETL job.
	 *
	 * @param version the version
	 * @return the ETL job response
	 */
	@Override
	public ETLJobResponse run(long version) {
		ETLJobResponse etlJobResponse = null;
		ImportEtlResult importResult = new ImportEtlResult();

		String url = endPoint.concat("/?job=").concat(job).concat("&version=").concat(String.valueOf(version));

		importResult.setVersion(version);
		importResult.setEndpoint(url);
		importResult.setDateTime(new Date());
		importResult.setStatus(Constants.KO);

		boolean statusMSETLListener = getStatusETL();
		this.logger.info("CALL getStatusListeners Management-system, status listeners: " + statusMSETLListener);
		if (statusMSETLListener) {

			try {
				ResponseEntity<ETLJobResponse> response = restTemplate.exchange(url, HttpMethod.GET,
						new HttpEntity<ETLJobResponse>(createBasicAuthenticationHeader()), ETLJobResponse.class);
				etlJobResponse = response.getBody();
				if (response.getStatusCode() == HttpStatus.OK && etlJobResponse != null
						&& etlJobResponse.getResult().equals(Constants.OK)) {
					importResult.setMessage(etlJobResponse.getMessage());
					importResult.setStatus(Constants.OK);
					logger.info("The ETL job {} has been ran successfully {}", url, response);
				} else {
					importResult.setMessage(response.toString());
					logger.error("Error running ETL process {}, Response: ", url, response);
				}
			} catch (Exception e) {
				importResult.setMessage(e.getMessage());
				logger.error("Error running ETL process {}. Exception: {}", url, e.getMessage());
				logger.error("run", e);
			}
		} else {
			importResult.setMessage(Constants.MANAGEMENT_SYSTEM_BUSY);
		}

		importEtlResult.save(importResult);

		return etlJobResponse;
	}

	/**
	 * Método para comprobar el estado de los listeners del Management system.
	 * 
	 * @return true ( Management system libre) / false (management system ocupado
	 *         por etl)
	 */
	public Boolean getStatusETL() {
		this.logger.info("Método getStatusETL()");
		try {
			URL url = new URL(urlmanagementsystem);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				logger.error("Failed HTTP Error code: {}", conn.getResponseCode());
				throw new InputProcessorException("Failed : HTTP Error code : " + conn.getResponseCode());
			}

			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String output;
			Boolean response = false;
			while ((output = br.readLine()) != null) {
				response = Boolean.valueOf(output);
				this.logger.info("DATA from service " + urlmanagementsystem + " , response: " + response);
			}
			conn.disconnect();
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * Creates the basic authentication header.
	 *
	 * @return the http headers
	 */
	private HttpHeaders createBasicAuthenticationHeader() {
		HttpHeaders authHeaders = new HttpHeaders();
		authHeaders.set("Authorization",
				"Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
		return authHeaders;
	}

}
