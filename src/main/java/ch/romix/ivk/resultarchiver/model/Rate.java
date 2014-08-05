package ch.romix.ivk.resultarchiver.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Rate {

  private String teamId;
  private int own;
  private int others;

  public String getTeamId() {
    return teamId;
  }

  public int getOwn() {
    return own;
  }

  public int getOthers() {
    return others;
  }

  public String getBothScores() {
    return String.format("%s : %s", getOthers(), getOwn());
  }

  public int getRate() {
    return own - others;
  }
}
