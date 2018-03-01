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
package club.extendz.spring.example.modules.hr.transaction.employeeAbsconded;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.StringPath;

/***
 * @author Asitha Niranjan (asitha93@live.com)
 */
@Repository
public interface EmployeeAbscondedRepository extends PagingAndSortingRepository<EmployeeAbsconded, Long>,
		QueryDslPredicateExecutor<EmployeeAbsconded>, QuerydslBinderCustomizer<QEmployeeAbsconded> {
	@Override
	default void customize(QuerydslBindings bindings, QEmployeeAbsconded employeeAbsconded) {
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
	}
}
