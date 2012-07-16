package controllers;

import models.Map;
import models.Message;
import models.Room;
import models.User;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.Date;
import java.util.List;

public class Application extends Controller {

    @Before(only = {"join", "leave", "send"})
    static void checkAuthentification() {
        if (session.get("user") == null) {
            String s = "you must connect to perform this action, type help for help";
            renderTemplate("info.html",s);
        }

    }

    public static void index() {
        render();


    }

    //help
    public static void help() {
        render();


    }

    //  connect [username]
    public static void connect(String username) {
        //if not in database-create

        User user = User.find("byName", username).first();

        if (user == null) {
            user = new User(username, new Date());
            user.save();
            Logger.info("new user named |" + user.name + "|is created");
        }
        session.put("user", username);
        String s = "Connected as " + username;
        renderTemplate("info.html", s);

    }

    //disconnect
    public static void disconnect() {
        String s;
        if(session.get("user")==null){
         s="It's imposible to disconnect disconnected user";
            renderTemplate("info.html", s);
        }
        session.put("user", null);
        s = "disconnected";
        renderTemplate("info.html", s);
        //("disconnected");


    }

    //list - отобразить список комнат
    public static void list() {
        List<Room> rooms = Room.findAll();
        render(rooms);
    }

    //join [room] - войти в комнату и увидеть всех пользователей, создать если не существует
    public static void join(String room) {
        //many to many- xz kak
        Room desiredRoom = Room.fetchByName(room);
        if (desiredRoom == null) {
            desiredRoom = new Room(room);
            desiredRoom.save();
        }
        User user = User.fetchByName(session.get("user"));
        Map map = new Map(user, desiredRoom);
        map.saveUnique();
        String s = "you have joint room " + desiredRoom.name;
        List<User> users = desiredRoom.getUsers();
        render(desiredRoom, users);


    }

    //leave [room]
    public static void leave(String room) {
        //xz

        String s;
        Room desiredRoom=Room.fetchByName(room);
        if(desiredRoom==null){
            s="You can't leave a room which doesn't exist.";
            renderTemplate("info.html", s);
        }

        Map map = Map.get(User.fetchByName(session.get("user")), desiredRoom);
        if (map == null) {
            s="You cant leave a room, which you didn't join";
            renderTemplate("info.html", s);
        }
        map.delete();
        s = "You left room " + room;

        renderTemplate("info.html", s);


    }

    //send [message] [room]
    public static void send(String message, String room) {

        Room desiredRoom = Room.fetchByName(room);
        if (desiredRoom == null) {
                         String s="room doesn't exist";
            renderTemplate("info.html",s);
        }
        Message mes = new Message(User.fetchByName(session.get("user")), desiredRoom, message);
        mes.save();
        String s = "you have sent the message";
        renderTemplate("info.html",s);

    }

    public static void fetchMessages() {
        String s = session.get("user");
        if(s==null){
            return;
        }
        User user = User.fetchByName(session.get("user"));
        if(user==null){
            s="something is wrong with db";
            renderTemplate("info.html", s);
        }
        List<Message.ShownMessage> newMessages = Message.fetchNew(user);
        user.lastRequest=new Date();
        user.save();
        render(newMessages);
    }


}