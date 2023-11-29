package Data;

import java.util.ArrayList;

public class Studio {

    private String tradeName;

    private String logoLink;

    private String dateFounded;

    private String founders;

    private String headquarters;

    private ArrayList<String> generalInfo;

    public Studio(){
        super();
    }


    public Studio(String tradeName, String logoLink, String dateFounded, String founders,
                  String headquarters, ArrayList<String> generalInfo){

        this.tradeName = tradeName;
        this.logoLink = logoLink;
        this.dateFounded = dateFounded;
        this.founders = founders;
        this.headquarters = headquarters;
        this.generalInfo = generalInfo;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public void setLogoLink(String logoLink) {
        this.logoLink = logoLink;
    }

    public String getDateFounded() {
        return dateFounded;
    }

    public void setDateFounded(String dateFounded) {
        this.dateFounded = dateFounded;
    }

    public String getFounders() {
        return founders;
    }

    public void setFounders(String founders) {
        this.founders = founders;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public ArrayList<String> getGeneralInfo() {
        return generalInfo;
    }

    public void setGeneralInfo(ArrayList<String> generalInfo) {
        this.generalInfo = generalInfo;
    }

    @Override
    public String toString() {
        return String.format(
                """
                Studio: {
                    "tradeName": "%s"
                    "logoLink": "%s"
                    "dateFounded": "%s"
                    "founders": "%s"
                    "headquarters": "%s"
                    "generalInfo": "%s"
                }
                """, this.getTradeName(), this.getLogoLink(),
                this.getDateFounded(), this.getFounders(),
                this.getHeadquarters(),
                this.getGeneralInfo()
        );
    }
}
