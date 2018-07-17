package com.kameocode.anydao.todoapp

import com.kameocode.anydao.AnyDAO
import com.kameocode.anydao.wraps.like
import com.kameocode.anydao.wraps.or
import javax.persistence.EntityManager
import javax.persistence.Persistence

/**
 * Example of using AnyDAO without spring
 */
fun main(args: Array<String>) {
    val em: EntityManager = Persistence.createEntityManagerFactory("test-pu").createEntityManager()
    val anyDao = AnyDAO(em)
    em.transaction.begin()

    val u1 = UserODB(email = "email1", todos = listOf(
            TodoODB(name = "task1.1")))
    val u2 = UserODB(email = "email2", todos = listOf(
            TodoODB(name = "task2.1"),
            TodoODB(name = "task2.2"),
            TodoODB(name = "task2.3")
    ))
    val u3 = UserODB(email = "email3", login = "login3")
    anyDao.persist(u1, u2, u3)


    val resAll = anyDao.all(UserODB::class)
    val res0 = anyDao.all(UserODB::class, { it[UserODB::email].eq("email0") })
    val res1 = anyDao.all(UserODB::class, {
        it.joinList(UserODB::todos)[TodoODB::name].or {
            it like "task1.1"
            it like "task2.2"
        }
    })
    println("AA " + resAll.size)
    println("AA " + res0.size)
    println("AA " + res1.size)


    em.transaction.commit()
}