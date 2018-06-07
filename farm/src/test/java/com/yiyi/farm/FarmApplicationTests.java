package com.yiyi.farm;

import com.yiyi.farm.excpetion.ExpCodeEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import com.yiyi.farm.rsp.Result;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FarmApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
public class FarmApplicationTests {

	@Autowired
	private TestRestTemplate template;

	@Test
	public void get() {
		Map<String,String> multiValueMap = new HashMap<>();
		multiValueMap.put("username","lake");//传值，但要在url上配置相应的参数
		Result result = template.getForObject("/customer/allInfo",Result.class);
		System.out.println(result);
		Assert.assertEquals(result.isSuccess(),false);
		Assert.assertEquals(result.getErrorCode(), ExpCodeEnum.UNLOGIN.getCode());
	}

}
