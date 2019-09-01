package com.y3tu.yao.gateway.handler;

import com.google.code.kaptcha.Producer;
import com.y3tu.yao.common.constants.SecurityConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码生成逻辑
 *
 * @author y3tu
 */
@Slf4j
@Component
@AllArgsConstructor
public class ImageCodeHandler implements HandlerFunction<ServerResponse> {

    @Autowired
    private final Producer producer;
    @Autowired
    private final RedisTemplate redisTemplate;

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        //生成验证码
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);
        //保存验证码信息
        String randomStr = serverRequest.queryParam("randomStr").get();
        //验证码保存到redis并设置失效时间30秒
        redisTemplate.opsForValue().set(SecurityConstants.DEFAULT_CODE_KEY+ randomStr, text, 30, TimeUnit.SECONDS);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpeg", os);
        } catch (IOException e) {
            log.error("ImageIO write err", e);
            return Mono.error(e);
        }

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(BodyInserters.fromResource(new ByteArrayResource(os.toByteArray())));
    }
}
