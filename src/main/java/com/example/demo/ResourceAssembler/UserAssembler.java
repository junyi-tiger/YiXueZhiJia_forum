package com.example.demo.ResourceAssembler;

import com.example.demo.controller.UserController;
import com.example.demo.entity.User;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UserAssembler implements Assembler<User> {
    @Override
    public User toResource(User user) {
        user.add(linkTo(methodOn(UserController.class).one(user.getUId())).withSelfRel());
        user.add(linkTo(methodOn(UserController.class).all()).withRel("users"));
        return user;
    }

}
