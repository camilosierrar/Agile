package model;

import java.util.Date;
/**
 * The class representing a step
 */
public class Step {
    /**
     * The name
     */
    private String name;
    /**
     * The date
     */
    private Date date;
    /**
     * The type
     */
    private String type;
    /**
     * The couple
     */
    private long couple;
    /**
     * The id
     */
    private long id;

    public Step(long id, String name, Date date, String type, long couple) {
        super();

        this.id = id;
        this.name = name;
        this.date = date;
        this.type = type;
        this.couple = couple;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCouple() {
        return couple;
    }

    public void setCouple(long couple) {
        this.couple = couple;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() { return date; }

    public String getType() {
        return type;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }


}
