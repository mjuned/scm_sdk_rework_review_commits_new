package com.crossover.trial.weather.endpoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class TestController {

    @GET
    @Path("/testIndex")
    public String testEndPoint() {
        return "Greetings from from the test project";
    }
    
    @GET
    @Path("/testSample")
    public String testSample() {
        return "Test1";
    }
    
    @GET
    @Path("/test2")
    public String test2() {
        return "Test2";
    }
    
    @GET
    @Path("/test3")
    public String test3() {
        return "Test3";
    }
}
