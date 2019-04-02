package com.example.demo.loader;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

    /**
     * 初次启动用于预加载数据库表
     * @param employeeRepository
     * @param userRepository
     * @param postRepository
     * @param commentRepository
     * @param reply_to_commentRepository
     * @return
     */
    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository,
                                   UserRepository userRepository,
                                   PostRepository postRepository,
                                   CommentRepository commentRepository,
                                   Reply_to_commentRepository reply_to_commentRepository){
        return args -> {
            Employee employee = new Employee("Weson", "CEO");
            employee.setId(1L);
            log.info("Preloading " + employeeRepository.save(employee));
            User user = new User("放空的心",
                    "123456","yijun0226@foxmail.com",1);
            user.setUId(1L);
            log.info("Preloading " + userRepository.save(user));
            Post post = new Post(1L,
                    "This is the Title","Hello, this is the content");
            post.setUId(1L);
            log.info("Preloading " + postRepository.save(post));
            Comment comment = new Comment(1L,
                    1L,"This is a comment");
            comment.setCID(1L);
            log.info("Preloading " + commentRepository.save(comment));
            Reply_to_comment reply_to_comment = new Reply_to_comment(1L,1L,"这是一条对评论的回复");
            reply_to_comment.setRID(1L);
            log.info("Preloading " + reply_to_commentRepository.save(reply_to_comment));
        };
    }
}
