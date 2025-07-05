package TruongND48_JPL.L.A203.src.main.java.fa.training.entities;


import java.sql.Date;

public class Order  {
    private String number;
    private Date date;

    public Order() {
    }

    public Order(String number, Date date) {
        this.number = number;
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "number='" + number + '\'' +
                ", date=" + date +
                '}';
    }

}
