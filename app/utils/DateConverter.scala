package utils

import org.joda.time.DateTime
import play.api.libs.json.{Format, JsResult, JsValue, Json}
import play.api.libs.json.JodaWrites
import play.api.libs.json.JodaReads
import reactivemongo.bson.BSONDateTime
import com.github.nscala_time.time.Imports._


object DateConverter {

  implicit val dateTimeWriter = JodaWrites.jodaDateWrites("yyyy-MM-dd HH:mm:ss.SSS")
  implicit val dateTimeJsReader = JodaReads.jodaDateReads("yyyy-MM-dd HH:mm:ss.SSS")

}
