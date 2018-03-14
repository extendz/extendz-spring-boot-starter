package club.extendz.spring.example.modules.hr.master.employee;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

	@RequestMapping(value = "/{id}/profileImage")
	public ResponseEntity<?> uploadProfileImage(@PathVariable(value = "id", required = true) Long id,
			@RequestParam(value = "profileImage", required = true) MultipartFile file[], Principal principal,
			HttpServletRequest request) {
		this.localFileSaver.save(request, file);

		return null;
	}// uploadRetailerStoreChecklist()

}
