package com.example.demo.controller;

import com.example.demo.ResourceAssembler.CommentResourceAssembler;
import com.example.demo.entity.Comment;
import com.example.demo.exception.NotFoundResourceException;
import com.example.demo.repository.CommentRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CommentController {

    private final CommentRepository repository;
    private final CommentResourceAssembler assembler;

    public CommentController(CommentRepository repository, CommentResourceAssembler assembler){
        this.repository = repository;
        this.assembler = assembler;
    }

    /**
     * 获取指定id的评论
     * @param id
     * @return
     */
    @GetMapping("/comments/{id}")
    public Resource<Comment> one(@PathVariable Long id){
        Comment comment = repository.findById(id)
                .orElseThrow(()->new NotFoundResourceException("comment",id));
        return assembler.toResource(comment);
    }

    /**
     * 获取所有评论
     * @return
     */
    @GetMapping("/comments")
    public Resources<Resource<Comment>> all(){
        List<Resource<Comment>> comments = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(comments, linkTo(methodOn(CommentController.class).all()).withSelfRel());
    }

    /**
     * 新增评论
     * @param comment
     * @return
     */
    @PostMapping("/comments")
    public Resource<Comment> newComment(@RequestBody Comment comment){
        return assembler.toResource(repository.save(comment));
    }

    /**
     * 删除评论
     * @param id
     */
    @DeleteMapping("/comments/{id}")
    public void deleteComment(@PathVariable Long id){
        repository.deleteById(id);
    }

    /**
     * 获取某帖子（id）的所有评论
     */
    @RequestMapping("/posts/{post_id}/comments")
    public Resources<Resource<Comment>> comments_of_post_id(@PathVariable Long post_id){
        Comment comment = new Comment();
        comment.setPID(post_id);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withIgnorePaths("CID", "UID", "CContent", "CLike_num", "C_reply_num", "comment_time");
        Sort sort = new Sort(Sort.Direction.DESC, "comment_time");
        Example<Comment> example = Example.of(comment,exampleMatcher);
        List<Resource<Comment>> comments =  repository.findAll(example).stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(comments, linkTo(methodOn(CommentController.class).all()).withSelfRel());
    }

}
