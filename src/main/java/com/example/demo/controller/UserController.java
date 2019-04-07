package com.example.demo.controller;

import com.example.demo.ResourceAssembler.UserResourceAssembler;
import com.example.demo.entity.User;
import com.example.demo.exception.NotFoundResourceException;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class UserController {

    private final UserRepository repository;//用户repository
    private final UserResourceAssembler assembler;//用户ResourceAssembler

    public UserController(UserRepository repository, UserResourceAssembler assembler){
        this.repository = repository;
        this.assembler = assembler;
    }

    /**
     * 获取指定id的用户
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    public Resource<User> one(@PathVariable Long id){
        User user = repository.findById(id)
                .orElseThrow(()->new NotFoundResourceException("user",id));
        return assembler.toResource(user);
    }

    /**
     * 用户登录验证
     * @param name
     * @param password
     * @param email
     * @return
     */
    @PostMapping("/users/login")
    @ResponseBody
    public ResponseEntity<Resource<User>> verify(@Nullable @RequestParam String name, @RequestParam String password, @Nullable @RequestParam String email){
        User user = new User();
        user.setUName(name);
        user.setUPassword(password);
        user.setUEmail(email);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                //忽略大小写
                .withIgnoreCase()
                //忽略register_time等字段
                .withIgnorePaths("register_time","UId","USex","UHead","UState","URole")
//                //忽略为空字段
                .withIgnoreNullValues();
        Example<User> example = Example.of(user, exampleMatcher);
        List<User> users = repository.findAll(example);
        if (users==null||users.isEmpty())return new ResponseEntity<>(HttpStatus.NOT_FOUND);//未找到用户
        return new ResponseEntity<>(assembler.toResource(users.get(0)),HttpStatus.OK);//登录成功
    }

    /**
     * 新增用户/用户注册
     * @param user
     * @return
     */
    @PostMapping("/users/register")
    public ResponseEntity<Resource<User>> newUser(@RequestBody User user){
        ResponseEntity<Resource<User>> return_User = verify(user.getUName(),user.getUPassword(),null);
        ResponseEntity<Resource<User>> return_User2 = verify(null,user.getUPassword(),user.getUEmail());
        if (return_User.getStatusCode()==HttpStatus.NOT_FOUND&&return_User2.getStatusCode()==HttpStatus.NOT_FOUND){
            //相同用户名或邮箱的用户都不存在，可以进行注册
            return new ResponseEntity<>(assembler.toResource(repository.save(user)),HttpStatus.OK);
        }else {
            //相同用户名或邮箱的用户已经存在，不能注册
            ResponseEntity<Resource<User>> responseEntity = new ResponseEntity<>(HttpStatus.FORBIDDEN);
            return responseEntity;
        }
    }

    /**
     * 获取所有用户
     * @return
     */
    @GetMapping("/users")
    public Resources<Resource<User>> all(){
        List<Resource<User>> users = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(users,linkTo(DummyInvocationUtils.methodOn(UserController.class).all()).withSelfRel());
    }

    /**
     * 修改指定id的用户
     * @param newUser   新用户的信息
     * @param id    原来用户的id
     * @return
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<Resource<User>> replaceUser(@RequestBody User newUser, @PathVariable Long id){
        Resource<User> userResource = repository.findById(id)
                .map(user -> {
                    user.setUName(newUser.getUName());
                    user.setUPassword(newUser.getUPassword());
                    user.setUEmail(newUser.getUEmail());
                    //用户注册时间不用修改
//                    user.setRegister_time(newUser.getRegister_time());
                    user.setUHead(newUser.getUHead());
                    user.setUSex(newUser.getUSex());
                    user.setUState(newUser.getUState());
                    user.setURole(newUser.getURole());
                    return assembler.toResource(repository.save(user));
                })
                .orElseThrow(()->new NotFoundResourceException("user",id));
        return new ResponseEntity<>(userResource, HttpStatus.OK);
    }

    /**
     * 删除指定id的用户
     * @param id
     */
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id){
        repository.deleteById(id);
    }
}
