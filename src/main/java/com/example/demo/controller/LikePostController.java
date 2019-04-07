package com.example.demo.controller;

import com.example.demo.entity.LikePost;
import com.example.demo.entity.Post;
import com.example.demo.exception.NotFoundResourceException;
import com.example.demo.repository.LikePostRepository;
import com.example.demo.repository.PostRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LikePostController {

    private final LikePostRepository repository;
    private final PostRepository postRepository;

    public LikePostController(LikePostRepository repository, PostRepository postRepository){
        this.repository = repository;
        this.postRepository = postRepository;
    }

    @GetMapping("/likeposts")
    public List<LikePost> all(){
        return repository.findAll();
    }


    @GetMapping("/likeposts/{id}")
    public LikePost one(@PathVariable Long id){
        return repository.findById(id).get();
    }

    /**
     * 获取某帖子的点赞量
     * @param id
     * @return
     */
    @GetMapping("/posts/{id}/likeposts")
    public int getTotalNumsOfPost(@PathVariable Long id){
        LikePost likePost = new LikePost();
        likePost.setPID(id);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withIgnorePaths("id","UID","like_time");
        Example<LikePost> example = Example.of(likePost,exampleMatcher);
        return repository.findAll(example).size();
    }

    /**
     * 用户为某帖子点赞（同时帖子的赞数+1）
     * @param likePost
     * @return
     */
    @PostMapping("/likeposts")
    @ResponseBody
    public ResponseEntity<LikePost> newLikePost(@RequestBody LikePost likePost){
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withIgnorePaths("like_time","id");
        Example<LikePost> example = Example.of(likePost,exampleMatcher);
        if (repository.exists(example)){
            //以及点过赞了，返回403禁止访问
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Long pid = likePost.getPID();
        Post post = postRepository.findById(pid).get();
        post.setPLike_num(post.getPLike_num()+1);
        postRepository.save(post);
        return new ResponseEntity<>(repository.save(likePost),HttpStatus.OK);
    }

    /**
     * 用户取消点赞（同时帖子的赞数-1）
     * @param likePost
     */
    @DeleteMapping("/likeposts")
    public void deleteLikePost(@RequestBody LikePost likePost){
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withIgnorePaths("id","like_time");
        Example<LikePost> example = Example.of(likePost,exampleMatcher);
        if (repository.exists(example)){
            //存在，则删除
            LikePost likePost1 = repository.findOne(example).get();
            Long pid = likePost1.getPID();
            Post post = postRepository.findById(pid).get();
            post.setPLike_num(post.getPLike_num()-1);
            postRepository.save(post);
            repository.deleteById(likePost1.getId());
        }
    }
}