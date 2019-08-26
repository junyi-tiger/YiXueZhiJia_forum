package com.example.demo.repository;

import com.example.demo.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @RepositoryRestResource注解： 引导Spring MVC去创建RESTful endpoints at $path
 * 这个注解并不是必须的：但它可以改变export的细节：例如将默认的"collections"改为其他内容（如"collection"）
 */
@RepositoryRestResource(collectionResourceRel = "collection", path = "collections")
public interface CollectionRepository extends JpaRepository<Collection, Long> {
}
