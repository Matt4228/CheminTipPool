package chemintippool;

import java.util.ArrayList;
import java.util.List;

public class Shift {
    private String date;
    private double tips;
    private String type;
    private List<Employee> bartenders = new ArrayList();
    private List<Employee> runners = new ArrayList();
    private List<Employee> servers = new ArrayList();

    public Shift() {
        date = "00/00/0000";
        tips = 0;
        type = "none";
    }

    public Shift(String date, double tips, String type, List<Employee> bartenders, List<Employee> runners, List<Employee> servers) {
        this.date = date;
        this.tips = tips;
        this.type = type;
        this.bartenders = bartenders;
        this.runners = runners;
        this.servers = servers;
    }

    public String getDate() {
        return date;
    }

    public double getTips() {
        return tips;
    }

    public String getType() {
        return type;
    }

    public String getBartenders() {
        String res = "";
        for (int i = 0; i < bartenders.toArray().length; i++) {
            if (i < bartenders.toArray().length - 1) {
                res += bartenders.get(i).getName() + ",\n";
            } else {
                res += bartenders.get(i).getName();
            }
        }
        return res;
    }

    public String getRunners() {
        String res = "";
        for (int i = 0; i < runners.toArray().length; i++) {
            if (i < runners.toArray().length - 1) {
                res += runners.get(i).getName() + ",\n";
            } else {
                res += runners.get(i).getName();
            }
        }
        return res;
    }

    public String getServers() {
        String res = "";
        for (int i = 0; i < servers.toArray().length; i++) {
            if (i < servers.toArray().length - 1) {
                res += servers.get(i).getName() + ",\n";
            } else {
                res += servers.get(i).getName();
            }
        }
        return res;
    }
}