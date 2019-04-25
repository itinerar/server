package com.itinerar.teste;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.itinerar.controller.response.UploadFileResponse;
import com.itinerar.entity.Comment;
import com.itinerar.entity.Journey;
import com.itinerar.entity.Location;
import com.itinerar.entity.Photo;
import com.itinerar.entity.Post;
import com.itinerar.entity.User;
import com.itinerar.exception.EntityAlreadyExistException;
import com.itinerar.exception.ItinerarEntityNotFoundException;
import com.itinerar.repositories.CommentRepository;
import com.itinerar.repositories.JourneyRepository;
import com.itinerar.repositories.PhotoRepository;
import com.itinerar.repositories.UserRepository;
import com.itinerar.service.AuthenticationService;
import com.itinerar.service.DbPhotoStorageService;
import com.itinerar.repositories.PostRepository;

//@RestController
//@CrossOrigin
public class MemberController {
	/*
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	FriendRepository friendRepository;
	@Autowired
	EntityManager entityManager;
	
	@Transactional
	@GetMapping(path = "/members", produces=MediaType.APPLICATION_JSON_VALUE)
	   private  List<Member> findMembers() {
	        List<Member> list = memberRepository.findAll();
	        
	        Long memberId = 1L;
			Long requestId = 2L;
			Member authUser = memberRepository.findById(memberId).get();
			Member member = memberRepository.findById(requestId).get();
			Friendship friendship = new Friendship(authUser,member,true);
			
			String query = "insert into friendship values(34, true, ?, ?)";
			entityManager.createNativeQuery(query)
			   .setParameter(1,memberId).setParameter(2, requestId).executeUpdate(); 
			//friendRepository.addRelation(memberId, requestId);
	        return list;
	    }
	
	
/*	@GetMapping(path = "/request", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> requestFriend() {
		Long memberId = 1L;
		Long requestId = 2L;
		Member authUser = memberRepository.findById(memberId).get();
		Member member = memberRepository.findById(requestId).get();
		Friendship friendship = new Friendship(authUser,member,true);
		friendRepository.save(friendship);
		
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}") 
			.buildAndExpand(friendship.getId())
			.toUri(); 
		return ResponseEntity.created(location).build(); // to return a status code back
		
	}
	*/

	/*
	@GetMapping(path = "/member/friends", produces=MediaType.APPLICATION_JSON_VALUE)
	   private  List<Member> findMemberFriends() {
	        List<Friendship> list = friendRepository.findFriends("dvorik");
	        return list;
	    }
	*/
	/*
	 * @GetMapping(path = "/member/friends",
	 * produces=MediaType.APPLICATION_JSON_VALUE) private Set<Member>
	 * findMemberFriends() { Long memberId = 1L; Set<Friendship> friends =
	 * friendRepository.findAllFriends(memberId); //Set<Friendship> friends =
	 * (List<Friendship>) member.getFriends(); // return
	 * memberRepository.findById(memberId).get().getFriends(); }
	 */
	
	/*
	    private static void createFriendships(EntityManager em) {
	        List<Member> members = createMembers(em);

	        for (int i = 0; i < members.size(); i++) {
	            for (int j = 0; j < members.size(); j++) {
	                if (i != j) {
	                    createFriendship(em, members.get(i), members.get(j));
	                }
	            }
	        }
	    }

	    private static List<Member> createMembers(EntityManager em) {
	        List<Member> members = new ArrayList<>();
	        members.add(createMember(em, "Roberta", "Williams", "rwilliams"));
	        members.add(createMember(em, "Ken", "Williams", "kwilliams"));
	        members.add(createMember(em, "Dave", "Grossman", "dgrossman"));
	        members.add(createMember(em, "Tim", "Schafer", "tschafer"));
	        members.add(createMember(em, "Ron", "Gilbert", "rgilbert"));
	        return members;
	    }

	    private static Member createMember(EntityManager em, String fname, String lname, String username) {
	        Member m = new Member();
	        m.setFname(fname);
	        m.setLname(lname);
	        m.setUsername(username);
	        em.persist(m);
	        return m;
	    }

	    private static void createFriendship(EntityManager em, Member requester, Member friend) {
	        Friendship f = new Friendship();
	        f.setActive(true);
	        f.setDate(new Date());
	        f.setRequester(requester);
	        f.setFriend(friend);
	        em.persist(f);
	    }
	    
	    */

}
