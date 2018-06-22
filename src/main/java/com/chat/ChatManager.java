package com.chat;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager {

    private static ChatManager ourInstance = new ChatManager();
    public static ChatManager getInstance() {
        return ourInstance;
    }

    private Map<String, String> users;
    private HazelcastInstance hzInstance;

    private ChatManager() {
        this.users = new ConcurrentHashMap<>();
        Config config = new ClasspathXmlConfig("hazelcast.xml");
        System.out.println("CREATE HAZELCAST INSTANCE");
        this.hzInstance = Hazelcast.newHazelcastInstance(config);
    }

    public ITopic<String> subscribe(User user){
        ITopic<String> topic = hzInstance.getTopic(user.getChat());
        String handler_id = topic.addMessageListener(user);
        users.put(user.getName(), handler_id);
        return topic;
    }

    public void unSubscribe(User user){
        String handler_id = users.get(user.getName());
        user.getTopic().removeMessageListener(handler_id);
        users.remove(user.getName());
    }

    public boolean userExist(String user_name){
        return users.values().stream().anyMatch((chat) ->
                chat.contains(user_name)
        );
    }

}
