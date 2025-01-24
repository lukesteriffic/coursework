package catalogue;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.Formatter;

/**
 * Write a description of class BetterBasket here.
 * 
 * @author  Your Name 
 * @version 1.0
 */
public class BetterBasket extends Basket implements Serializable
{
  private static final long serialVersionUID = 1L;
  

  /**
   * Sorts the items in the basket in ascending order
   * based on their product numbers.
   */
  public void sortBasketByProductNumber()
  {
    Collections.sort(this, Comparator.comparing(Product::getProductNum));
  }
  
  public void mergeDuplicateProducts()
  {
    HashMap<String, Product> mergedProducts = new HashMap<>();

    for (Product product : this)
    {
      String productNum = product.getProductNum();
      if (mergedProducts.containsKey(productNum))
      {
        // If the product already exists, add its quantity to the existing product
        Product existingProduct = mergedProducts.get(productNum);
        existingProduct.setQuantity(existingProduct.getQuantity() + product.getQuantity());
      }
      else
      {
        // Otherwise, add the product to the map
        mergedProducts.put(productNum, product);
      }
    }

    // Clear the basket and re-add merged products
    this.clear();
    this.addAll(mergedProducts.values());
  }
}
