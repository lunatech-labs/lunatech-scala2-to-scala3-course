package org.lunatechlabs.dotty

opaque type DollarAmount = BigDecimal
object DollarAmount {
  def apply(amount: BigDecimal): DollarAmount = {
    require(amount >= 0)
    amount.setScale(2, BigDecimal.RoundingMode.HALF_DOWN)
  }
}

opaque type TaxRate = BigDecimal
object TaxRate {
  def apply(taxRate: BigDecimal): TaxRate = {
    require(0 <= taxRate && taxRate <= 1)
    taxRate
  }
}

opaque type Quantity = Int
object Quantity {
  def apply(quantity: Int): Quantity = {
    require(quantity >= 0)
    quantity
  }
}

def (amountA: DollarAmount) + (amountB: DollarAmount): DollarAmount = amountA + amountB
def (amont: DollarAmount) * (taxRate: TaxRate): DollarAmount = amont * taxRate
def (amount: DollarAmount) * (quantity: Quantity): DollarAmount = amount * quantity

case class LineItem(unitPrice: DollarAmount, taxRate: TaxRate, quantity: Quantity)
