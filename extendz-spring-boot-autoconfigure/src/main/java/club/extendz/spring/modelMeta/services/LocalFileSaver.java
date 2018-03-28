package club.extendz.spring.modelMeta.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocalFileSaver {

	private String basePath;

	@Value("${extendz.local-data-dir}")
	private String localDataDir;

	private FileConverterService fileConverterService;

	public LocalFileSaver(RepositoryRestMvcConfiguration restMvcConfiguration,
			FileConverterService fileConverterService) {
		this.basePath = restMvcConfiguration.config().getBasePath().toString();
		this.fileConverterService = fileConverterService;
	} // constructor

	private File getParentFile(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String dirPath = uri.substring(basePath.length());
		return new File(this.localDataDir, dirPath);
	} // getParentFile()

	public Iterator<String> save(HttpServletRequest request, MultipartFile[] multipartFiles) {
		File parentDir = this.getParentFile(request);
		Set<File> savedFiles = fileConverterService.multipartToFile(parentDir, multipartFiles);
		return savedFiles.stream().map(file -> file.getName()).collect(Collectors.toCollection(LinkedList::new)).descendingIterator();
	}// save()

	public void get(String fileName, HttpServletRequest request, HttpServletResponse response) {
		File exportFile = this.getParentFile(request);
		try {
			ServletContext context = request.getServletContext();
			FileInputStream inputStream = new FileInputStream(exportFile);
			String mimeType = context.getMimeType(exportFile.getName());
			if (mimeType == null) {
				mimeType = MediaType.IMAGE_JPEG.getType();
			}
			response.setContentType(mimeType);
			response.setContentLength((int) exportFile.length());
			String headerValue = String.format("attachment; filename=\"%s\"", exportFile.getName());
			response.setHeader("Content-Disposition", headerValue);
			OutputStream outStream = response.getOutputStream();
			byte[] buffer = new byte[1028];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			inputStream.close();
			outStream.close();
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}// End exportFile ()
}