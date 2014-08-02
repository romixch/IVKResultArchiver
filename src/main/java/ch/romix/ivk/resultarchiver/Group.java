package ch.romix.ivk.resultarchiver;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Group implements Serializable {

  private int ID;
  private String name;

  public int getID() {
    return ID;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return ID + ": " + name;
  }

}
