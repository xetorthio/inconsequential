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
package org.springframework.datastore.redis.collection;

import org.springframework.beans.TypeConverter;
import org.springframework.datastore.keyvalue.convert.ByteArrayAwareTypeConverter;
import org.springframework.datastore.redis.util.RedisTemplate;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Creates a list that is backed onto a Redis list
 *
 * @author Graeme Rocher
 * @since 1.0
 * 
 */
public class RedisList extends AbstractList implements List, RedisCollection {
    private RedisTemplate redisTemplate;
    private String redisKey;
    private TypeConverter converter = new ByteArrayAwareTypeConverter();

    public RedisList(RedisTemplate redisTemplate, String redisKey) {
        this.redisTemplate = redisTemplate;
        this.redisKey = redisKey;
    }

    public Object get(int index) {
        return redisTemplate.lindex(redisKey, index);
    }

    public Object set(int index, Object element) {
        Object prev = get(index);
        redisTemplate.lset(redisKey, index, element);
        return prev;
    }

    public void add(int index, Object element) {
        if(index == 0) {
            redisTemplate.lpush(redisKey, element);
        }
        else if(index == size()) {
            redisTemplate.rpush(redisKey, element);
        }
        else {
            throw new UnsupportedOperationException("Redis lists only support adding elements at the beginning or end of a list");
        }
    }

    public Object remove(int index) {
        Object o = get(index);
        remove(o);
        return o;
    }

    public int size() {
        return redisTemplate.llen(redisKey);
    }

    public boolean contains(Object o) {
        return Arrays.asList(elements()).contains(o);
    }

    public Iterator iterator() {
        return new RedisIterator(elements(), this);
    }

    private String[] elements() {
        return redisTemplate.lrange(redisKey, 0, -1);
    }

    public boolean add(Object e) {
        redisTemplate.rpush(redisKey, e);
        return true;
    }

    public boolean remove(Object o) {
        return redisTemplate.lrem(redisKey, o, 0) != 0;
    }

    public String getRedisKey() {
        return this.redisKey;
    }

    public String[] members() {
        return redisTemplate.lrange(redisKey, 0, -1);
    }

    public String[] members(int offset, int max) {
        return redisTemplate.lrange(redisKey, offset, max);
    }
}
