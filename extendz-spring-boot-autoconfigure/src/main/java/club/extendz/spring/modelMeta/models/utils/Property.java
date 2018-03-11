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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class Property {

	private String name;
	private String key;

	private Boolean required;

	private Boolean unique;
	private Integer max;
	private Integer min;
	private String pattern;

	private String hint;
	private String type;

	private String[] enums;

	private Boolean iggnoreOnCreate;
	private Boolean iggnoreOnRead;
	private Boolean iggnoreOnUpdate;

	private RelationShipType relationShipType;
	private String reference;

}// End class
