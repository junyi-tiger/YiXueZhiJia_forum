package com.example.demo.controller;

import com.example.demo.ResourceAssembler.PostResourceAssembler;
import com.example.demo.entity.Post;
import com.example.demo.exception.NotFoundResourceException;
import com.example.demo.repository.PostRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

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
     * @return
     */
    @RequestMapping("/posts")
    public Resources<Resource<Post>> all(){
        List<Resource<Post>> posts= repository.findAll().stream()
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
     * 修改指定id的帖子
     * @param newPost
     * @param id
     * @return
     */
    @PutMapping("/posts/{id}")
    public Resource<Post> replacePost(@RequestBody Post newPost, @PathVariable Long id){
        return repository.findById(id)
                .map(post->{
                    post.setPTitle(newPost.getPTitle());
                    post.setPContent(newPost.getPContent());
                    post.setPState(newPost.getPState());
                    post.setPTag(newPost.getPTag());
                    return assembler.toResource(post);
                })
                .orElseGet(()->{
                    newPost.setPID(id);
                    return assembler.toResource(repository.save(newPost));
                });
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