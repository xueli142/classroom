package org.example.demo5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AllControllerSmokeTest {

    @Autowired
    private MockMvc mvc;

    @Qualifier("controllerEndpointHandlerMapping")
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Test
    public void smokeTest() throws Exception {
        // 1. 扫描所有 Controller 的映射
        Map<RequestMappingInfo, HandlerMethod> map = handlerMapping.getHandlerMethods();
        for (RequestMappingInfo info : map.keySet()) {
            // 2. 只取 GET 且不带 PathVariable 的接口（可自己扩展）
            if (info.getMethodsCondition().getMethods().contains(RequestMethod.GET)
                    && info.getPathPatternsCondition().getPatterns().size() == 1) {
                String path = info.getPathPatternsCondition().getPatterns().iterator().next().getPatternString();
                mvc.perform(get(path))
                        .andExpect(status().isOk());
            }
        }
    }
}