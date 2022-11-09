package chemintippool;

public class Employee {
    private String id;
    private String first;
    private String last;
    private String position;
    private double owed;
    private double earnedLastShift;
    private double earnedTotal;

    public Employee() {
        id = "-1";
        first = "john";
        last = "doe";
        position = "civilian";
        owed = 0;
        earnedLastShift = 0;
        earnedTotal = 0;
    }

    public Employee(String id, String first, String last, String position, double owed, double earnedLastShift, double earnedTotal) {
        this.id = id;
        this.first = first;
        this.last = last;
        this.position = position;
        this.owed = owed;
        this.earnedLastShift = earnedLastShift;
        this.earnedTotal = earnedTotal;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return first + " " + last;
    }

    public String getPos() {
        return position;
    }

    public double getOwed() {
        return owed;
    }

    public double getEarnedLS() {
        return earnedLastShift;
    }

    public double getEarnedTotal() {
        return earnedTotal;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void setPos(String pos) {
        this.position = pos;
    }
}
