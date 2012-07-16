package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "message")
public class Message extends Model {

    @ManyToOne
    public User user;

    @ManyToOne
    public Room room;
    @Column(name = "text")

    public String text;
    @Temporal(TemporalType.TIMESTAMP)
    public Date date;

    public Message(User user, Room room, String text) {
        this.user = user;
        this.room = room;
        this.text = text;
        this.date = new Date();
    }
    //                "SELECT mes.text, r.name, u.name " +
//                "FROM Map m, Message mes, Room r, User u  " +
//                "WHERE m.userId=:userId AND m.roomId = mes.room AND r.id=mes.room AND mes.user=u.id " +
    public static List<ShownMessage> fetchNew(User user) {
        Query q = em().createQuery(
                "SELECT mes.text, r.name, u.name " +
                "FROM Map m, Message mes, Room r, User u  " +
                     "WHERE m.user= :requester AND m.room=mes.room AND r=mes.room AND mes.user=u "+
                "AND mes.date BETWEEN :lastDate and :now"
        ).setParameter("requester",user)
                .setParameter("lastDate", user.lastRequest).setParameter("now", new Date());
        List<ShownMessage> res = new LinkedList<ShownMessage>();
        for (Object o : q.getResultList()) {
            Object[] array = (Object[]) o;
            res.add(new ShownMessage((String) array[2], (String) array[1], (String) array[0]));
        }
        return res;
    }

    public static class ShownMessage {
        public String user;
        public String room;
        public String text;

        public ShownMessage(String user, String room, String text) {
            this.user = user;
            this.room = room;
            this.text = text;
        }
    }
}
