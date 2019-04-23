package com.itinerar;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.itinerar.teste.Employee;


@SpringBootApplication
@EnableJpaRepositories
public class ItinerarApplication implements CommandLineRunner {
	@Autowired
	private GenerateRandomEntity generateEntity;
	@Autowired
	EntityManager entityManger;
	
	public static void main(String[] args) {
		SpringApplication.run(ItinerarApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		//generateEntity.generateUsers();
		generateEntity.generateRandomDatabase();
		
	       
	        /*
	        Employee employee1 = new Employee("Sergey", "Brin");
	        Employee employee2 = new Employee("Larry", "Page");
	        Employee employee3 = new Employee("Marrisa", "Mayer");
	        Employee employee4 = new Employee("Matt", "Cutts");

	        employee1.getColleagues().add(employee3);
	        employee1.getColleagues().add(employee4);
	        employee2.getColleagues().add(employee4);
	        employee3.getColleagues().add(employee4);
	        employee4.getColleagues().add(employee1);
	        employee4.getColleagues().add(employee3);
	        

	        //entityManger.getTransaction().begin();
	        entityManger.persist(employee1);
	        entityManger.persist(employee2);
	        entityManger.persist(employee3);
	        entityManger.persist(employee4);
	        */
	       // entityManger.getTransaction().commit();
	}

}

