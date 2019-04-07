package com.example.demo.ResourceAssembler;

import com.example.demo.controller.CommentController;
import com.example.demo.entity.Comment;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class CommentResourceAssembler implements ResourceAssembler<Comment, Resource<Comment>> {

    @Override
    public Resource<Comment> toResource(Comment comment) {
        return new Resource<>(comment,
                linkTo(methodOn(CommentController.class).one(comment.getCID())).withSelfRel(),
                linkTo(methodOn(CommentController.class).all()).withRel("comments"));
    }
}