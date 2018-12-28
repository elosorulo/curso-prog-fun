interp.configureCompiler(_.settings.YpartialUnification.value = true)
import $ivy.`org.typelevel::cats-core:1.0.1`, cats.implicits._, cats.data.Validated._, cats.data.NonEmptyList._
import cats.data.Validated._
import cats.data._
import cats.data.NonEmptyList

val input: String = """Jorge, 22, Argentina, jorgeperez@gmail.com
Pepe, 25, Uruguay, pepeargento@gmail.com
Mariana, 19, Argentina, marianarodriguez@gmail.com
Maria, 24, Argentina, maria93@gmail.com"""

sealed trait CsvParserError

val emailRegex: String = """(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])"""
val textRegex: String = """^[a-zA-Z ]*$"""

case class InvalidInputError(message: String)

case class User(name: String, age: Int, country: String, email: String)

case class CsvParamError(rowIndex: Int, message: String)

case class InvalidParametersError(validations: NonEmptyList[CsvParamError]) extends CsvParserError

type Users = List[User]

trait CsvParser {
    def parse(input: String): Either[CsvParserError, List[User]]
}

object Validations {

    def validateRowFormat(index: Int)(row: Array[String]): ValidatedNel[CsvParamError, Unit] = {
        if(row.length == 4) validNel[CsvParamError, Unit]()
        else invalidNel[CsvParamError, Unit](CsvParamError(index, "Invalid row format."))
    }

    def validateAge(index: Int)(age: String): ValidatedNel[CsvParamError, Unit] = {
        if(age.length < 0) invalidNel[CsvParamError, Unit](CsvParamError(index, "Age must be larger than 0."))
        else validNel[CsvParamError, Unit]()
    }

    def validateName(index: Int)(name: String): ValidatedNel[CsvParamError, Unit] = {
        if(name.length > 30)invalidNel[CsvParamError, Unit](CsvParamError(index, "Name must be shorter than 30."))
        else validNel[CsvParamError, Unit]()
    } 

    def validateCountry(index: Int)(country: String): ValidatedNel[CsvParamError, Unit] = {
        if(emailRegex.r.pattern.matcher(country).matches) validNel(())
        else invalidNel[CsvParamError, Unit](CsvParamError(index, "Invalid email format."))
    }

    def validateEmail(index: Int)(email: String): ValidatedNel[CsvParamError, Unit] = {
        if(emailRegex.r.pattern.matcher(email).matches) validNel(())
        else invalidNel[CsvParamError, Unit](CsvParamError(index, "Invalid email format."))
    }
}

object CsvParserImpl extends CsvParser {
    def parse(input: String): Either[CsvParserError, List[User]] = {
        val rows: Array[String] = input.split("\\n")
        validate(rows).fold({ invalid =>
            Left(InvalidParametersError(invalid))
        },{ valid =>
            Right(valid)
        })
    }

    private def validate(rows: Array[String]): ValidatedNel[CsvParamError, Users] = {
        val rowValidations: Array[ValidatedNel[CsvParamError, Users]] = rows.zipWithIndex.map { v =>
            val row = v._1
            val index = v._2
            val splittedRow: Array[String] = row.split(",")
            val rowFormatValidation: ValidatedNel[CsvParamError, Unit] = Validations.validateRowFormat(index)(splittedRow)
            rowFormatValidation match {
                case Invalid(_) => rowFormatValidation.map(_ => List.empty[User])
                case Valid(_) =>
                    val name = splittedRow(0)
                    val age = splittedRow(1)
                    val country = splittedRow(2)
                    val email = splittedRow(3)
                    (Validations.validateName(index)(name) |+| 
                    Validations.validateAge(index)(age) |+|
                    Validations.validateCountry(index)(country) |+|
                    Validations.validateEmail(index)(email)) map (_ => List(User(name, age.toInt, country, email)))
            }
        }
        rowValidations.foldLeft(validNel[CsvParamError, Users](List.empty[User]))(_ |+| _)
    }
}
