/**
 * Класс для описания судов и получения информации из БД
 */

public class Court {
    private String idCourt;
    private String nameCourt;
    private String adressCourt;
    private String link;

    public Court(String idCourt, String nameCourt, String adressCourt, String link) {
        this.idCourt = idCourt;
        this.nameCourt = nameCourt;
        this.adressCourt = adressCourt;
        this.link = link;
    }

    public Court() {

    }

    public String getIdCourt() {
        return idCourt;
    }

    public Court(String nameCourt) {
        this.nameCourt = nameCourt;
    }

    public String getNameCourt() {
        return nameCourt;
    }

    public String getAdressCourt() {
        return adressCourt;
    }

    public String getLink() {
        return link;
    }

    public void setIdCourt(String idCourt) {
        this.idCourt = idCourt;
    }

    public void setNameCourt(String nameCourt) {
        this.nameCourt = nameCourt;
    }

    public void setAdressCourt(String adressCourt) {
        this.adressCourt = adressCourt;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
