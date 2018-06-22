package com.chat;

import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;


@ServerEndpoint("/chat")
public class User implements MessageListener<String>{

    private static final String DUPLICATE_MSG = "{\"type\":\"system\",\"message\":\"Ya existe un usuario con ese nombre\"}";

    private Session session;
    private String name;
    private String chat;
    private ChatManager manager;
    private ITopic<String> topic;

    @OnOpen
    public void open(Session session){
        this.session = session;
        this.manager = ChatManager.getInstance();
    }

    @OnMessage
    public void handleMessage(Session session, String message){

        if(this.name != null){
            // Broadcast message
            this.topic.publish(message);
        }else{
            // Init message
            newUser(message);
        }

    }

    @Override
    public void onMessage(Message<String> message) {
        this.send(message.getMessageObject());
    }

    @OnClose
    public void close(Session session){

        if(this.name != null){
            this.manager.unSubscribe(this);
        }

    }

    @OnError
    public void onError(Session session, Throwable thr) {
        System.err.println("Client "+session.getId()+" error: "+thr.getMessage());
    }

    public void send(String message){
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.err.println("Can't send message to user: "+this.name);
        }
    }

    private void newUser(String message){

        JSONObject jsonMessage = new JSONObject(message);

        this.chat = jsonMessage.getString("chat");
        this.name = jsonMessage.getString("name");

        if( manager.userExist(name) ){
            send(DUPLICATE_MSG);
        }else{
            this.topic = this.manager.subscribe(this );
        }
    }

    public String getName(){
        return this.name;
    }

    public String getChat(){
        return this.chat;
    }

    public ITopic<String> getTopic(){
        return this.topic;
    }

}
