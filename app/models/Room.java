package models;

import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 7/14/12
 * Time: 4:15 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "room")
public class Room extends Model {

    @Required
    @Unique
    @Column(name = "name")
    public String name;

    public Room(String name) {
        this.name = name;
    }

    public static Room fetchByName(String name) {
        Room room = Room.find("byName", name).first();
        return room;
    }

    public  List<User> getUsers() {
        TypedQuery<User> query = em().createQuery("select u from User u, Map m where m.room=:room and m.user=u",
                User.class).setParameter("room", this);
        return query.getResultList();
    }

}
