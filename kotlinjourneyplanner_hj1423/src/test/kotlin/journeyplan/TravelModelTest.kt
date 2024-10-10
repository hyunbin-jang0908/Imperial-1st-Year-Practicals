package journeyplan

import org.junit.Test
import kotlin.test.assertEquals

class TravelModelTest {
  val map = londonUnderground()

  @Test
  fun `printing stations shows their names`() {
    assertEquals("South Kensington", Station("South Kensington").toString())
    assertEquals("Knightsbridge", Station("Knightsbridge").toString())
  }

  @Test
  fun `printing lines shows their names`() {
    assertEquals("District Line", Line("District").toString())
    assertEquals("Circle Line", Line("Circle").toString())
  }

  @Test
  fun `finding routes`() {
    assertEquals(
      "[South Kensington to Westminster - 10 minutes, 0 changes\n" +
        "- South Kensington to Sloane Square by District Line\n" +
        "- Sloane Square to Victoria by District Line\n" +
        "- Victoria to St. James's Park by District Line\n" +
        "- St. James's Park to Westminster by District Line\n" +
        ", South Kensington to Westminster - 18 minutes, 2 changes\n" +
        "- South Kensington to Knightsbridge by Piccadilly Line\n" +
        "- Knightsbridge to Hyde Park Corner by Piccadilly Line\n" +
        "- Hyde Park Corner to Green Park by Piccadilly Line\n" +
        "- Green Park to Victoria by Victoria Line\n" +
        "- Victoria to St. James's Park by District Line\n" +
        "- St. James's Park to Westminster by District Line\n" +
        "]",
      map.routesFrom(southKensington, westminster, ::totalChanges).toString()
    )
  }
}
