package es.um.asio.inputprocessor.service.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import es.um.asio.domain.DataSetDataBase;
import es.um.asio.domain.proyectos.Proyecto;
import es.um.asio.inputprocessor.service.repository.generic.DatasetGenericRepository;
import es.um.asio.inputprocessor.service.service.DatasetGenericService;
import es.um.asio.inputprocessor.service.service.impl.DatasetGenericServiceImpl;

@RunWith(SpringRunner.class)
public class DatasetGenericServiceTest {

	/**
	 * Generic dataset service.
	 */
	@Autowired
	private DatasetGenericService service;

	/**
	 * Dataset generic repository.
	 */
	@MockBean
	private DatasetGenericRepository repository;

	@TestConfiguration
	static class DatasetGenericServiceConfiguration {
		@Bean
		public DatasetGenericService datasetGenericService() {
			return new DatasetGenericServiceImpl();
		}
	}

	@Before
	public void setUp() {
		// Mock save
		Mockito.when(this.repository.save(any())).thenAnswer(invocation -> {
			final Proyecto proyecto = invocation.getArgument(0);
			proyecto.setEntityId("1");
			return proyecto;
		});
	}

	@Test
	public void whenSaveNewDataset_thenDatasetHasId() {
		Proyecto proyecto = new Proyecto();
		proyecto.setIdProyecto("id1");
		proyecto.setNombre("Test");
		final DataSetDataBase newData = this.service.save(proyecto);

		assertThat(newData.getEntityId()).isEqualTo("id1");
	}
}
