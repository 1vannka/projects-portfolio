package com.project5.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;


import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration
public class RabbitMQConfig {
    public static final String OWNER_EXCHANGE = "owner_exchange";
    public static final String CAT_EXCHANGE = "cat_exchange";

    public static final String OWNER_GET_ALL_KEY = "owner.get.all";
    public static final String OWNER_GET_BY_ID_KEY = "owner.get.by.id";
    public static final String OWNER_CREATE_KEY = "owner.create";
    public static final String OWNER_UPDATE_KEY = "owner.update";
    public static final String OWNER_DELETE_KEY = "owner.delete";
    public static final String OWNER_VALIDATE_KEY = "owner.validate";

    public static final String CAT_GET_ALL_KEY = "cat.get.all";
    public static final String CAT_GET_BY_ID_KEY = "cat.get.by.id";
    public static final String CAT_CREATE_KEY = "cat.create";
    public static final String CAT_UPDATE_KEY = "cat.update";
    public static final String CAT_DELETE_KEY = "cat.delete";
    public static final String CAT_ADD_FRIEND_KEY = "cat.add.friend";
    public static final String CAT_REMOVE_FRIEND_KEY = "cat.remove.friend";

    
    @Value("${owner.queue.all}")
    private String ownerGetAllQueue;

    @Value("${owner.queue.byId}")
    private String ownerGetByIdQueue;

    @Value("${owner.queue.create}")
    private String ownerCreateQueue;

    @Value("${owner.queue.update}")
    private String ownerUpdateQueue;

    @Value("${owner.queue.delete}")
    private String ownerDeleteQueue;

    @Value("${owner.queue.validate}")
    private String ownerValidateQueue;

    
    @Value("${cat.queue.all}")
    private String catGetAllQueue;

    @Value("${cat.queue.byId}")
    private String catGetByIdQueue;

    @Value("${cat.queue.create}")
    private String catCreateQueue;

    @Value("${cat.queue.update}")
    private String catUpdateQueue;

    @Value("${cat.queue.delete}")
    private String catDeleteQueue;

    @Value("${cat.queue.addFriend}")
    private String catAddFriendQueue;

    @Value("${cat.queue.removeFriend}")
    private String catRemoveFriendQueue;


    @Bean
    public DirectExchange ownerExchange() {
        return new DirectExchange(OWNER_EXCHANGE);
    }

    @Bean
    public DirectExchange petExchange() {
        return new DirectExchange(CAT_EXCHANGE);
    }

    @Bean
    public Queue ownerGetAllQueue() {
        return new Queue(ownerGetAllQueue);
    }
    @Bean
    public Queue ownerGetByIdQueue() {
        return new Queue(ownerGetByIdQueue);
    }
    @Bean
    public Queue ownerCreateQueue() {
        return new Queue(ownerCreateQueue);
    }
    @Bean
    public Queue ownerUpdateQueue() {
        return new Queue(ownerUpdateQueue);
    }
    @Bean
    public Queue ownerDeleteQueue() {
        return new Queue(ownerDeleteQueue);
    }
    @Bean
    public Queue ownerValidateQueue() {
        return new Queue(ownerValidateQueue);
    }

    @Bean
    public Queue catGetAllQueue() {
        return new Queue(catGetAllQueue);
    }
    @Bean
    public Queue catGetByIdQueue() {
        return new Queue(catGetByIdQueue);
    }
    @Bean
    public Queue catCreateQueue() {
        return new Queue(catCreateQueue);
    }
    @Bean
    public Queue catUpdateQueue() {
        return new Queue(catUpdateQueue);
    }
    @Bean
    public Queue catDeleteQueue() {
        return new Queue(catDeleteQueue);
    }
    @Bean
    public Queue catAddFriendQueue() {
        return new Queue(catAddFriendQueue);
    }
    @Bean
    public Queue catRemoveFriendQueue() {
        return new Queue(catRemoveFriendQueue);
    }

    @Bean
    public Binding bindOwnerGetAll(Queue ownerGetAllQueue, DirectExchange ownerExchange) {
        return BindingBuilder.bind(ownerGetAllQueue).to(ownerExchange).with(OWNER_GET_ALL_KEY);
    }
    @Bean
    public Binding bindOwnerGetById(Queue ownerGetByIdQueue, DirectExchange ownerExchange) {
        return BindingBuilder.bind(ownerGetByIdQueue).to(ownerExchange).with(OWNER_GET_BY_ID_KEY);
    }
    @Bean
    public Binding bindOwnerCreate(Queue ownerCreateQueue, DirectExchange ownerExchange) {
        return BindingBuilder.bind(ownerCreateQueue).to(ownerExchange).with(OWNER_CREATE_KEY);
    }
    @Bean
    public Binding bindOwnerUpdate(Queue ownerUpdateQueue, DirectExchange ownerExchange) {
        return BindingBuilder.bind(ownerUpdateQueue).to(ownerExchange).with(OWNER_UPDATE_KEY);
    }
    @Bean
    public Binding bindOwnerDelete(Queue ownerDeleteQueue, DirectExchange ownerExchange) {
        return BindingBuilder.bind(ownerDeleteQueue).to(ownerExchange).with(OWNER_DELETE_KEY);
    }
    @Bean
    public Binding bindOwnerValidate(Queue ownerValidateQueue, DirectExchange ownerExchange) {
        return BindingBuilder.bind(ownerValidateQueue).to(ownerExchange).with(OWNER_VALIDATE_KEY);
    }

    @Bean
    public Binding bindCatGetAll(Queue catGetAllQueue, DirectExchange petExchange) {
        return BindingBuilder.bind(catGetAllQueue).to(petExchange).with(CAT_GET_ALL_KEY);
    }
    @Bean
    public Binding bindCatGetById(Queue catGetByIdQueue, DirectExchange petExchange) {
        return BindingBuilder.bind(catGetByIdQueue).to(petExchange).with(CAT_GET_BY_ID_KEY);
    }
    @Bean
    public Binding bindCatCreate(Queue catCreateQueue, DirectExchange petExchange) {
        return BindingBuilder.bind(catCreateQueue).to(petExchange).with(CAT_CREATE_KEY);
    }
    @Bean
    public Binding bindCatUpdate(Queue catUpdateQueue, DirectExchange petExchange) {
        return BindingBuilder.bind(catUpdateQueue).to(petExchange).with(CAT_UPDATE_KEY);
    }
    @Bean
    public Binding bindCatDelete(Queue catDeleteQueue, DirectExchange petExchange) {
        return BindingBuilder.bind(catDeleteQueue).to(petExchange).with(CAT_DELETE_KEY);
    }
    @Bean
    public Binding bindCatAddFriend(Queue catAddFriendQueue, DirectExchange petExchange) {
        return BindingBuilder.bind(catAddFriendQueue).to(petExchange).with(CAT_ADD_FRIEND_KEY);
    }
    @Bean
    public Binding bindCatRemoveFriend(Queue catRemoveFriendQueue, DirectExchange petExchange) {
        return BindingBuilder.bind(catRemoveFriendQueue).to(petExchange).with(CAT_REMOVE_FRIEND_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}