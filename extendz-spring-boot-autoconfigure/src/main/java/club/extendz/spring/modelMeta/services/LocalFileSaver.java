package club.extendz.spring.modelMeta.services;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalFileSaver {

	private String basePath;

	@Value("${extendz.local-data-dir}")
	private String localDataDir;

	public LocalFileSaver(RepositoryRestMvcConfiguration restMvcConfiguration) {
		this.basePath = restMvcConfiguration.config().getBasePath().toString();
	}

	public void save(HttpServletRequest request, MultipartFile[] files) {
		String uri = request.getRequestURI();
		String dirPath = uri.substring(basePath.length());
		File outputFiel = new File(this.localDataDir, dirPath);
		System.err.println(outputFiel.getAbsolutePath());
	}

}
