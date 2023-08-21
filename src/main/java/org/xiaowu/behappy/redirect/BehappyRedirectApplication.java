package org.xiaowu.behappy.redirect;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

/**
 * @author xiaowu
 */
@Controller
@SpringBootApplication
public class BehappyRedirectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BehappyRedirectApplication.class, args);
    }

    @Value("${redirect-url:}")
    private String redirectUrl;

    @Value("${server.port:8080}")
    private String port;

    @RequestMapping("/**")
    public RedirectView redirect(@RequestParam(required = false) Map<String, Object> params) {
        if (!StringUtils.hasLength(redirectUrl)){
            redirectUrl = "http://127.0.0.1:%s/test-callback".formatted(port);
        }
        StringBuilder urlBuilder = new StringBuilder(redirectUrl);
        boolean appendQuestionMask = false;
        int totalParamSize = params.keySet().size();
        int i = 0;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (!appendQuestionMask) {
                appendQuestionMask = true;
                urlBuilder.append("?");
            }
            String key = entry.getKey();
            Object value = entry.getValue();
            ++i;
            if (i == totalParamSize){
                urlBuilder.append("%s=%s".formatted(key, ((String) value)));
            }else {
                urlBuilder.append("%s=%s&".formatted(key, ((String) value)));
            }
        }
        return new RedirectView(urlBuilder.toString(), true);
    }


    @RequestMapping("/test-callback")
    public ResponseEntity<?> test(@RequestParam(required = false) Map<String, Object> params) {
        StringBuilder urlBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            urlBuilder.append("%s = %s<br>".formatted(key, ((String) value)));
        }
        return ResponseEntity.ok(urlBuilder.toString());
    }

}
