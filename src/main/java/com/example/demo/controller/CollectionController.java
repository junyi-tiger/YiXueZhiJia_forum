package com.example.demo.controller;

import com.example.demo.ResourceAssembler.CollectionResourceAssembler;
import com.example.demo.entity.Collection;
import com.example.demo.entity.Post;
import com.example.demo.exception.NotFoundResourceException;
import com.example.demo.repository.CollectionRepository;
import com.example.demo.repository.PostRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CollectionController {

    private final CollectionRepository repository;
    private final CollectionResourceAssembler assembler;
    private final PostRepository postRepository;

    public CollectionController(CollectionRepository repository,
                                CollectionResourceAssembler assembler,
                                PostRepository postRepository){
        this.repository = repository;
        this.assembler = assembler;
        this.postRepository = postRepository;
    }

    /**
     * 获取指定id的收藏
     * @param id
     * @return
     */
    @GetMapping("/collections/{id}")
    public Resource<Collection> one(@PathVariable Long id){
        Collection collection = repository.findById(id)
                .orElseThrow(()->new NotFoundResourceException("collection",id));
        return assembler.toResource(collection);
    }

    /**
     * 获取所有收藏
     * @return
     */
    @GetMapping("/collections")
    public Resources<Resource<Collection>> all(){
        List<Resource<Collection>> resources = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(resources,linkTo(methodOn(CollectionController.class).all()).withSelfRel());
    }

    /**
     * 添加收藏，同时帖子的收藏量+1
     * @param collection
     * @return
     */
    @PostMapping("/collections")
    @ResponseBody
    public ResponseEntity<Resource<Collection>> newCollection(@RequestBody Collection collection){
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withIgnorePaths("id","collect_time");
        Example<Collection> example = Example.of(collection,exampleMatcher);
        if (repository.exists(example)){
            //已经收藏过了
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Long pid = collection.getPID();
        Post post = postRepository.findById(pid).get();
        post.setPCollection_num(post.getPCollection_num()+1);
        postRepository.save(post);
        return new ResponseEntity<>(assembler.toResource(repository.save(collection)), HttpStatus.OK);
    }

    /**
     * 删除收藏,同时帖子的收藏量-1
     */
    @DeleteMapping("/collections")
    public void deleteCollection(@RequestBody Collection collection){
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withIgnorePaths("id","collect_time");
        Example<Collection> example = Example.of(collection,exampleMatcher);
        if (repository.exists(example)){
            Collection collection1 = repository.findOne(example).get();
            Long cid = collection1.getId();
            repository.deleteById(cid);
            Long pid = collection1.getPID();
            Post post = postRepository.findById(pid).get();
            post.setPCollection_num(post.getPCollection_num()-1);
            postRepository.save(post);
        }

    }

    /**
     * 获取某用户的所有收藏内容
     * 按时间倒序排列
     * @param uid
     * @return
     */
    @GetMapping("/{uid}/collections")
    public Resources<Resource<Collection>> all_of_user(@PathVariable Long uid){
        Collection collection = new Collection();
        collection.setUID(uid);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withIgnorePaths("id","PID","collect_time");
        Example<Collection> example = Example.of(collection,exampleMatcher);
        List<Collection> all_collections = repository.findAll(example);
        Collections.sort(all_collections, new Comparator<Collection>() {
            @Override
            public int compare(Collection o1, Collection o2) {
                return o2.getCollect_time().compareTo(o1.getCollect_time());
            }
        });
        List<Resource<Collection>> collections = all_collections.stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(collections,linkTo(methodOn(CollectionController.class).all()).withSelfRel());
    }
}