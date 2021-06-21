package com.example.DemoServlet01.services;

import com.example.DemoServlet01.models.Book;
import com.example.DemoServlet01.models.Order;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Database {
  private static Database cache;
  public static Database GetInstance(){
    if(cache == null){
      cache = new Database();
    }
    return cache;
  }
  private Database() {
    this.books = new ArrayList<>();
    this.cart = new ArrayList<>();
  }

  private ArrayList<Book> books;

  public ArrayList<Order> getCart() {
    return cart;
  }

  private ArrayList<Order> cart;

  public void loadData(String filename) throws FileNotFoundException {
    this.books = new ArrayList<>();
    try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
      for(String line; (line = br.readLine()) != null; ) {
        String[] tokens = line.split(";");
        this.books.add(new Book(tokens[0],tokens[1],Double.parseDouble(tokens[2])));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public Book getBookById(String id) {
    Optional<Book> result = this.books.stream().filter(book -> book.getId().equals(id)).findFirst();
    return result.orElse(null);
  }
  public ArrayList<Book> getBooks(){
    return this.books;
  }

  public void addOrder(Book book){
    boolean existed = false;
    for(Order order: cart){
      if(order.getBook().getId().equals(book.getId())){
        order.increaseQuantity();
        existed = true;
        break;
      }
    }
    if(!existed){
      cart.add(new Order(book,1));
    }
  }

  public void removeOrder(Book book){
    int removed = -1;
    for(int i=0;i<cart.size();i++){
      Order order = cart.get(i);
      if(order.getBook().getId().equals(book.getId())) {
        if (order.getQuantity() == 1) {
          removed = i;
        } else {
          order.decreaseQuantity();
        }
      }
    }
    if(removed != -1){
      cart.remove(removed);
    }
  }

  public int getOrderQuantityById(String id){
    return this.cart.stream()
        .filter(order -> order.getBook().getId().equals(id))
        .findFirst()
        .orElse(new Order(null,0))
        .getQuantity();
  }

  public void saveToSession(HttpServletRequest request){
    HttpSession session = request.getSession();
    session.setAttribute("cart",this.cart);
  }

  public void restoreFromSession(HttpServletRequest request){
    HttpSession session = request.getSession();
    Object cart = session.getAttribute("cart");
    if(cart == null) {
      this.cart = new ArrayList<>();
    }
    else {
      this.cart = (ArrayList<Order>) cart;
    }
  }

}

