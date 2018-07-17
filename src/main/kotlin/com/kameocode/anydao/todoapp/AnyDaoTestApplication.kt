package com.kameocode.anydao.todoapp

import com.kameocode.anydao.AnyDAO
import com.kameocode.anydao.wraps.like
import com.kameocode.anydao.wraps.or
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

/**
 * Example of using AnyDAO in SpringBott application
 */
@SpringBootApplication
open class AnyDaoTestApplication() : CommandLineRunner {

    @Autowired
    lateinit var anyDao: AnyDAO

    @Bean
    open fun anyDao(em: EntityManager): AnyDAO {
        return AnyDAO(em)
    }

    @Transactional
    override fun run(vararg p: String?) {
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
    }

}


fun main(args: Array<String>) {
    SpringApplication.run(AnyDaoTestApplication::class.java, *args)
}