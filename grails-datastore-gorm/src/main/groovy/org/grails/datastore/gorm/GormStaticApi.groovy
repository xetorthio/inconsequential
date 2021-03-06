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
package org.grails.datastore.gorm

import org.springframework.datastore.core.Datastore
import org.springframework.datastore.query.Query
import grails.gorm.CriteriaBuilder
import org.grails.datastore.gorm.finders.DynamicFinder

/**
 *  Static methods of the GORM API
 *
 * @author Graeme Rocher
 */
class GormStaticApi extends AbstractGormApi {


  GormStaticApi(Class persistentClass, Datastore datastore) {
    super(persistentClass,datastore)
  }

  /**
   * Retrieves and object from the datastore. eg. Book.get(1)
   */
  def get(Serializable id) {
    datastore.currentSession.retrieve(persistentClass,id)
  }

  /**
   * Retrieves and object from the datastore as a proxy. eg. Book.load(1)
   */
  def load(Serializable id) {
    datastore.currentSession.proxy(persistentClass,id)
  }

  /**
   * Retrieves and object from the datastore as a proxy. eg. Book.proxy(1)
   */
  def proxy(Serializable id) {
    datastore.currentSession.proxy(persistentClass,id)
  }

  /**
   * Retrieve all the objects for the given identifiers
   * @param ids The identifiers to operate against
   * @return A list of identifiers
   */
  List getAll(Serializable... ids) {
    datastore.currentSession.retrieveAll(persistentClass, ids)
  }

  /**
   * Creates a criteria builder instance
   */
  def createCriteria() {
    return new CriteriaBuilder(persistentClass, datastore)
  }

  /**
   * Creates a criteria builder instance
   */
  def withCriteria(Closure callable) {
    return new CriteriaBuilder(persistentClass, datastore).list(callable)
  }

  /**
   * Locks an instance for an update
   * @param id The identifier
   * @return The instance
   */
  def lock(Serializable id) {
    datastore.currentSession.lock(persistentClass, id)
  }

  /**
   * Counts the number of persisted entities
   * @return The number of persisted entities
   */
  Integer count() {
    def q = datastore.currentSession.createQuery(persistentClass)
    q.projections().count()
    def result = q.singleResult()
    if(!(result instanceof Number)) result = result.toString()
    try {
      result as Integer
    } catch (NumberFormatException e) {
      return 0
    }
  }

  /**
   * Checks whether an entity exists
   */
  boolean exists(Serializable id) {
    get(id) != null
  }

  /**
   * Lists objects in the datastore. eg. Book.list(max:10)
   *
   * @param params Any parameters such as offset, max etc.
   * @return A list of results
   */
  List list(Map params) {
    Query q = datastore.currentSession.createQuery(persistentClass)
    DynamicFinder.populateArgumentsForCriteria(persistentClass, q, params)
    q.list()
  }

  /**
   * Finds all results matching all of the given conditions. Eg. Book.findAllWhere(author:"Stephen King", title:"The Stand")
   *
   * @param queryMap The map of conditions
   * @return A list of results
   */
  List findAllWhere(Map queryMap) {
    Query q = datastore.currentSession.createQuery(persistentClass)
    if(queryMap) {
      for(entry in queryMap) {
        q.eq(entry.key, entry.value)
      }
    }
    q.list()
  }

  /**
   * Finds a single result matching all of the given conditions. Eg. Book.findWhere(author:"Stephen King", title:"The Stand")
   *
   * @param queryMap The map of conditions
   * @return A single result
   */
  def findWhere(Map queryMap) {
    Query q = datastore.currentSession.createQuery(persistentClass)
    if(queryMap) {
      for(entry in queryMap) {
        q.eq(entry.key, entry.value)
      }
    }
    q.singleResult()
  }

  /**
   * Execute a closure whose first argument is a reference to the current session
   * @param callable The callable closure
   * @return The result of the closure
   */
  def withSession(Closure callable) {
    callable.call(datastore.currentSession)
  }

  // TODO: In the first version no support will exist for String-based queries
  def executeQuery(String query) {
    unsupported("executeQuery")
  }

  def executeQuery(String query, Map args) {
    unsupported("executeQuery")
  }

  def executeQuery(String query, Map params, Map args) {
    unsupported("executeQuery")
  }

  def executeQuery(String query, Collection params) {
    unsupported("executeQuery")
  }

  def executeQuery(String query, Collection params, Map args) {
    unsupported("executeQuery")
  }

  def executeUpdate(String query) {
    unsupported("executeUpdate")
  }

  def executeUpdate(String query, Map args) {
    unsupported("executeUpdate")
  }

  def executeUpdate(String query, Map params, Map args) {
    unsupported("executeUpdate")
  }

  def executeUpdate(String query, Collection params) {
    unsupported("executeUpdate")
  }

  def executeUpdate(String query, Collection params, Map args) {
    unsupported("executeUpdate")
  }

  def find(String query) {
    unsupported("find")
  }

  def find(String query, Map args) {
    unsupported("find")
  }

  def find(String query, Map params, Map args) {
    unsupported("find")
  }

  def find(String query, Collection params) {
    unsupported("find")
  }

  def find(String query, Collection params, Map args) {
    unsupported("find")
  }

  def findAll(String query) {
    unsupported("findAll")
  }

  def findAll(String query, Map args) {
    unsupported("findAll")
  }

  def findAll(String query, Map params, Map args) {
    unsupported("findAll")
  }

  def findAll(String query, Collection params) {
    unsupported("find")
  }

  def findAll(String query, Collection params, Map args) {
    unsupported("findAll")
  }

  def unsupported(method) {
    throw new UnsupportedOperationException("String-based queries like [$method] are currently not supported in this implementation of GORM. Use criteria instead.")
  }

}
