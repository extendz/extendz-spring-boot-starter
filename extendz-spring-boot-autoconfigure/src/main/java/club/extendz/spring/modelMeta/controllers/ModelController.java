/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package club.extendz.spring.modelMeta.controllers;

//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Optional;

import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.extendz.spring.modelMeta.services.ModelService;
import lombok.RequiredArgsConstructor;

/***
 * Controller for serving model data
 * 
 * @author Randika Hapugoda
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${extendz.model-meta-endpoint}")
public class ModelController implements ResourceProcessor<RepositoryLinksResource> {

	private final ModelService modelService;

	@RequestMapping
	public ResponseEntity<?> getModels() {
		return Optional.ofNullable(modelService.getAllModelMeta())
				.map(model -> new ResponseEntity<>(model, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}// getModels()

	/*
	 * @GetMapping("enums") public ResponseEntity<?> getEnums() { return
	 * Optional.ofNullable(modelService.getEnums()).map(enums -> new
	 * ResponseEntity<>(enums, HttpStatus.OK)) .orElse(new
	 * ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)); }
	 */

	@RequestMapping(value = "{m}")
	private ResponseEntity<?> getModel(@PathVariable(value = "m") String name,
			@RequestParam(required = false, name = "projection") String projection) {
		return ResponseEntity.ok(modelService.getModelByName(name, projection));
	} // getModel()

	// TODO : add to the root of the HATEOS
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		// Link link =
		// linkTo(methodOn(ModelController.class).getModels()).withRel("models");
		// resource.add(link);
		return resource;
	}

} // class