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
package club.extendz.spring.modelMeta.services;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.envers.Audited;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import club.extendz.spring.modelMeta.annotations.Extendz;
import club.extendz.spring.modelMeta.annotations.enums.InputType;
import club.extendz.spring.modelMeta.models.utils.Model;
import club.extendz.spring.modelMeta.models.utils.Projection;
import club.extendz.spring.modelMeta.models.utils.Property;
import club.extendz.spring.modelMeta.models.utils.RelationShipType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***
 * Process the Spring Data REST exposed models
 * 
 * @author Randika Hapugoda
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ModelService {

	private final RepositoryRestMvcConfiguration restMvcConfiguration;
	private final SourceCodeGenerationService sourceCodeGenerationService;

	/***
	 * Holds all the model related data. Key contains the model name and value is
	 * the object.
	 */
	private static Map<String, Model> modelsMap = new HashMap<>();

	private static Comparator<Model> byName = new Comparator<Model>() {
		@Override
		public int compare(Model o1, Model o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};

	/***
	 * Basic representation of the model.Contains the name and url.This can be used
	 * with extendz-angular-root.
	 */
	private static Set<Model> basicModelsMap = new TreeSet<>(byName);

	private static HashMap<String, Field> enumsMap = new HashMap<>();

	@PostConstruct
	public void onPostConstruct() {
		log.info("Initializing Model Meta Export service.");

		// Get all rest end points
		restMvcConfiguration.resourceMappings().forEach(resourceMapping -> {
			Class<?> domainType = resourceMapping.getDomainType();
			String domainClassName = domainType.getSimpleName();

			String url = resourceMapping.getPath().toString();

			String name = WordUtils.uncapitalize(domainClassName);
			Model model = new Model(domainClassName, url);

			// this.generateAuditing(domainType, model);

			basicModelsMap.add(SerializationUtils.clone(model));

			// add Properties
			model.setProperties(this.getProperties(domainType, model));

			// get projections
			Map<String, Class<?>> projections = restMvcConfiguration.config().getProjectionConfiguration()
					.getProjectionsFor(resourceMapping.getDomainType());

			model.setProjections(this.getProjection(projections, model));
			// System.err.println(model.getProjections().size());
			modelsMap.put(domainClassName.toLowerCase(), model);
		});

	}// onPostConstruct()

	private void generateAuditing(Class<?> domainType, Model model) {
		Audited audited = domainType.getAnnotation(Audited.class);
		if (audited != null) {
			this.sourceCodeGenerationService.generateAuditingClasses(domainType, model);
		}
	}// generateAuditing()

	private HashMap<String, Projection> getProjection(Map<String, Class<?>> projectionsMap, Model model) {
		HashMap<String, Projection> projections = new HashMap<String, Projection>();

		projectionsMap.forEach((key, value) -> {

			org.springframework.data.rest.core.config.Projection projectionAnotation = value
					.getAnnotation(org.springframework.data.rest.core.config.Projection.class);

			Projection projection = new Projection();
			String name = projectionAnotation.name();

			Method[] methods = value.getMethods();
			List<Property> properties = new ArrayList<>(value.getMethods().length);

			for (int i = 0; i < methods.length; i++) {
				Property property = new Property();
				String propertyName = methods[i].getName().substring(3);
				propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
				property.setName(propertyName);
				properties.add(property);
			}

			projection.setProperties(properties);
			projections.put(name, projection);
		});
		return projections;
	} // addProjection()

	public Set<Model> getAllModelMeta() {
		return basicModelsMap;
	}// getAllModelMeta()

	public HashMap<String, Field> getEnums() {
		return enumsMap;
	}

	public Model getModelByName(String name, String projectionName) {
		Model model = SerializationUtils.clone(modelsMap.get(name));
		if (projectionName != null) {
			Projection projection = model.getProjections().get(projectionName);
			if (projection != null)
				model.setProperties(projection.getProperties());
		}
		// model.setProjections(null);
		return model;
	}// getModelByName()

	public List<Property> getProperties(Class<?> entityClass, Model m) {
		List<Property> properties = new ArrayList<Property>();
		// Check for local properties
		for (Field field : entityClass.getDeclaredFields()) {
			Property p = covertFieldToProperty(field, m);
			if (p != null) {
				properties.add(p);
			}
		}
		return properties;
	}// End getPropertiesByClass

	private Property covertFieldToProperty(Field field, Model m) {

		// Ignore Final fields
		if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
			return null;
		}

		JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
		if (jsonIgnore != null)
			return null;

		// Create new Property
		Property property = new Property();

		// Set Name
		property.setName(field.getName());
		property.setReference(field.getType().getSimpleName());
		property.setType(InputType.STRING.toString());
		// property.setReference(InputType.STRING.toString());

		// Long,Integer and double to be type number
		switch (field.getType().getSimpleName().toLowerCase()) {
		case "integer":
		case "double":
		case "long":
			property.setReference(InputType.NUMBER.toString());
			property.setType(InputType.NUMBER.toString());
			break;
		case "boolean":
			property.setReference(InputType.BOOLEAN.toString());
			property.setType(InputType.BOOLEAN.toString());
			break;
		case "date":
			property.setType(InputType.DATE.toString());
		case "string":
			// No need to set Reference String.
			property.setReference(null);
		}

		// default types
		try {
			ParameterizedType parameterizedType1 = (ParameterizedType) field.getGenericType();

			// Not annotated fields
			if (field.getAnnotations().length == 0) {
				String className = parameterizedType1.getActualTypeArguments()[0].getTypeName();
				switch (field.getType().getSimpleName()) {
				case "Map":
					String key = null;
					String reference = null;
					for (Type type : parameterizedType1.getActualTypeArguments()) {
						if (key == null)
							key = Class.forName(type.getTypeName()).getSimpleName().toLowerCase();
						else
							reference = Class.forName(type.getTypeName()).getSimpleName();
					}
					property.setKey(key);
					property.setReference(reference);

					break;
				case "List":
					try {
						property.setReference(Class.forName(className).getSimpleName());
						property.setRelationShipType(RelationShipType.MULTIPLE);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					break;
				}
				// Map
				property.setType(field.getType().getSimpleName().toLowerCase());

			} else {
				// This is used to detect the file upload single or mutiple
				// based on annotaion.
				property.setRelationShipType(RelationShipType.MULTIPLE);
			}
		} catch (Exception e) {
			// TODO handle exeception.
		}

		// Id
		Id id = field.getAnnotation(Id.class);
		if (id != null) {
			return null;
		}

		Column column = field.getAnnotation(Column.class);
		if (column != null) {
			property.setRequired(!column.nullable());
		}

		NotNull notNull = field.getAnnotation(NotNull.class);
		if (notNull != null) {
			property.setRequired(true);

		}

		Size size = field.getAnnotation(Size.class);
		if (size != null) {
			property.setMin(size.min());
			if (size.max() != Integer.MAX_VALUE)
				property.setMax(size.max());
		}

		// JPA
		OneToOne oneToOne = field.getAnnotation(OneToOne.class);
		if (oneToOne != null) {
			property.setType(Object.class.getSimpleName().toLowerCase());
			property.setRelationShipType(RelationShipType.SINGLE);
		}

		ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
		if (manyToOne != null) {
			property.setType(Object.class.getSimpleName().toLowerCase());
			property.setRelationShipType(RelationShipType.SINGLE);
			property.setReference(field.getType().getSimpleName());
		}

		OneToMany oneToMany = field.getAnnotation(OneToMany.class);
		if (oneToMany != null) {
			property.setRelationShipType(RelationShipType.MULTIPLE);
			property.setType(field.getType().getSimpleName().toLowerCase());
			property.setMappedBy(oneToMany.mappedBy());

			ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
			String className = parameterizedType.getActualTypeArguments()[0].getTypeName();
			try {
				property.setReference(Class.forName(className).getSimpleName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			property.setIggnoreOnRead(true);
		}

		ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
		if (manyToMany != null) {
			property.setRelationShipType(RelationShipType.MULTIPLE);
			property.setType(field.getType().getSimpleName().toLowerCase());
			ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
			String className = parameterizedType.getActualTypeArguments()[0].getTypeName();
			try {
				property.setReference(Class.forName(className).getSimpleName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			property.setIggnoreOnRead(true);
		}

		// Extendz
		Extendz ext = field.getAnnotation(Extendz.class);
		if (ext != null) {
			if (ext.title())
				m.setTitle(property.getName());
			if (ext.type() != InputType.NONE)
				property.setType(ext.type().toString());
			if (!ext.mappedBySource().equals("")) {
				property.setMappedBySource(ext.mappedBySource());
			}
		}

		// Collect enums for later use.
		if (field.getAnnotation(Enumerated.class) != null) {
			enumsMap.put(field.getType().getCanonicalName(), field);
			property.setType("enum");
			property.setRelationShipType(RelationShipType.ENUM);
			String[] enumList = new String[field.getType().getEnumConstants().length];
			for (int i = 0; i < field.getType().getEnumConstants().length; i++) {
				enumList[i] = field.getType().getEnumConstants()[i].toString();
			}
			property.setEnums(enumList);
			// property.setType(field.getType().getSimpleName().toLowerCase());
			// property.setReference(field.getType().getSimpleName());
		}

		return property;
	}// End covertFieldToProperty()

}// End class
