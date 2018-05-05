package com.yiyi.farm;

import com.yiyi.farm.rsp.Result;
import org.springframework.web.client.RestTemplate;


public class FarmClient {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        Result result = restTemplate.getForObject("http://localhost:8080/invite/redEnvelope?phone=13478569427&startTime=1518107481&endTime=1522115764&totalConsume=100&chargeConsume=50", Result.class);
        System.out.println(result);
    }
}
