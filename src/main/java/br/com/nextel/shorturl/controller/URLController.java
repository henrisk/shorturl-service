package br.com.nextel.shorturl.controller;

import br.com.nextel.shorturl.domain.vo.URLStatsVO;
import br.com.nextel.shorturl.domain.vo.URLVO;
import br.com.nextel.shorturl.service.URLService;
import br.com.nextel.shorturl.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.codec.binary.Base32;
import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(value = "URL API", produces = "application/json")
public class URLController {

    private Logger logger = LoggerFactory.getLogger(URLController.class);

    @Autowired
    private URLService urlService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/urls/{id}")
    @ApiOperation(value = "Redirects to the full URL")
    @ApiResponses(value = {
            @ApiResponse(code = 301, message = "Redirects to the full URL"),
            @ApiResponse(code = 404, message = "If shortURL is unknown")
    })
    public void getURL (HttpServletResponse response, @PathVariable("id") final Long id) {
        URLVO vo = urlService.findURLById(id);
        logger.info(vo.toString());
        if (vo == null || vo.getUrl() == null)
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        else {
            urlService.hitURL(vo.getId());
            logger.info("hit");
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        }

        response.setHeader("Location", vo.getUrl());
        response.setHeader("Connection", "close");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users/{userid}/urls")
    @ApiOperation(value = "Redirects to the full URL", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "URL Successful stored"),
            @ApiResponse(code = 403, message = "Forbidden User not found"),
            @ApiResponse(code = 422, message = "Invalid parameter(s)")
    })
    public ResponseEntity<URLVO> putURL (HttpServletRequest request, @PathVariable("userid") final Long userId,
            @RequestBody final URLVO url) {
        if (userId == null || !userService.isExistingUser(userId))
            return new ResponseEntity<>(new URLVO(), HttpStatus.NOT_FOUND);

        if (!this.isURLValid(url.getUrl()))
            return new ResponseEntity<>(new URLVO(), HttpStatus.UNPROCESSABLE_ENTITY);

        String serverPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        if (url.getShortUrl() == null)
            url.setShortUrl(this.generateShortURL(url.getUrl()));

        url.setUserId(userId);

        URLVO vo = urlService.addURL(url);
        vo.setShortUrl(serverPath + "/" + vo.getShortUrl());
        return new ResponseEntity<>(vo, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stats")
    @ApiOperation(value = "Shows the system access statistics", response = URLStatsVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "URL Statistics")
    })
    public URLStatsVO getStats (HttpServletRequest request) {
        return urlService
                .getStats(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stats/{id}")
    @ApiOperation(value = "Shows the system access statistics", response = URLStatsVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "URL Statistics")
    })
    public ResponseEntity<URLVO> getURLStats (HttpServletRequest request, @PathVariable("id") final Long id) {
        String serverPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        URLVO vo = urlService.findById(serverPath, id);
        if (vo == null)
            return new ResponseEntity<>(new URLVO(), HttpStatus.OK);

        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/urls/{id}")
    @ApiOperation(value = "Remove a URL", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfull removed")
    })
    public void del (@PathVariable("id") final Long id) {
        urlService.remove(id);
    }

    private boolean isURLValid (final String url) {
        URLValidator urlValidator = new URLValidator();
        return urlValidator.isValid(url, null);
    }

    private String generateShortURL (final String fullURL) {
        if (fullURL == null)
            return null;

        return new Base32().encodeAsString(fullURL.getBytes());
    }
}