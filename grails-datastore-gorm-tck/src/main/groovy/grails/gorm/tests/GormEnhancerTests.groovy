package grails.gorm.tests

import org.junit.Test

/**
 * Created by IntelliJ IDEA.
 * User: graemerocher
 * Date: Aug 23, 2010
 * Time: 1:43:50 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class GormEnhancerTests {


  @Test
  void testCRUD() {
    def t = TestEntity.get(1)

    assert t == null

    t = new TestEntity(name:"Bob", child:new ChildEntity(name:"Child"))
    t.save()

    assert t.id

    def results = TestEntity.list()

    assert 1 == results.size()
    assert "Bob" == results[0].name

    t = TestEntity.get(t.id)

    assert t
    assert "Bob" == t.name
  }

  @Test
  void testSaveWithMap() {
    def t = TestEntity.get(1)

    assert t == null

    t = new TestEntity(name:"Bob", child:new ChildEntity(name:"Child"))
    t.save(param:"one")

    assert t.id

  }


  @Test
  void testDynamicFinder() {

    def t = new TestEntity(name:"Bob", child:new ChildEntity(name:"Child"))
    t.save()

    t = new TestEntity(name:"Fred", child:new ChildEntity(name:"Child"))
    t.save()

    def results = TestEntity.list()

    assert 2 == results.size()

    def bob = TestEntity.findByName("Bob")

    assert bob
    assert "Bob" == TestEntity.findByName("Bob").name
  }

  @Test
  void testDisjunction() {
    def age = 40
    ["Bob", "Fred", "Barney"].each { new TestEntity(name:it, age: age++, child:new ChildEntity(name:"$it Child")).save() }

    assert 3 == TestEntity.list().size()

    def results = TestEntity.findAllByNameOrAge("Barney", 40)

    assert 2 == results.size()

    def barney = results.find { it.name == "Barney" }
    assert barney
    assert 42 == barney.age
    def bob = results.find { it.age == 40 }
    assert bob
    assert "Bob" == bob.name

  }

  @Test
  void testGetAll() {
    def age = 40
    ["Bob", "Fred", "Barney"].each { new TestEntity(name:it, age: age++, child:new ChildEntity(name:"$it Child")).save() }

    assert 2 == TestEntity.getAll(1,2).size()

  }

  @Test
  void testIdent() {
    def t = new TestEntity(name:"Bob", child:new ChildEntity(name:"Child"))
    t.save()

    assert t.id

    assert t.id == t.ident()
  }

  @Test
  void testFinderWithPagination() {
    def age = 40
    ["Bob", "Fred", "Barney", "Frank"].each { new TestEntity(name:it, age: age++, child:new ChildEntity(name:"$it Child")).save() }

    assert 4 == TestEntity.list().size()
    assert 4 == TestEntity.count()

    assert 2 == TestEntity.findAllByNameOrAge("Barney", 40).size()
    assert 1 == TestEntity.findAllByNameOrAge("Barney", 40, [max:1]).size()

  }


  @Test
  void testInQuery() {
    def age = 40
    ["Bob", "Fred", "Barney", "Frank"].each { new TestEntity(name:it, age: age++, child:new ChildEntity(name:"$it Child")).save() }

    assert 2 == TestEntity.findAllByNameInList(["Fred", "Frank"]).size()
    assert 1 == TestEntity.findAllByNameInList(["Joe", "Frank"]).size()
    assert 0 == TestEntity.findAllByNameInList(["Jeff", "Jack"]).size()
    assert 2 == TestEntity.findAllByNameInListOrName(["Joe", "Frank"], "Bob").size()
  }

  @Test
  void testLikeQuery() {
    def age = 40
    ["Bob", "Fred", "Barney", "Frank"].each { new TestEntity(name:it, age: age++, child:new ChildEntity(name:"$it Child")).save() }

    def results = TestEntity.findAllByNameLike("Fr%")

    assert 2 == results.size()
    assert results.find { it.name == "Fred" }
    assert results.find { it.name == "Frank" }
  }
  @Test
  void testCountByQuery() {

    def age = 40
    ["Bob", "Fred", "Barney"].each { new TestEntity(name:it, age: age++, child:new ChildEntity(name:"$it Child")).save() }

    assert 3 == TestEntity.list().size()

    assert 2 == TestEntity.countByNameOrAge("Barney", 40)
    assert 1 == TestEntity.countByNameAndAge("Bob", 40)
  }

  @Test
  void testConjunction() {
    def age = 40
    ["Bob", "Fred", "Barney"].each { new TestEntity(name:it, age: age++, child:new ChildEntity(name:"$it Child")).save() }

    assert 3 == TestEntity.list().size()

    assert TestEntity.findByNameAndAge("Bob", 40)
    assert !TestEntity.findByNameAndAge("Bob", 41)
  }

  @Test
  void testCount() {
    def t = new TestEntity(name:"Bob", child:new ChildEntity(name:"Child"))
    t.save()

    assert 1 == TestEntity.count()

    t = new TestEntity(name:"Fred", child:new ChildEntity(name:"Child"))
    t.save()

    assert 2 == TestEntity.count()

  }
}

@grails.persistence.Entity
class ChildEntity {
  Long id
  String name

  static mapping = {
    name index:true
  }
  static belongsTo = [TestEntity]
}