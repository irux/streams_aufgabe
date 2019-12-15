package models;

public class AdCampaignStatistics {




    private String camp_id;
    private int fakeClicks;
    private int validClicks;
    private int total;
    private double fraudPercent;
    private double validPercent;

    public AdCampaignStatistics(String ID){
        this.camp_id = ID;
    }


    public void addFakeClick(){
        this.fakeClicks++;
        this.total++;
        this.updateValues();
    }

    public void addValidClick(){
        this.validClicks++;
        this.total++;
        this.updateValues();
    }

    private void updateValues(){
        this.fraudPercent = this.fakeClicks/(double)total * 100;
        this.validPercent = this.validClicks/(double)total* 100;

    }

    public double getFraudPercent() {
        return fraudPercent;
    }

    public double getValidPercent() {
        return validPercent;
    }

    public int getFakeClicks() {
        return fakeClicks;
    }

    public int getTotal() {
        return total;
    }

    public int getValidClicks() {
        return validClicks;
    }

    public String getCampID() {
        return camp_id;
    }

    @Override
    public String toString() {
        return "AdCampaignStatistics{" +
                "campID='" + camp_id + '\'' +
                ", fakeClicks=" + fakeClicks +
                ", validClicks=" + validClicks +
                ", total=" + total +
                ", fraudPercent=" + fraudPercent +
                ", validPercent=" + validPercent +
                '}';
    }
}

