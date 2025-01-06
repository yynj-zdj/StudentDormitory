package cn.lanqiao.studentdormitory.config;



import cn.lanqiao.studentdormitory.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {
    /**
     * 扩展SpringMVC 框架的消息转换器
     * @param converters
     */
    @Override
     public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//       创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//       需要为消息转换器设置一个对象转换器，对象转换器可以将java对象序列号为JSON数据
        converter.setObjectMapper(new JacksonObjectMapper());
//        将自己的消息转换器加入容器
        converters.add(0,converter);
    }
}
