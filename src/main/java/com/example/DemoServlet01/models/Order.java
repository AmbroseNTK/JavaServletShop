package com.example.DemoServlet01.models;

public class Order {

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  private Book book;
  private int quantity;

  public Order(Book book, int quantity) {
    this.book = book;
    this.quantity = quantity;
  }

  public int getQuantity() {
    return quantity;
  }

  public void increaseQuantity(){
    this.quantity++;
  }
  public void decreaseQuantity(){
    this.quantity--;
  }

  public double total(){
    return this.book.getPrice()*this.quantity;
  }

}
