package es.um.asio.inputprocessor.kafka.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import es.um.asio.inputprocessor.kafka.model.ETLJobResponse;
import es.um.asio.inputprocessor.kafka.service.ETLService;

/**
 * Service that run job of ETL.
 */
@Service
public class ETLServiceImpl implements ETLService {
    
    /**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(ETLServiceImpl.class);

    /** 
     * The rest template. 
     * */
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
     * The ETL path.
    */
    @Value("${app.services.etl.path}")
    private String path;
    
    
    /**
     * Run ETL job.
     *
     * @return the ETL job response
     */
    @Override
    public ETLJobResponse run() {
        ETLJobResponse etlJobResponse = null;
        String url = endPoint.concat("/?job=").concat(job).concat("&param_path=").concat(path);

        try {
            ResponseEntity<ETLJobResponse> response = restTemplate.getForEntity(url, ETLJobResponse.class);
            etlJobResponse = response.getBody();
            if (response.getStatusCode() == HttpStatus.OK && etlJobResponse != null && etlJobResponse.getResult().equals("OK")) {
                logger.info("The ETL job {} has been ran successfully {}", url, response.toString());
            } else {
                logger.error("Error running ETL process {}, Response: ", url, response.toString());
            }
        } catch (Exception e) {
            logger.error("Error running ETL process {}. Exception: {}", url, e.getMessage());
            e.printStackTrace();
        }

        return etlJobResponse;
    }
    

}
