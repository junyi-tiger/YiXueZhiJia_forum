package com.example.demo.controller;

import com.example.demo.entity.Comment;
import com.example.demo.entity.LikeComment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.LikeCommentRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LikeCommentController {
    LikeCommentRepository repository;
    CommentRepository commentRepository;

    public LikeCommentController(LikeCommentRepository repository,CommentRepository commentRepository){
        this.repository = repository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/likecomments")
    public List<LikeComment> all(){
        return repository.findAll();
    }
    @GetMapping("/likecomments/{id}")
    public LikeComment one(@PathVariable Long id){
        return repository.findById(id).get();
    }

    /**
     * 获取某评论的所有点赞数量
     * @param id
     * @return
     */
    @GetMapping("/comments/{id}/likecomments")
    public int getTotalNumsOfComment(@PathVariable Long id){
        LikeComment likeComment = new LikeComment();
        likeComment.setCID(id);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "UID", "like_num");
        Example<LikeComment> example = Example.of(likeComment,exampleMatcher);
        return repository.findAll(example).size();
    }

    /**
     * 新增给评论的点赞（同时评论的点赞量+1）
     * @param likeComment
     * @return
     */
    @PostMapping("/likecomments")
    @ResponseBody
    public ResponseEntity<LikeComment> newLikeComment(@RequestBody LikeComment likeComment){
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withIgnorePaths("id","like_time");
        Example<LikeComment> example = Example.of(likeComment,exampleMatcher);
        if (repository.exists(example)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Long cid = likeComment.getCID();
        Comment comment = commentRepository.findById(cid).get();
        comment.setCLike_num(comment.getCLike_num()+1);
        commentRepository.save(comment);
        return new ResponseEntity<>(repository.save(likeComment), HttpStatus.OK);
    }

    /**
     * 取消给评论的点赞（同时评论的点赞数量-1）
     * @param likeComment
     */
    @DeleteMapping("/likecomments")
    public void deleteLikeComment(@RequestBody LikeComment likeComment){
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withIgnorePaths("id","like_time");
        Example<LikeComment> example = Example.of(likeComment,exampleMatcher);
        if (repository.exists(example)){
            LikeComment likeComment1 = repository.findOne(example).get();
            Long cid = likeComment1.getCID();
            Comment comment = commentRepository.findById(cid).get();
            comment.setCLike_num(comment.getCLike_num()-1);
            commentRepository.save(comment);
            repository.deleteById(likeComment1.getId());
        }
    }
}
