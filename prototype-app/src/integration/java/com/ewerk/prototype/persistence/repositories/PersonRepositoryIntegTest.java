/*
 * Copyright 2012-2014 the original author or authors.
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

package com.ewerk.prototype.persistence.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.ewerk.prototype.AbstractIntegTest;
import com.ewerk.prototype.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * @author holgerstolzenberg
 * @since 0.0.3
 */
public class PersonRepositoryIntegTest extends AbstractIntegTest {

  @Autowired
  private PersonRepository personRepository;

  @Test
  public void testCrudOperations() {
    personRepository.deleteAll();

    Person person = new Person();
    person.setFirstName("John");
    person.setLastName("Smith");

    Person saved = personRepository.save(person);
    assertThat(saved.getId()).isNotEmpty();

    Person loaded = personRepository.findOne(saved.getId());
    assertThat(loaded.getId()).isEqualTo(saved.getId());
    assertThat(loaded.getFirstName()).isEqualTo(saved.getFirstName());
    assertThat(loaded.getLastName()).isEqualTo(saved.getLastName());
  }
}
