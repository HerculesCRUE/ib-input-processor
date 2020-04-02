package es.um.asio.inputprocessor.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import es.um.asio.domain.DataSetDataBase;
import es.um.asio.inputprocessor.service.repository.generic.DatasetGenericRepository;
import es.um.asio.inputprocessor.service.service.DatasetGenericService;


/**
 * Generic dataset service implementation.
 */
@Service
public class DatasetGenericServiceImpl implements DatasetGenericService {
    
	/**
	 * Kafka template.
	 */
	@Autowired
	private KafkaTemplate<String, DataSetDataBase> kafkaTemplate;
	
	@Value("${app.kafka.send-general-data-topic}")
	private Boolean sendGeneralDataTopic;
	
	/**
	 * Topic name
	 */
	@Value("${app.kafka.general-topic-name}")
	private String topicName;
	
    /**
     * Dataset generic repository.
     */
    @Autowired
    private DatasetGenericRepository datasetGenericRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSetDataBase save(DataSetDataBase data) {
    	
    	if (sendGeneralDataTopic) {
    		kafkaTemplate.send(topicName, data);    		
    	}
    	
        return datasetGenericRepository.save(data);
    }

}
