package com.example.demo.ResourceAssembler;

import com.example.demo.controller.CollectionController;
import com.example.demo.entity.Collection;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class CollectionResourceAssembler implements ResourceAssembler<Collection, Resource<Collection>> {
    @Override
    public Resource<Collection> toResource(Collection collection) {
        return new Resource<>(collection,
                linkTo(methodOn(CollectionController.class).one(collection.getId())).withSelfRel(),
                linkTo(methodOn(CollectionController.class).all()).withRel("collections"));
    }
}
