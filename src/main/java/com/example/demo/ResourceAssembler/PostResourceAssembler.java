package com.example.demo.ResourceAssembler;

import com.example.demo.controller.PostController;
import com.example.demo.entity.Post;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class PostResourceAssembler implements ResourceAssembler<Post, Resource<Post>> {
    @Override
    public Resource<Post> toResource(Post post) {
        return new Resource<>(post,
                linkTo(methodOn(PostController.class).one(post.getPID())).withSelfRel(),
                linkTo(methodOn(PostController.class).all()).withRel("posts"));
    }
}
