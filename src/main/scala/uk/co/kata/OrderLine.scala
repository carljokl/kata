package uk.co.kata

/**
  * This object represents a discount price that kicks in at a given threshold of numbers of items.
  */
case class OrderLine(quantity: Int, item: Item) {

  /**
    * Get the price of the given quantity of the item.
    *
    * @return The price of the quantity of the item.
    */
  def price: Int = item.price(quantity)
}
