package models;

import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 7/14/12
 * Time: 4:15 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "chat_user")
public class User extends Model {
    @Required
    @Unique
    @Column(name = "name")
    public String name;
    @Column(name = "last_request")
    @Temporal(TemporalType.TIMESTAMP)
    public Date lastRequest;

    public User(String name, Date lastRequest) {
        this.name = name;
        this.lastRequest = lastRequest;
    }



    public void send(String room, Message message) {

    }



    public static User fetchByName(String name) {
        User user = User.find("byName", name).first();
        return user;
    }

}
