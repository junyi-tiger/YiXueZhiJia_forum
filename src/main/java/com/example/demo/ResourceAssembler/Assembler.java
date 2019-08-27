package com.example.demo.ResourceAssembler;

import com.example.demo.entity.User;
import org.springframework.hateoas.ResourceSupport;

public interface Assembler<T extends ResourceSupport>{
    T toResource(T var1);
}
