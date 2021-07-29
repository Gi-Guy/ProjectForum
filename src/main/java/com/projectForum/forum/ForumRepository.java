package com.projectForum.forum;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ForumRepository extends JpaRepository<Forum, Integer> {

}
