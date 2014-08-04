package ch.romix.ivk.resultarchiver.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamTwo {

  private int teamTwoId;
  private String result;

  public int getTeamTwoId() {
    return teamTwoId;
  }

  public String getResult() {
    return result.replace("&nbsp;", "");
  }
}
