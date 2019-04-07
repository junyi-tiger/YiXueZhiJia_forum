# 接口文档(API DOCUMENTS)
### 1.用户登录、注册
    1.登录
    method: post
    url:    /users/login
    params: 1:String name, 2:String password, 3:String email
    return: User对象
    2.注册
    method: post
    url:    /users/register
    params: User user(String UName,String UPassword,String UEmail,int USex)
    return: User对象
    >> 步骤：查询是否有重复的用户名和Email->无重复则进行注册
### 2.帖子
    1.显示所有帖子
    method: request
    url:    /posts
    2.显示特定id的帖子
    method: request
    url:    /posts/{id}
    params: id
    5.增加帖子
    method: post
    url:    /posts
    params: Post对象
    6.删除帖子(删除帖子，同时删除帖子下属的点赞（LikePost）、收藏（Collection）、
    评论（Comment）、评论的点赞（LikeComment）、评论的回复（Reply_to_comment）
    method: delete
    url:    /posts/{id}
    params: id
### 3.评论
    1.评论帖子（同时帖子评论数+1）
    method: post
    url:    /reply_to_comments
    params: Reply_to_comment对象
    2.删除帖子的评论（同时帖子评论数-1）
    method: delete
    url:    /reply_to_comments/{id}
    params: id
    3.显示特定id帖子的评论
    method: get
    url:    /posts/{post_id}/comments
    params: post_id
### 4.回复
    1.显示特定id评论的回复
    method: get
    url:    /comments/{comment_id}/replies_to_comment
    params: cid
    2.新增回复
    method: post
    url:    /reply_to_comments
    params: Reply_to_comment(cid,uid,rcontent)
    3.删除回复
    method: delete
    url:    /reply_to_comments/{id}
    params: rid
    
### 5.收藏
    1.收藏帖子（...)
        method: post
        url:    /collections
        params: Collection对象
    2.取消收藏(...)
        method: delete
        url:    /collections/{id}
        params: id
    3.获取某用户的所有收藏内容
        method: get
        url:    /{uid}/collections
        params: uid
    
### 6.点赞
    -- 帖子 --
    1.获取某帖子的点赞量
    method: get
    url:    /posts/{id}/likeposts
    params: pid
    2.为帖子点赞
    method: post
    url:    /likeposts
    parmas: LikePost(pid,uid)
    3.取消点赞
    method: delete
    url:    /likeposts
    parmas: LikePost(pid,uid)
    -- 评论 --
    1.获取某评论的点赞量
    method: get
    url:    /comments/{id}/likecomments
    params: cid
    2.为评论点赞
    method: post
    url:    /likecomments
    params: LikeComment(uid,cid)
    3,取消点赞
    method: delete
    url:    /likecomments
    params: LikeComment(uid,cid)