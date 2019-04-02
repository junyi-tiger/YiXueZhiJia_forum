# 接口文档(API DOCUMENTS)
## 用户可能的行为分析
### 1.登录、注册
    1.登录    √
    method: post
    url:    /users/login
    params: 1:String name, 2:String password, 3:String email
    return: User对象
    2.注册    √
    method: post
    url:    /users/register
    params: User user(String UName,String UPassword,String UEmail,int USex)
    return: User对象
    >> 步骤：查询是否有重复的用户名和Email->无重复则进行注册
### 2.浏览帖子
    # 分页功能暂时不做
    1.显示所有帖子    √
    method: request
    url:    /posts
    2.显示特定id的帖子 √
    method: request
    url:    /posts/{id}
    params: id
    3.显示特定id帖子的评论   √
    method: get
    url:    /posts/{post_id}/comments
    params: post_id
    4.显示特定id评论的回复   √
    url:    /comments/{comment_id}/replies_to_comment