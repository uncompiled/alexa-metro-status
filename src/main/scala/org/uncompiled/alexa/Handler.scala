package org.uncompiled.alexa

import java.text.SimpleDateFormat
import java.util.{Calendar,UUID}
import scala.xml.XML

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

class Handler extends RequestHandler[Request, Response] {
  type AffectedLines = Set[String]

  def handleRequest(input: Request, context: Context): Response = {
    val xml = XML.load("http://www.metroalerts.info/rss.aspx?rs")
    val incidents = (xml \\ "item")
    val affectedLines : AffectedLines = incidents.map(i => (i \\ "title").text).toSet

    def affectedLinesText() : String = {
      val ALL_LINES = 6 // Metro currently has 6 rail lines
      affectedLines.size match {
        case ALL_LINES => "Oh my. All lines are currently impacted by delays."
        case 0 => "Surprise! There are no incidents on the Metro rail system."
        case 1 => s"There are incidents on the ${affectedLines.mkString} line."
        case _ =>  s"There are incidents on the ${speak(affectedLines)} lines."
      }
    }

    /* personalText creates a personalized briefing */
    def personalText() : String = {
      // TODO: Find a way to generalize this hack by linking to a WMATA account or commute itinerary.
      val myLines = "RED"
      val myIncidents = incidents.filter(i => (i \\ "title").text == myLines).map(i => (i \\ "description").text)
      val opener = if (myIncidents.size >= 3) { "You're fucked. " } else { "" }
      val closer = if (incidents.size >= 6) { " May the odds ever be in your favour." } else { "" }
      transformWMATAShorthand(myIncidents.mkString(opener, " ", closer))
    }
    
    // Define Alexa's speech text!
    val speechText = s"$affectedLinesText $personalText"

    // https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/flash-briefing-skill-api-feed-reference
    val uid = UUID.randomUUID.toString
    val updateDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.0Z'").format(Calendar.getInstance().getTime())
    val titleText = "DC Metro Status"
    val mainText = speechText
    val redirectionUrl = "https://goo.gl/Dvsv3R"

    new Response(uid, updateDate, titleText, mainText, redirectionUrl)
  }

  /* speak takes a set of affected lines and outputs speech text as a string
   * @param affectedLines set of rail lines with incidents
   */
  def speak(affectedLines: AffectedLines): String =
    s"${affectedLines.init.mkString(", ")} and ${affectedLines.last}"

  /* transformWMATAShorthand replaces WMATA shorthand text with words that sound more natural for speech
   * @param input speech text string
   */
  def transformWMATAShorthand(input: String): String = {
    // TODO: This is probably fucked up.
    // https://imgs.xkcd.com/comics/perl_problems.png
    input
      .replaceAll("w/add'l", "with additional")
      .replaceAll("btwn", "between")
      .replaceAll("Pl", "Place")
      // WMATA uses 9A instead of 9AM and this sounds weird
      .replaceAll("""(\d)(A|P)""", "$1 $2M ")
      // Finally, remove slashes because Alexa will read them
      .replaceAll("/", " ")
  }
}
