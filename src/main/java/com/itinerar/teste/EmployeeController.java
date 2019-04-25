package com.itinerar.teste;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.itinerar.entity.Journey;
import com.itinerar.entity.Post;
import com.itinerar.entity.User;
import com.itinerar.exception.ItinerarEntityNotFoundException;

@RestController
@CrossOrigin
public class EmployeeController {
	
private Logger logger = LoggerFactory.getLogger(this.getClass());
	
/*
	@Autowired
	EmployeeRepository employeeRepository;
	
	/*
	 * 
	 * getTeammates daca ti-au acceptat cererea
	 * getColleges pentru request invite
	 */
	
/*
	@GetMapping(path = "/employeeSentRequest", produces=MediaType.APPLICATION_JSON_VALUE)
	   private  Set<Employee> employeeSentRequest() {
		Long employeeLoggedId = 72L;
		return employeeRepository.findSentRequests(employeeLoggedId);
	}
	
	@GetMapping(path = "/employeeReceivedRequest", produces=MediaType.APPLICATION_JSON_VALUE)
	   private  Set<Employee> employeeReceivedRequest() {
		Long employeeLoggedId = 72L;
		return employeeRepository.findReceivedRequests(employeeLoggedId);
	}
	
	
	@GetMapping(path = "/employeesFriends", produces=MediaType.APPLICATION_JSON_VALUE)
	   private  Set<Employee> findFriends() {
			Long employeeLoggedId = 72L;
			return employeeRepository.findFriends((long) employeeLoggedId);
	        
	}
	
	@PostMapping("/requestFriendship/{id}")
	public ResponseEntity<Object> requestFriendShip(@PathVariable Long id) throws Exception {
		
		Long employeeLoggedId = 72L;
		Optional<Employee> optionalEmployee = employeeRepository.findById((long) employeeLoggedId);
		Optional<Employee> optionalNewFriend = employeeRepository.findById((long) id);
		if (!optionalEmployee.isPresent())
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + employeeLoggedId);
		else if (!optionalNewFriend.isPresent())
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + id);
		
		Set<Employee> employeeFriends = findFriends(); // find friends for current user
		if(employeeFriends.contains(optionalNewFriend.get())) {
			throw new Exception("Entity with id = " + employeeLoggedId + " is already friend with id = " + id);
		}
		
		
		optionalEmployee.get().getColleagues().add(optionalNewFriend.get());
		employeeRepository.save(optionalEmployee.get());
				
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalEmployee.get().getFirstname())
				.toUri();

		return ResponseEntity.created(location).build();
	}
	
	
	
	@PostMapping("/cancelRequest/{id}")
	public ResponseEntity<Object> cancelRequest(@PathVariable Long id) throws Exception {	
		
		Long employeeLoggedId = 72L;
		Optional<Employee> optionalEmployee = employeeRepository.findById((long) employeeLoggedId);
		Optional<Employee> optionalNewFriend = employeeRepository.findById((long) id);
		if (!optionalEmployee.isPresent())
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + employeeLoggedId);
		else if (!optionalNewFriend.isPresent())
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + id);
		
		Set<Employee> employeeRequests = employeeReceivedRequest(); // find friendship requests for this user
		if(!employeeRequests.contains(optionalNewFriend.get())) {
			throw new Exception("Entity with id = " + employeeLoggedId + " don't have a friend request from = " + id);
		}
		
		optionalNewFriend.get().getColleagues().remove(optionalEmployee.get());
		employeeRepository.save(optionalNewFriend.get());
				
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalNewFriend.get().getFirstname())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	
	@PostMapping("/acceptFriendShip/{id}")
	public ResponseEntity<Object> acceptFriendship(@PathVariable Long id) throws Exception {	
		
		Long employeeLoggedId = 72L;
		Optional<Employee> optionalEmployee = employeeRepository.findById((long) employeeLoggedId);
		Optional<Employee> optionalNewFriend = employeeRepository.findById((long) id);
		if (!optionalEmployee.isPresent())
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + employeeLoggedId);
		else if (!optionalNewFriend.isPresent())
			throw new ItinerarEntityNotFoundException("Entity not found with id-" + id);
		
		Set<Employee> employeeFriends = findFriends(); // find friends for current user
		Set<Employee> employeeRequests = employeeReceivedRequest(); // find friends for current user
		if(employeeFriends.contains(optionalNewFriend.get())) {
			throw new Exception("Entity with id = " + employeeLoggedId + " is already friend with id = " + id);
		}
		
		if(!employeeRequests.contains(optionalNewFriend.get())) {
			throw new Exception("Entity with id = " + employeeLoggedId + " don't have a friend request from = " + id);
		}
		optionalEmployee.get().getColleagues().add(optionalNewFriend.get());
		employeeRepository.save(optionalEmployee.get());
				
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalEmployee.get().getFirstname())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	
	@DeleteMapping(path = "/removeFriendShip/{id}")
	public void removeFriend(@PathVariable Long id) {
		Long employeeLoggedId = 72L;
		Employee employee = employeeRepository.findById((long) employeeLoggedId).get();
		Employee enemy = employeeRepository.findById((long) id).get();		
		employee.getColleagues().remove(enemy);
		employee.getTeammates().remove(enemy);
		employeeRepository.save(employee);
	}
	*/

}
