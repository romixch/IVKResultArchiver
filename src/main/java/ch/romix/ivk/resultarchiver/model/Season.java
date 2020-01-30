package ch.romix.ivk.resultarchiver.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Season implements Serializable {

  private String season;

  public Season(String season) {
    this.season = season;
  }

  public String getSeason() {
    return season;
  }

  @Override
  public String toString() {
    return season;
  }
}
