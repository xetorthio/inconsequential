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

package org.springframework.datastore.redis.query;

import org.springframework.datastore.core.Session;
import org.springframework.datastore.mapping.PersistentEntity;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Used for query results and lazily loading entities from their identifiers
 *
 * @author Graeme Rocher
 */
public class RedisEntityResultList extends AbstractList{

    Session session;
    private Object[] identifiers;
    private Object[] objects;
    private PersistentEntity entity;

    public RedisEntityResultList(Session session, PersistentEntity entity, Object[] identifiers) {
        this.session = session;
        this.identifiers = identifiers;
        this.objects = new Object[identifiers.length];
        this.entity = entity;
    }

    @Override
    public Object get(int index) {
        Object result = objects[index];
        if(result == null) {
            Object identifier = identifiers[index];
            result = session.retrieve(this.entity.getJavaClass(), (Serializable) identifier);
            objects[index] = result;
        }
        return result;
    }

    @Override
    public int size() {
        return identifiers.length;
    }
}
