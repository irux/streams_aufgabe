package models;

public class AdCampaign {

    private String camp_id;
    private String cookie;
    private int is_fake;
    private String time;

    public int getIs_fake() {
        return is_fake;
    }

    public String getCamp_id() {
        return camp_id;
    }

    public String getCookie() {
        return cookie;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "AdCampaign{" +
                "camp_id='" + camp_id + '\'' +
                ", cookie='" + cookie + '\'' +
                ", is_fake=" + is_fake +
                ", time='" + time + '\'' +
                '}';
    }
}
