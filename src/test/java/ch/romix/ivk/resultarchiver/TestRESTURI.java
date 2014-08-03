package ch.romix.ivk.resultarchiver;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestRESTURI {

  @Test
  public void assertTableURIContainsId() {
    String uri = RESTURI.getTableURI(42);
    assertNotNull(uri);
    assertTrue(uri.endsWith("42"));
  }

}
