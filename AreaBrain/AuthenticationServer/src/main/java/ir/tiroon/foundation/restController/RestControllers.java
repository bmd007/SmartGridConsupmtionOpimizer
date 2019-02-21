package ir.tiroon.foundation.restController;


import ir.tiroon.foundation.model.userManagement.User;
import ir.tiroon.foundation.service.userManagement.UserServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RestController
public class RestControllers {

    @Autowired
    UserServices userServices;


    @GetMapping("/users")
    public List<User> all() {
        return userServices.getEntityList();
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> createEmployee(@RequestBody User user) {

        ResponseEntity entity;

        if (user == null)
            entity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        else
              entity = new ResponseEntity(user,HttpStatus.CREATED);

        return entity;
    }

}
