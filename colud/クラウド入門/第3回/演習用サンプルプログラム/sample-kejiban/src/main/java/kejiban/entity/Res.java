package kejiban.entity;

import java.io.Serializable;
import java.sql.Timestamp;

//import javax.persistence.Column;
public class Res implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String entry;
    private Timestamp date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
