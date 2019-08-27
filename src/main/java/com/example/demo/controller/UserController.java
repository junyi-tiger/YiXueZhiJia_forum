package com.example.demo.controller;

import com.example.demo.ResourceAssembler.UserAssembler;
import com.example.demo.entity.User;
import com.example.demo.entity.UserInfo;
import com.example.demo.exception.NotFoundResourceException;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @RestController indicates that the data returned by each method will be written straight into the response body instead of rendering a template.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;//用户repository
    private final UserAssembler assembler;

    /**
     * An UserRepository is injected by constructor into the controller.
     * @param repository userRepository
     * @param assembler userAssembler
     */
    public UserController(UserRepository repository, UserAssembler assembler){
        this.repository = repository;
        this.assembler = assembler;
    }

    /**
     * 获取指定id的用户
     * @param id 用户id
     * @return 该用户
     */
    @GetMapping("/{id}")
    public User one(@PathVariable Long id){
        User user = repository.findById(id)
                .orElseThrow(()->new NotFoundResourceException("user",id));
        user.add(linkTo(methodOn(UserController.class).one(id)).withSelfRel());
        user.add(linkTo(methodOn(UserController.class).all()).withRel("users"));
        return user;
    }

    /**
     * 用户登录验证
     * @return ResponseEntity<> 用户不存在则返回空，用户存在返回该用户
     */
    @PostMapping("/login")
    public ResponseEntity<User> verify(@RequestBody UserInfo userInfo){
        User user = new User();
        String name = userInfo.getName();
        String password = userInfo.getPassword();
        String email = userInfo.getEmail();
        if (name!=null)user.setUName(name);
        if (password!=null)user.setUPassword(password);
        if (email!=null)user.setUEmail(email);
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
     * @param user 用户信息
     * @return ResponseEntity
     */
    @PostMapping("/register")
    public ResponseEntity<User> newUser(@RequestBody User user){
        UserInfo userInfo = new UserInfo();
        userInfo.setName(user.getUName());
        userInfo.setPassword(user.getUPassword());
        userInfo.setEmail(user.getUEmail());
        ResponseEntity<User> return_User = verify(userInfo);
        if (return_User.getStatusCode()==HttpStatus.NOT_FOUND){
            //相同用户名或邮箱的用户都不存在，可以进行注册
            return new ResponseEntity<>(assembler.toResource(repository.save(user)), HttpStatus.OK);
        }else {
            //相同用户名或邮箱的用户已经存在，不能注册
            ResponseEntity<User> responseEntity = new ResponseEntity<>(HttpStatus.FORBIDDEN);
            return responseEntity;
        }
    }

    /**
     * 获取所有用户
     * @return 所有用户
     */
    @GetMapping("/")
    public List<User> all(){
        List<User> users = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return users;
    }

    /**
     * 修改指定id的用户
     * @param newUser   新用户的信息
     * @param id    原来用户的id
     * @return 修改后的用户
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> replaceUser(@RequestBody User newUser, @PathVariable Long id){
        User userResource = repository.findById(id)
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
     * @param id 要删除的用户id
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        repository.deleteById(id);
    }
}
