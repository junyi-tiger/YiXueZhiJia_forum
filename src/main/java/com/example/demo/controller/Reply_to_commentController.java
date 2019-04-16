package com.example.demo.controller;

import com.example.demo.ResourceAssembler.Reply_to_commentResourceAssembler;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Reply_to_comment;
import com.example.demo.exception.NotFoundResourceException;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.Reply_to_commentRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class Reply_to_commentController {

    private final Reply_to_commentRepository repository;
    private final Reply_to_commentResourceAssembler assembler;
    private final CommentRepository commentRepository;

    public Reply_to_commentController(Reply_to_commentRepository reply_to_commentRepository,
                                      Reply_to_commentResourceAssembler reply_to_commentResourceAssembler,
                                      CommentRepository commentRepository){
        repository = reply_to_commentRepository;
        assembler = reply_to_commentResourceAssembler;
        this.commentRepository = commentRepository;
    }

    /**
     * 获取指定id的回复
     * @param id
     * @return
     */
    @GetMapping("/reply_to_comments/{id}")
    public Resource<Reply_to_comment> one(@PathVariable Long id){
        Reply_to_comment reply_to_comment =  repository.findById(id)
                .orElseThrow(()->new NotFoundResourceException("reply_to_comment",id));
        return assembler.toResource(reply_to_comment);
    }

    /**
     * 获取所有回复
     * @return
     */
    @GetMapping("/reply_to_comments")
    public Resources<Resource<Reply_to_comment>> all(){
        List<Resource<Reply_to_comment>> replies = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(replies, linkTo(methodOn(Reply_to_commentController.class).all()).withSelfRel());
    }

    /**
     * 添加新回复
     * @param reply_to_comment
     * @return
     */
    @PostMapping("/reply_to_comments")
    public Resource<Reply_to_comment> newReply(@RequestBody Reply_to_comment reply_to_comment){
        Comment comment = commentRepository.findById(reply_to_comment.getCID()).get();
        comment.setC_reply_num(comment.getC_reply_num()+1);
        commentRepository.save(comment);
        return assembler.toResource(repository.save(reply_to_comment));
    }

    /**
     * 删除回复（同时评论的回复数量-1）
     * @param id
     */
    @DeleteMapping("/reply_to_comments/{id}")
    public void deleteReply(@PathVariable Long id){
        Reply_to_comment reply_to_comment = repository.findById(id).get();
        Long cid = reply_to_comment.getCID();
        Comment comment = commentRepository.findById(cid).get();
        comment.setC_reply_num(comment.getC_reply_num()-1);
        commentRepository.save(comment);
        repository.deleteById(id);
    }

    /**
     * 以下是一些常用操作
     */
    //查询并返回某评论（由id指定）的所有回复(按回复时间排序）
    @GetMapping("/comments/{comment_id}/replies_to_comment")
    public Resources<Resource<Reply_to_comment>> replies_to_comment(@PathVariable Long comment_id){
        Reply_to_comment reply_to_comment = new Reply_to_comment();
        reply_to_comment.setCID(comment_id);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnorePaths("RID", "UID", "RContent", "reply_time")
                .withIgnoreNullValues();
        Example<Reply_to_comment> example = Example.of(reply_to_comment, exampleMatcher);
        List<Reply_to_comment> replies = repository.findAll(example);
        Collections.sort(replies, (o1,o2)->{
            //使用java 8 lamda表达式 代替 new Comparator {...}
                return o2.getReply_time().compareTo(o1.getReply_time());
        });
        List<Resource<Reply_to_comment>> resources = replies.stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(resources,linkTo(methodOn(Reply_to_commentController.class).all()).withSelfRel());
    }
}
