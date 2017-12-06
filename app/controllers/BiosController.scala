package controllers

import javax.inject.Inject

import io.swagger.annotations.{Api, ApiOperation, ApiResponse, ApiResponses}
import models.{Bios, BiosRepository}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import models.BiosJsonFormats._

import scala.concurrent.ExecutionContext
import play.modules.reactivemongo.json._
import reactivemongo.bson._
/**
  * Created by Kabir Idris 04/12/2017
  */
@Api(value = "/bios")
class BiosController @Inject()(cc: ControllerComponents, biosRepo: BiosRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {


  @ApiOperation(
    value = "Find all Bios",
    response = classOf[Bios],
    responseContainer = "List"
  )
   def getAllBios() = Action.async{

     biosRepo.getAll.map( bios => Ok(Json.toJson(bios)))
   }



   def getAwardsByYears(year: Option[Int]) = Action.async{

      val query = year.map(y => document("awards.year"-> y)).getOrElse(document())
      biosRepo.getAwardsByYear(query).map( awards => Ok(Json.toJson(awards)))
   }
}
