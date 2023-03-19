package com.learn.springboot.firstrestapi.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsInterface extends JpaRepository<UserDetails, Long> {

}
