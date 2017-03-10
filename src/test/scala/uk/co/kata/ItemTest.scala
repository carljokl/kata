package uk.co.kata

import org.scalatest.{FlatSpec, Matchers}

 /**
  * This is a test of the the Item class functionality
  */
class ItemTest extends FlatSpec with Matchers {

   def testItem = Item("A", 100, Some(Discount(3, 150)))

   "Item" should  "hold the values assigned to it" in {
     testItem.basePrice should be (100)
     val discount = testItem.discount.get
     discount.price should be (150)
     discount.threshold should be (3)
   }

   "Item" should "return base price for quantity of 1" in {
     testItem.price(1) should be (100)
   }

   "Item" should "return discount price when exact discount threshold is hit" in {
     testItem.price(3) should be (150)
   }

   "Item" should "return discount price plus base price when over threshold" in {
     testItem.price(8) should be ((2 * 150) + (2 * 100))
   }
 }
