package com.example.demo.ResourceAssembler;

import com.example.demo.controller.UserController;
import com.example.demo.entity.User;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, Resource<User>> {

    @Override
    public Resource<User> toResource(User user) {
        return new Resource<>(user,
                linkTo(methodOn(UserController.class).one(user.getUId())).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("users"));
    }
}
