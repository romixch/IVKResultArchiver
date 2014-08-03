package ch.romix.ivk.resultarchiver;

public class RESTURI {

  private static final String BASE = "http://korbball.turnverband.ch/ws/";
  private static final String GROUPS = "groupsV1.php";

  public static String getGroupURI() {
    return BASE + GROUPS;
  }
}
