package ch.romix.ivk.resultarchiver.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Game implements Serializable {

  private static SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
  private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
  private static SimpleDateFormat timeParser = new SimpleDateFormat("HH:mm:ss");
  private static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");


  private String tag;
  private String zeit;
  private String runde;
  private String txtTeamA;
  private String txtTeamB;
  private String resultatA;
  private String resultatB;
  private String punkteA;
  private String punkteB;

  public boolean isPlayed() {
    return !resultatA.isEmpty() && !resultatB.isEmpty();
  }

  public String getTag() {
    return tag;
  }

  public String getZeit() {
    return zeit;
  }

  public String getRunde() {
    return runde;
  }

  public String getTxtTeamA() {
    return txtTeamA;
  }

  public String getTxtTeamB() {
    return txtTeamB;
  }

  public String getResultatA() {
    return resultatA;
  }

  public String getResultatB() {
    return resultatB;
  }

  public String getPunkteA() {
    return punkteA;
  }

  public String getPunkteB() {
    return punkteB;
  }

  public void setTag(String tag) throws ParseException {
    this.tag = dateFormatter.format(dateParser.parse(tag));
  }

  public void setZeit(String zeit) throws ParseException {
    this.zeit = timeFormatter.format(timeParser.parse(zeit));
  }

  public void setRunde(String runde) {
    this.runde = runde;
  }

  public void setTxtTeamA(String txtTeamA) {
    this.txtTeamA = txtTeamA;
  }

  public void setTxtTeamB(String txtTeamB) {
    this.txtTeamB = txtTeamB;
  }

  public void setResultatA(String resultatA) {
    this.resultatA = resultatA;
  }

  public void setResultatB(String resultatB) {
    this.resultatB = resultatB;
  }

  public void setPunkteA(String punkteA) {
    this.punkteA = punkteA;
  }

  public void setPunkteB(String punkteB) {
    this.punkteB = punkteB;
  }
}
