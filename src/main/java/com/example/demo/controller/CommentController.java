package com.example.demo.controller;

import com.example.demo.ResourceAssembler.CommentResourceAssembler;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.exception.NotFoundResourceException;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import javafx.geometry.Pos;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

//@RestController
public class CommentController {

    private final CommentRepository repository;
    private final CommentResourceAssembler assembler;
    private final PostRepository postRepository;

    public CommentController(CommentRepository repository, CommentResourceAssembler assembler, PostRepository postRepository){
        this.repository = repository;
        this.assembler = assembler;
        this.postRepository = postRepository;
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
     * 新增评论（同时帖子评论量+1）
     * @param comment
     * @return
     */
    @PostMapping("/comments")
    public Resource<Comment> newComment(@RequestBody Comment comment){
        Post post = postRepository.findById(comment.getPID()).get();
        post.setPComments(post.getPComments()+1);
        postRepository.save(post);
        return assembler.toResource(repository.save(comment));
    }

    /**
     * 删除评论（同时帖子评论量-1、删除评论的点赞(LikeComment)、回复(Reply_to_comment)）——"待优化"
     * @param id
     */
    @DeleteMapping("/comments/{id}")
    public void deleteComment(@PathVariable Long id){
        Comment comment = repository.findById(id).get();
        Post post = postRepository.findById(comment.getPID()).get();
        post.setPComments(post.getPComments()-1);
        postRepository.save(post);
        repository.deleteById(id);
    }

    /**
     * 获取某帖子（id）的所有评论
     * 按点赞数、评论时间依次排列
     */
    @GetMapping("/posts/{post_id}/comments")
    public Resources<Resource<Comment>> comments_of_post_id(@PathVariable Long post_id){
        Comment comment = new Comment();
        comment.setPID(post_id);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withIgnorePaths("CID", "UID", "CContent", "CLike_num", "C_reply_num", "comment_time");
        Example<Comment> example = Example.of(comment,exampleMatcher);
        List<Comment> all_comments = repository.findAll(example);
        Collections.sort(all_comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                if (o2.getCLike_num()-o1.getCLike_num()==0)
                    return o2.getComment_time().compareTo(o1.getComment_time());
                return o2.getCLike_num()-o1.getCLike_num();
            }
        });
        List<Resource<Comment>> comments =  all_comments.stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(comments, linkTo(methodOn(CommentController.class).all()).withSelfRel());
    }

}
