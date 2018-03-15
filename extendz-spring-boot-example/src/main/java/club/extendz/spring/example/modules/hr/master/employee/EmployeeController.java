package club.extendz.spring.example.modules.hr.master.employee;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import club.extendz.spring.modelMeta.services.LocalFileSaver;

@Controller
@RequestMapping(value = "${spring.data.rest.base-path}/employees")
public class EmployeeController {

	private LocalFileSaver localFileSaver;

	public EmployeeController(LocalFileSaver localFileSaver) {
		this.localFileSaver = localFileSaver;
	}

	@PostMapping(value = "/{id}/profileImage")
	public ResponseEntity<?> setProfileImage(@PathVariable(value = "id", required = true) Long id,
			@RequestParam(value = "profileImage", required = true) MultipartFile file[], Principal principal,
			HttpServletRequest request) {
		return ResponseEntity.ok(this.localFileSaver.save(request, file));
	}// setProfileImage()

	@GetMapping(value = "/{id}/profileImage/{fileName}")
	public void getProfileImage(@PathVariable(value = "id", required = true) Long id,
			@PathVariable("fileName") String fileName, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		this.localFileSaver.get(fileName, request, response);
	} // getProfileImage()

	@PostMapping(value = "/{id}/idImages")
	public ResponseEntity<?> setIdImages(@PathVariable(value = "id", required = true) Long id,
			@RequestParam(value = "idImages", required = true) MultipartFile file[], Principal principal,
			HttpServletRequest request) {
		return ResponseEntity.ok(this.localFileSaver.save(request, file));
	}// setProfileImage()

	@GetMapping(value = "/{id}/idImages/{fileName}")
	public void getIdImages(@PathVariable(value = "id", required = true) Long id,
			@PathVariable("fileName") String fileName, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		this.localFileSaver.get(fileName, request, response);
	} // getProfileImage()
}
