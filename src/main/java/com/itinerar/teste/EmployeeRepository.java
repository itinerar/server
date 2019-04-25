package com.itinerar.teste;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository { // extends JpaRepository<Employee, Long> {
	
	/*
	@Query(value = "select * from employee where employee.employee_id in (SELECT e.colleague_id FROM `employee_colleague` e WHERE e.employee_id = :reqId and e.colleague_id not in (SELECT e.employee_id FROM `employee_colleague` e  WHERE e.colleague_id =  :reqId))", nativeQuery = true)
	Set<Employee> findSentRequests(@Param("reqId") Long reqId);
	
	@Query(value = "select * from employee where employee.employee_id in (SELECT e.colleague_id FROM `employee_colleague` e WHERE e.employee_id = :reqId and e.colleague_id in (SELECT e.employee_id FROM `employee_colleague` e  WHERE e.colleague_id =  :reqId))", nativeQuery = true)
	Set<Employee> findFriends(@Param("reqId") Long reqId);

	@Query(value="select * from employee where employee.employee_id in (SELECT e.employee_id FROM `employee_colleague` e WHERE e.colleague_id = :reqId and e.employee_id not in ( SELECT colleague_id FROM `employee_colleague` WHERE `employee_id` = :reqId ))", nativeQuery = true)
	Set<Employee> findReceivedRequests(@Param("reqId") Long reqId);
	*/
}
