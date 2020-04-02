package es.um.asio.inputprocessor.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import es.um.asio.domain.DataSetData;
import es.um.asio.domain.InputData;
import es.um.asio.inputprocessor.service.service.KafkaService;

@Service
public class KafkaServiceImpl implements KafkaService {

	
/**
 * Topic name
 */
@Value("${app.kafka.general-topic-name}")
private String topicName;

	/**
	 * Kafka template.
	 */
	@Autowired
	private KafkaTemplate<String, InputData<DataSetData>> kafkaTemplate;
	
	@Value("${app.kafka.send-general-data-topic}")
	private Boolean sendGeneralDataTopic;
	
	  /**
     * {@inheritDoc}
     */
    @Override
    public void sendGeneralDataTopic(InputData<DataSetData> data) {
    	if (sendGeneralDataTopic) {
    		kafkaTemplate.send(topicName, data);    		
    	}
    }
}
