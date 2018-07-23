package br.com.nextel.shorturl.controller;

import br.com.nextel.shorturl.domain.entity.UserEntity;
import br.com.nextel.shorturl.domain.vo.URLStatsVO;
import br.com.nextel.shorturl.domain.vo.UserVO;
import br.com.nextel.shorturl.exception.BusinessException;
import br.com.nextel.shorturl.service.URLService;
import br.com.nextel.shorturl.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "User API", produces = "application/json")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private URLService urlService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/users/{userId}/stats")
    @ApiOperation(value = "Redirects to the full URL", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "URL Statistics by user"),
            @ApiResponse(code = 404, message = "If userId is unknown")
    })
    public ResponseEntity<URLStatsVO> getStats(HttpServletRequest request, @PathVariable("userId") final Long userId) {
        final String serverURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        if (!userService.isExistingUser(userId))
            return new ResponseEntity<>(new URLStatsVO(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(urlService.getStatsByUser(serverURL, userId), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users")
    @ApiOperation(value = "Save new User", response = UserVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created"),
            @ApiResponse(code = 409, message = "User already exists")
    })
    public ResponseEntity<String> add(@RequestBody final UserVO user) {
        try {
            userService.add(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{userId}")
    @ApiOperation(value = "Removes a User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successful executed")
    })
    public void del(@PathVariable("userId") Long id) {
        userService.remove(id);
    }
}