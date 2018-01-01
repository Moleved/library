package entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="books")
public class BooksEntity extends ActiveModel {
    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="state")
    private int state = 1;

    @Column(name="release_date")
    private Date releaseDate;

    public String getId() {
        return Integer.toString(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return Integer.toString(state);
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getReleaseDate() {
        return releaseDate.toString();
    }

    public void setReleaseDate(Date release_date) {
        this.releaseDate = release_date;
    }
}
