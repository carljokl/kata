package uk.co.kata

import scala.util.parsing.json._
import scala.io._

/**
  * A shopping item with a base price and optional collection of discounts.
  */
case class Item(sku: String, basePrice: Int,  discount: Option[Discount]) {

  def price(quantity: Int): Int = {
    discount match {
      case Some(Discount(threshold, discountPrice)) => if (quantity >= threshold)
                                                          ((quantity / threshold) * discountPrice) +
                                                            ((quantity % threshold) * basePrice)
                                                       else quantity * basePrice
      case _ => basePrice * quantity
    }
  }
}

/**
  * This object represents a discount price that kicks in at a given threshold of numbers of items.
  * @param threshold The number items before the discount applies.
  * @param price The prices of the group of items i.e. all the combined items at the threshold
  */
case class Discount(threshold: Int, price: Int)

class Inventory(val items: Map[String, Item]) {

  def lookUp (sku: String): Option[Item] = items get sku

  def processLines(skus: Seq[String]): Seq[OrderLine] = skus.distinct flatMap lookUp map (item => OrderLine(skus count (_ == item.sku), item))
}

object Inventory {

  def parseRuleString(rule: Map[String, Any], key: String, defaultValue: String): String = {
    rule.get(key) match {
      case Some(stringValue: String) => stringValue
      case value => Option(value).getOrElse(defaultValue).toString
    }
  }

  def parseRuleInt(rule: Map[String, Any], key: String, defaultValue: Int): Int = {
    rule.get(key) match {
      case Some(intValue: Int) => intValue
      case _ => defaultValue
    }
  }

  def parseRule(rule: Map[String, Any]): Item = {
    if (rule.contains("discount-threshold") && rule.contains("discount-price"))
      Item(parseRuleString(rule, "sku", ""), parseRuleInt(rule, "base-price", 0),
           Some(Discount(parseRuleInt(rule, "discount-threshold", 1), parseRuleInt(rule, "discount-price", 0))))
    else
      Item(parseRuleString(rule, "sku", ""), parseRuleInt(rule, "base-price", 0), None)
  }

  def readFromFile(file: String): Option[Inventory] = {
    val jsonFile = new java.io.File(file)
    if (jsonFile.exists) {
      JSON.parseFull(Source.fromFile(file).mkString) match {
        case Some(Seq(rules: Seq[Map[String, Any]])) => {
          val items: Seq[Item] = rules map parseRule
          Some(Inventory(items))
        }
        case _ => None
      }
    }
    else None
  }

  def apply(items: Seq[Item]): Inventory = {
    new Inventory(Map(items map { item => item.sku -> item }: _*))
  }
}
