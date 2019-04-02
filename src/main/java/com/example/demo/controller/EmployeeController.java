package com.example.demo.controller;

import com.example.demo.ResourceAssembler.EmployeeResourceAssembler;
import com.example.demo.entity.Employee;
import com.example.demo.exception.NotFoundResourceException;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;
    private final EmployeeResourceAssembler assembler;

    public EmployeeController(EmployeeRepository repository, EmployeeResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/employees/{id}")
    public Resource<Employee> one(@PathVariable Long id){
        Employee employee = repository.findById(id)
                .orElseThrow(()->new NotFoundResourceException("employee",id));
        return assembler.toResource(employee);
    }

    @GetMapping("/employees")
    public Resources<Resource<Employee>> all(){
        List<Resource<Employee>> employees = repository.findAll().stream()
                .map(assembler::toResource) //java 8 method references
                .collect(Collectors.toList());
        return new Resources<>(employees,linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @PostMapping("/employees")
    public Resource<Employee> newEmployee(@RequestBody Employee newEmployee) {
        return assembler.toResource(repository.save(newEmployee));
    }

    @PutMapping("/employees/{id}")
    public Resource<Employee> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id){
        return repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return assembler.toResource(repository.save(employee));
                })
                .orElseGet(()->{
                    newEmployee.setId(id);
                    return assembler.toResource(repository.save(newEmployee)) ;
                });
    }

    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(@PathVariable Long id){
        repository.deleteById(id);
    }
}
