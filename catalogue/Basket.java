package catalogue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Stack;


/**
 * A collection of products,
 * used to record the products that are to be wished to be purchased.
 * @author  Mike Smith University of Brighton
 * @version 2.2
 *
 */
public class Basket extends ArrayList<Product> implements Serializable
{
  private static final long serialVersionUID = 1;
  private int    theOrderNum = 0;          // Order number
  private Stack<Product> addHistory = new Stack<>(); // History of added products for undo functionality
  
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
  /**
   * Constructor for a basket which is
   *  used to represent a customer order/ wish list
   */
  public Basket()
  {
    theOrderNum  = 0;
  }
  
  /**
   * Set the customers unique order number
   * Valid order Numbers 1 .. N
   * @param anOrderNum A unique order number
   */
  public void setOrderNum( int anOrderNum )
  {
    theOrderNum = anOrderNum;
  }

  /**
   * Returns the customers unique order number
   * @return the customers order number
   */
  public int getOrderNum()
  {
    return theOrderNum;
  }
  
  /**
   * Add a product to the Basket.
   * Product is appended to the end of the existing products
   * in the basket.
   * @param pr A product to be added to the basket
   * @return true if successfully adds the product
   */
  // Will be in the Java doc for Basket
  @Override
  public boolean add( Product pr )
  {                              
      addHistory.push(pr); //Record the product in the add history
      return super.add( pr );     // Call add in ArrayList
    

  }
  
  /**
   * Undo the last added product to the Basket.
   * Removes the most recently added product if there is any.
   * @return true if undo was successful, false if there is nothing to undo.
   */
  public boolean undoAdd() {
    if (!addHistory.isEmpty()) {
      Product lastAdded = addHistory.pop(); // Get the last added product
      return super.remove(lastAdded); // Remove it from the basket
    }
    return false; // No product to undo
  }


  /**
   * Returns a description of the products in the basket suitable for printing.
   * @return a string description of the basket products
   */
  public String getDetails()
  {
    Locale uk = Locale.UK;
    StringBuilder sb = new StringBuilder(256);
    Formatter     fr = new Formatter(sb, uk);
    String csign = (Currency.getInstance( uk )).getSymbol();
    double total = 0.00;
    if ( theOrderNum != 0 )
      fr.format( "Order number: %03d\n", theOrderNum );
      mergeDuplicateProducts();
      sortBasketByProductNumber();
      
      
      
    if ( this.size() > 0 )
    { 
      for ( Product pr: this )
      { 
        
        int number = pr.getQuantity();
        fr.format("%-7s",       pr.getProductNum() );
        fr.format("%-14.14s ",  pr.getDescription() );
        fr.format("(%3d) ",     number );
        fr.format("%s%7.2f",    csign, pr.getPrice() * number );
        fr.format("\n");
        total += pr.getPrice() * number;
      }
      fr.format("----------------------------\n");
      fr.format("Total                       ");
      fr.format("%s%7.2f\n",    csign, total );
      fr.close();
    }
    return sb.toString();
  }
  
  
  
  
  
}
