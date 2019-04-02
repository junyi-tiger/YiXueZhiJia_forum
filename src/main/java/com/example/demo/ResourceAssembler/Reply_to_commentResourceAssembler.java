package com.example.demo.ResourceAssembler;

import com.example.demo.controller.PostController;
import com.example.demo.controller.Reply_to_commentController;
import com.example.demo.entity.Reply_to_comment;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class Reply_to_commentResourceAssembler implements ResourceAssembler<Reply_to_comment,Resource<Reply_to_comment>> {

    @Override
    public Resource<Reply_to_comment> toResource(Reply_to_comment reply_to_comment) {
        return new Resource<>(reply_to_comment,
                linkTo(methodOn(Reply_to_commentController.class).one(reply_to_comment.getRID())).withSelfRel(),
                linkTo(methodOn(Reply_to_commentController.class).all()).withRel("replies_to_comment"));
    }
}
