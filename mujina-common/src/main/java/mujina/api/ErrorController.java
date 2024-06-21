package mujina.api;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/*
* 错误处理控制器, 实现了Spring框架的ErrorController接口，用于处理应用中发生的错误情况
*/
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    // ErrorAttributes - 错误信息的实例
    private final ErrorAttributes errorAttributes;

    // ErrorController构造函数
    public ErrorController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }

    // error方法 - 处理HTTP请求中的错误，返回一个包含错误信息的ResponseEntity对象
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest aRequest) {
        ServletWebRequest webRequest = new ServletWebRequest(aRequest);
        Map<String, Object> result = this.errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());

        HttpStatus statusCode = INTERNAL_SERVER_ERROR;
        Object status = result.get("status");
        if (status != null && status instanceof Integer) {
            statusCode = HttpStatus.valueOf(((Integer) status).intValue());
        }
        return new ResponseEntity<>(result, statusCode);

    }

}
