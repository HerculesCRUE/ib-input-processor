package es.um.asio.inputprocessor.kafka.listener;

import java.awt.event.InputEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import es.um.asio.abstractions.domain.ExitStatusCode;
import es.um.asio.domain.DataSetData;
import es.um.asio.domain.DataSetDataBase;
import es.um.asio.domain.InputData;
import es.um.asio.domain.importResult.ImportResult;
import es.um.asio.inputprocessor.kafka.service.ETLService;
import es.um.asio.inputprocessor.kafka.service.KafkaService;
import es.um.asio.inputprocessor.kafka.service.ServiceRedirectorService;
import es.um.asio.inputprocessor.service.service.DatasetService;

/**
 * Input message listener.
 *
 * @see InputEvent
 */
@Component
public class InputListener {

	/** Logger. */
	private final Logger logger = LoggerFactory.getLogger(InputListener.class);

	/** The service redirector service. */
	@Autowired
	private ServiceRedirectorService serviceRedirectorService;

	/** The kafka service. */
	@Autowired
	private KafkaService kafkaService;

	/** The ETL service. */
	@Autowired
	private ETLService etlService;

	/**
	 * Method listening input topic name.
	 *
	 * @param data the data
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@KafkaListener(topics = "#{'${app.kafka.input-topic-name}'.split(',')}", containerFactory = "inputKafkaListenerContainerFactory")
	public void listen(final InputData<DataSetData> data) {

		DataSetDataBase incomingData = (DataSetDataBase) data.getData();

		DatasetService service = serviceRedirectorService.redirect(incomingData);
		if (service != null) {
			logger.info("Saving {} into DB", incomingData.getClass());
			logger.info("GRAYLOG-IP Importado objeto de tipo: " + incomingData.getClass().getSimpleName());
			service.save(incomingData);
		}

		if (!(incomingData instanceof ImportResult)) {
			logger.info("Send data to general kafka topic: {}", data.getClass());
			kafkaService.sendGeneralDataTopic(data);
		}

		logger.info("******** incomingData to process: " + incomingData.getClass().toString());
		if (incomingData instanceof ImportResult
				&& ((ImportResult) incomingData).getExitStatusCode() == ExitStatusCode.COMPLETED) {
			logger.info("Running ETL service");
			etlService.run(((ImportResult) incomingData).getVersion());
		}
	}

}
