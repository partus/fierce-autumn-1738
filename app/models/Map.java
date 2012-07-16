package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 7/16/12
 * Time: 2:12 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "map")
public class Map extends Model {

    @ManyToOne
    public User user;


    @ManyToOne
    public Room room;

    public static Map get(User user, Room room){
      return Map.find("byUserAndRoom",user,room).first();
    }

    public void saveUnique(){
        if(Map.find("byUserAndRoom",this.user,this.room).first()==null){
            this.save();
        }
    }

    public Map(User user, Room room) {
        this.user = user;
        this.room = room;
    }
    //public static List<Map>
}
