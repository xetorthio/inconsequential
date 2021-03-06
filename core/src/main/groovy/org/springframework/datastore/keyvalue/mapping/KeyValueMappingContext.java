/* Copyright (C) 2010 SpringSource
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
package org.springframework.datastore.keyvalue.mapping;

import org.springframework.datastore.keyvalue.mapping.config.GormKeyValueMappingFactory;
import org.springframework.datastore.mapping.AbstractMappingContext;
import org.springframework.datastore.mapping.MappingFactory;
import org.springframework.datastore.mapping.MappingConfigurationStrategy;
import org.springframework.datastore.mapping.PersistentEntity;
import org.springframework.datastore.mapping.config.GormMappingConfigurationStrategy;

/**
 * A MappingContext used to map objects to a Key/Value store
 *
 * @author Graeme Rocher
 * @since 1.0
 */
public class KeyValueMappingContext extends AbstractMappingContext {
    protected MappingFactory<Family, KeyValue> mappingFactory;
    private MappingConfigurationStrategy syntaxStrategy;

    /**
     * Constructs a context using the given keyspace
     *
     * @param keyspace The keyspace, this is typically the application name
     */
    public KeyValueMappingContext(String keyspace) {
        if(keyspace == null) throw new IllegalArgumentException("Argument [keyspace] cannot be null");

        // TODO: Need to abstract the construction of these to support JPA syntax etc.
        this.mappingFactory = new GormKeyValueMappingFactory(keyspace);
        this.syntaxStrategy = new GormMappingConfigurationStrategy(mappingFactory);
    }
    public MappingConfigurationStrategy getMappingSyntaxStrategy() {
        return syntaxStrategy;
    }

    public MappingFactory<Family, KeyValue> getMappingFactory() {
        return this.mappingFactory;
    }

    @Override
    protected PersistentEntity createPersistentEntity(Class javaClass) {
        return new KeyValuePersistentEntity(javaClass, this);
    }
}
