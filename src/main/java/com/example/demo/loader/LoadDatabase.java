package com.example.demo.loader;

import com.example.demo.ResourceAssembler.*;
import com.example.demo.controller.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

    /**
     * 初次启动用于预加载数据库表
     * @param userRepository
     * @param postRepository
     * @param commentRepository
     * @param reply_to_commentRepository
     * @return
     */
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   UserResourceAssembler userResourceAssembler,
                                   PostRepository postRepository,
                                   PostResourceAssembler postResourceAssembler,
                                   CommentRepository commentRepository,
                                   CommentResourceAssembler commentResourceAssembler,
                                   Reply_to_commentRepository reply_to_commentRepository,
                                   Reply_to_commentResourceAssembler reply_to_commentResourceAssembler,
                                   CollectionRepository collectionRepository,
                                   CollectionResourceAssembler collectionResourceAssembler,
                                   LikePostRepository likePostRepository,
                                   LikeCommentRepository likeCommentRepository){
        return args -> {
            //init 用户
            User user = new User("放空的心", "123456","yijun0226@foxmail.com",1);
            user.setUHead("3");
            user.setUId(1L);
            //init 帖子
            Post post = new Post(1L, "This is the Title","Hello, this is the content");
            post.setUId(1L);
            //init 评论
            Comment comment = new Comment(1L, 1L,"This is a comment");
            comment.setCID(1L);
            //init 回复
            Reply_to_comment reply_to_comment = new Reply_to_comment(1L,1L,"这是一条对评论的回复");
            reply_to_comment.setRID(1L);
            //init 收藏
            Collection collection = new Collection(1L,1L);
            collection.setId(1L);
            //init 点赞帖子
            LikePost likePost = new LikePost(1L,1L);
            likePost.setId(1L);
            //init 点赞评论
            LikeComment likeComment = new LikeComment(1L,1L);
            likeComment.setId(1L);

            UserController userController = new UserController(userRepository,userResourceAssembler);
            userController.newUser(user);
            PostController postController = new PostController(postRepository, postResourceAssembler);
            postController.newPost(post);
            CommentController commentController = new CommentController(commentRepository,commentResourceAssembler,postRepository);
            commentController.newComment(comment);
            Reply_to_commentController reply_to_commentController = new Reply_to_commentController(reply_to_commentRepository,reply_to_commentResourceAssembler,commentRepository);
            reply_to_commentController.newReply(reply_to_comment);
            CollectionController collectionController = new CollectionController(collectionRepository,collectionResourceAssembler,postRepository);
            collectionController.newCollection(collection);
            LikePostController likePostController = new LikePostController(likePostRepository,postRepository);
            likePostController.newLikePost(likePost);
            LikeCommentController likeCommentController = new LikeCommentController(likeCommentRepository,commentRepository);
            likeCommentController.newLikeComment(likeComment);
        };
    }
}
