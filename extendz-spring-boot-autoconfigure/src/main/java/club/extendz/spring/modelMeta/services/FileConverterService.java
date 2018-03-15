package club.extendz.spring.modelMeta.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileConverterService {

	public Set<File> multipartToFile(File base, MultipartFile multipartFiles[]) {
		Set<File> savedFiles = new HashSet<>(multipartFiles.length);
		for (MultipartFile multipartFile : multipartFiles) {
			savedFiles.add(this.multipartToFile(base, multipartFile));
		}
		return savedFiles;
	}// multipartToFile()

	public File multipartToFile(File base, MultipartFile multipart) {
		try {
			String fileName = multipart.getOriginalFilename();
			// Create base DIR if not exists
			if (!base.exists()) {
				base.mkdirs();
			}

			String fileExtention = fileName.substring(fileName.lastIndexOf("."));
			File savingFile = new File(base, fileName);

			// Saving file NOT exits then overwrite
			if (!savingFile.exists()) {
				String timeStamp = new SimpleDateFormat("yyyyMMddHH:mm:ss:SSSSSSS").format(new Date());
				savingFile = new File(base, timeStamp + fileExtention);
			}

			FileOutputStream fos = new FileOutputStream(savingFile);
			fos.write(multipart.getBytes());
			fos.close();
			return savingFile;
		} catch (IOException e) {
			log.error("Error saving file in {}", e.getMessage());
			return null;
		}
	}// multipartToFile()

}// class