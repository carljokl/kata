package uk.co.kata

import org.scalatest.{FlatSpec, Matchers}

/**
 * This is a test of the complete Checkout system
 */
class CheckoutTest extends FlatSpec with Matchers {

  def testItemA = Item("A", 50, Some(Discount(3, 130)))
  def testItemB = Item("B", 30, Some(Discount(2, 45)))
  def testItemC = Item("C", 20, None)
  def testItemD = Item("D", 15, None)

  def testInventory = Inventory(Seq(testItemA, testItemB, testItemC, testItemD))

  "Checkout" should  "return the correct price for a single item" in {
    Checkout.processTotal(Seq("A"), testInventory) should be (testItemA.basePrice)
  }

  "Checkout" should  "return the correct price for a multiple items below the discount" in {
    Checkout.processTotal(Seq("A", "A"), testInventory) should be (testItemA.basePrice * 2)
  }

  "Checkout" should  "return the correct discounted price for a group of items" in {
    Checkout.processTotal(Seq("A", "A", "A"), testInventory) should be (testItemA.discount.get.price)
  }

  "Checkout" should  "return the correct discounted price for a group of items combined with a base price" in {
    Checkout.processTotal(Seq("A", "A", "A", "A", "A"), testInventory) should be (testItemA.discount.get.price + (testItemA.basePrice * 2))
  }

  "Checkout" should  "return the correct discounted price for a group of items combined with a base price when different items are mixed" in {
    Checkout.processTotal(Seq("A", "A", "B", "A", "B", "B", "A", "A"), testInventory) should be (testItemA.discount.get.price +
                                                                                                 (testItemA.basePrice * 2) +
                                                                                                 testItemB.discount.get.price +
                                                                                                 testItemB.basePrice)
  }
}
