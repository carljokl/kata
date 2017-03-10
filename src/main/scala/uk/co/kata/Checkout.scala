package uk.co.kata

/**
  * The main application class for the checkout system.
  */
object Checkout {

  def testItemA = Item("A", 50, Some(Discount(3, 130)))
  def testItemB = Item("B", 30, Some(Discount(2, 45)))
  def testItemC = Item("C", 20, None)
  def testItemD = Item("D", 15, None)

  def testInventory: Inventory = Inventory(Seq(testItemA, testItemB, testItemC, testItemD))

  /**
    * A total value of all the order items.
    *
    * @param lines The order items to be totalled.
    * @return A total of all the order items.
    */
  def total(lines: Seq[OrderLine]): Int = lines map (_.price) sum

  /**
    * Process the total of the raw scanned text values against the specified inventory of items.
    *
    * @param rawLines The raw scanned input lines.
    * @return The total value of the successfully scanned items.
    */
  def processTotal(rawLines: Seq[String], inventory: Inventory): Int = total(inventory.processLines(rawLines))

  def main(args: Array[String]): Unit = {
    println("Enter SKU codes or q to when finished...")
    var inventory = if (args.length == 1) Inventory.readFromFile(args head).getOrElse(testInventory) else testInventory
    val rawLines: Seq[String] = Iterator.continually(Console.in.readLine()).takeWhile((line) => line != null && !line.toLowerCase.startsWith("q")).toSeq
    val totalCost = processTotal(rawLines, testInventory)
    printf("The total for these items is: %d\n", totalCost)
  }
}
