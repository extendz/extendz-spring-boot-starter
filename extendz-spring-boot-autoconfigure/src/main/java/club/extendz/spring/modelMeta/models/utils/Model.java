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
package club.extendz.spring.modelMeta.models.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * Model representation. This map to the domain entity to custom model
 * representation.
 * 
 * @author Randika Hapugoda
 *
 */
@Data
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor
public class Model implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2592289428073906001L;
	private String name;
	private String url;

	private String title;
	private String subTitle;
	private String defaultSort;
	private String sortType;

	private String icon;

	private String packageName;

	private List<Property> properties;

	private List<Property> projection;

	@JsonIgnore
	private Map<String, Projection> projections;

	public Model(String name, String url) {
		this.name = name;
		this.url = url;
	}

}// End class
