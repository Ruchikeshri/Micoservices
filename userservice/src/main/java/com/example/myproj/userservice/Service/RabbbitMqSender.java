//
//package com.example.myproj.userservice.Service;
//
//import com.example.myproj.userservice.model.RegisterAndLogin;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RabbbitMqSender {
//    private RabbitTemplate rabbitTemplate;
//
//    @Autowired
//    public RabbbitMqSender(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    @Value("${spring.rabbitmq.exchange}")
//    private String exchange;
//    @Value("${spring.rabbitmq.routingkey}")
//    private String routingKey;
//
//    public void send(RegisterAndLogin user)
//    {
//        rabbitTemplate.convertAndSend(exchange,routingKey,user);
//    }
//}
