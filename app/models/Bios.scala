package models

import javax.inject.Inject

import reactivemongo.bson.{BSONDateTime, BSONDocument, BSONInteger, BSONObjectID, BSONReader, BSONValue, BSONWriter, document}
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.{DefaultDB, ReadPreference}
import reactivemongo.core.commands._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}
import com.github.nscala_time.time.Imports._
import play.modules.reactivemongo.json._
import BiosJsonFormats._
import utils.DateConverter._
import BSONProducers._
/**
  * Created by Kabir Idris on 04/12/2017.
  */
object BSONProducers {

  implicit object DateWriter extends BSONWriter[DateTime,BSONDateTime]{
    def write(dt:DateTime) : BSONDateTime = BSONDateTime(dt.getMillis)
  }
  implicit object DateReader extends BSONReader[BSONDateTime,DateTime]{
    def read(dt:BSONDateTime) : DateTime = DateTime.parse(dt.value.toString)
  }
}

final case class FullName(first:String, last: String)

final case class Award(award: String, year :Int, by: String)

final case class Bios(_id: Option[String],
                      name :FullName,
                      birth: Option[BSONDateTime],
                      death: Option[BSONDateTime],
                      contribs: List[String],
                      awards : Option[Seq[Award]])




object BiosJsonFormats{

  implicit val awardJson = Json.format[Award]
  implicit val fullNameJson = Json.format[FullName]
  implicit val biosJson = Json.format[Bios]
}


class BiosRepository @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi){


  def biosCollection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("bios"))


  /**
    * get all bios from collection
    * @return
    */
  def getAll: Future[Seq[Bios]] = {

    val query = Json.obj()
    biosCollection.flatMap(_.find(query)
      .cursor[Bios](ReadPreference.primary)
      .collect[Seq]())
  }

  /**
    * unwind awards array of object and query it.
    * @param query
    * @return all bios with some specific fields if the query bsondocument is empty else
    *         return the bios documents based on the query
    */
  def getAwardsByYear(query: BSONDocument): Future[Stream[BSONDocument]] = {

    val agg = Aggregate("bios",
      Seq(Unwind("awards"),
        Match(query),
        Group(document("_id"->"id"))
        ("legend" -> PushMulti(
          "name" -> "name" ,
          "award" -> "awards.award",
          "year" -> "awards.year" )
        )
        ))

     biosCollection.flatMap(_.db.command(agg))
 }
}