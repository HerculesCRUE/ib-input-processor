package es.um.asio.inputprocessor.back.controller.importresult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.um.asio.abstractions.dto.ImportResultDto;
import es.um.asio.domain.importResult.ImportResult;
import es.um.asio.inputprocessor.service.filter.ImportResultFilter;
import es.um.asio.inputprocessor.service.proxy.ImportResultProxy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Import Result Controller.
 */
@RestController
@RequestMapping(ImportResultController.Mappings.BASE)
public class ImportResultController {

	/**
	 * Proxy service implementation for {@link ImportResult}.
	 */
	@Autowired
	private ImportResultProxy proxy;

	/**
	 * Search import results.
	 *
	 * @param filter   the user filter
	 * @param pageable Pagination configuration
	 * @return page containing results
	 */
	@GetMapping(ImportResultController.Mappings.SEARCH)
	public Page<ImportResultDto> searchImportResults(final ImportResultFilter filter, final Pageable pageable) {
		// FindPAginated
		return this.proxy.findPaginated(filter, pageable);
	}

	/**
	 * Mappings.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	static final class Mappings {

		/**
		 * Controller request mapping.
		 */
		protected static final String BASE = "/import-result";

		protected static final String SEARCH = "/search";
	}
}
