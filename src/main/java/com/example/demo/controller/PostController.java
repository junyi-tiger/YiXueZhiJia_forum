package com.example.demo.controller;

import com.example.demo.ResourceAssembler.PostResourceAssembler;
import com.example.demo.entity.Post;
import com.example.demo.exception.NotFoundResourceException;
import com.example.demo.repository.PostRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * 帖子对应的controller
 */
@RestController
public class PostController {

    private final PostRepository repository;
    private final PostResourceAssembler assembler;

    public PostController(PostRepository repository, PostResourceAssembler assembler){
        this.repository = repository;
        this.assembler = assembler;
    }

    /**
     * 获取指定id的帖子
     * @param id
     * @return
     */
    @RequestMapping("/posts/{id}")
    public Resource<Post> one(@PathVariable Long id){
        Post post = repository.findById(id)
                .orElseThrow(()->new NotFoundResourceException("post",id));
        return assembler.toResource(post);
    }

    /**
     * 获取所有帖子
     * 按帖子发表时间倒序
     * @return
     */
    @RequestMapping("/posts")
    public Resources<Resource<Post>> all(){
        List<Post> all_posts = repository.findAll();
        Collections.sort(all_posts, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return o2.getPost_time().compareTo(o1.getPost_time());
            }
        });
        List<Resource<Post>> posts= all_posts.stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(posts,linkTo(methodOn(PostController.class).all()).withSelfRel());
    }

    /**
     * 新增帖子
     * @param post
     * @return
     */
    @PostMapping("/posts")
    public Resource<Post> newPost(@RequestBody Post post){
        return assembler.toResource(repository.save(post));
    }

    /**
     * 删除帖子
     * @param id
     */
    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Long id){
        repository.deleteById(id);
    }
}