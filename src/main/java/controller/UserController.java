package controller;

import annotation.Auth;
import domain.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import service.UserService;

import java.util.HashMap;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ResponseBody
    @ApiOperation(value = "회원가입", notes = "계정, 비번, 닉네임")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody User user){
        return userService.signUp(user)
                ? new ResponseEntity<String>("success", HttpStatus.CREATED)
                : new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ApiOperation(value = "로그인", notes = "계정, 비번")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<HashMap<String, String>> login(@RequestBody User user) throws Exception {
        return new ResponseEntity<HashMap<String, String>>(userService.login(user), HttpStatus.OK);
    }

    @Auth
    @ResponseBody
    @ApiOperation(value = "계정확인", notes = "", authorizations = @Authorization(value = "Authorization"))
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public ResponseEntity<User> me() throws Exception{
        return new ResponseEntity<User>(userService.getUser(), HttpStatus.OK);
    }

    @Auth
    @ResponseBody
    @ApiOperation(value = "회원탈퇴", notes = "", authorizations = @Authorization(value = "Authorization"))
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete() throws Exception{
        return userService.deleteAccount()
                ? new ResponseEntity<String>("success", HttpStatus.OK)
                : new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Auth
    @ResponseBody
    @ApiOperation(value = "정보수정", notes = "닉네임", authorizations = @Authorization(value = "Authorization"))
    @RequestMapping(value = "/", method = RequestMethod.PATCH)
    public ResponseEntity<String> update(@RequestBody User user) throws Exception{
        return userService.updateAccount(user)
                ? new ResponseEntity<String>("success", HttpStatus.OK)
                : new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
