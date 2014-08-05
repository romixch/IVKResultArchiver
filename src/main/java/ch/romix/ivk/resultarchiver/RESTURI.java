package ch.romix.ivk.resultarchiver;

public class RESTURI {

  private static final String BASE = "http://korbball.turnverband.ch/ws/";
  private static final String GROUPS = "groupsV1.php";
  private static final String TABLE = "tableV2.php?group=%s";

  public static String getGroupURI() {
    return appendToBase(GROUPS);
  }

  public static String getTableURI(int id) {
    return appendToBase(String.format(TABLE, id));
  }

  private static String appendToBase(String path) {
    String base = System.getProperty("BASE_PATH", BASE);
    return base + path;
  }
}
