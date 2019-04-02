package com.example.demo.controller;

import com.example.demo.ResourceAssembler.Reply_to_commentResourceAssembler;
import com.example.demo.entity.Reply_to_comment;
import com.example.demo.exception.NotFoundResourceException;
import com.example.demo.repository.Reply_to_commentRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class Reply_to_commentController {

    private final Reply_to_commentRepository repository;
    private final Reply_to_commentResourceAssembler assembler;

    public Reply_to_commentController(Reply_to_commentRepository reply_to_commentRepository,
                                      Reply_to_commentResourceAssembler reply_to_commentResourceAssembler){
        repository = reply_to_commentRepository;
        assembler = reply_to_commentResourceAssembler;
    }

    @RequestMapping("/reply_to_comments/{id}")
    public Resource<Reply_to_comment> one(@PathVariable Long id){
        Reply_to_comment reply_to_comment =  repository.findById(id)
                .orElseThrow(()->new NotFoundResourceException("reply_to_comment",id));
        return assembler.toResource(reply_to_comment);
    }

    @RequestMapping("/reply_to_comments")
    public Resources<Resource<Reply_to_comment>> all(){
        List<Resource<Reply_to_comment>> replies = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(replies, linkTo(methodOn(Reply_to_commentController.class).all()).withSelfRel());
    }

    @PostMapping("/reply_to_comments")
    public Resource<Reply_to_comment> newReply(@RequestBody Reply_to_comment reply_to_comment){
        return assembler.toResource(repository.save(reply_to_comment));
    }

    /**
     * 以下是一些常用操作
     */
    //查询并返回某评论（由id指定）的所有回复
    @PostMapping("/comments/{comment_id}/replies_to_comment")
    public Resources<Resource<Reply_to_comment>> replies_to_comment(@PathVariable Long comment_id){
        Reply_to_comment reply_to_comment = new Reply_to_comment();
        reply_to_comment.setCID(comment_id);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnorePaths("RID", "UID", "RContent", "reply_time")
                .withIgnoreNullValues();
        Example<Reply_to_comment> example = Example.of(reply_to_comment, exampleMatcher);
        Sort sort = new Sort(Sort.Direction.DESC, "reply_time");
        /*
        ！！！repository.findAll(example,sort)联合起来用会出现问题：被example忽略的属性无法用sort进行排序
         */
        List<Resource<Reply_to_comment>> replies = repository.findAll(example).stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(replies,linkTo(methodOn(Reply_to_commentController.class).all()).withSelfRel());
    }

}
