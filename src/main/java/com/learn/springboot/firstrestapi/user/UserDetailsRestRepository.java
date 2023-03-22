package com.learn.springboot.firstrestapi.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="userdetails")
public interface UserDetailsRestRepository extends PagingAndSortingRepository<UserDetails, Long> {
	List<UserDetails> findByRole(String role);
}
