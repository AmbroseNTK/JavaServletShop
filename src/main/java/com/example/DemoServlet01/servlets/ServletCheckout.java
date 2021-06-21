package com.example.DemoServlet01.servlets;

import com.example.DemoServlet01.models.Order;
import com.example.DemoServlet01.services.Database;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletCheckout", value = "/checkout")
public class ServletCheckout extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");

    PrintWriter out = response.getWriter();
    out.println("<html><body>");
    out.println("<h1>Checkout</h1>");
    out.println("<h2>Your cart</h2>");
    double total = 0;
    for (Order order : Database.GetInstance().getCart()) {
      out.println(
          "<p>" + order.getBook().getName() + ": " + order.getBook().getPrice() + "$ * " + order
              .getQuantity() + " = " + order.total() + "$ </p>");
      total += order.total();
    }
    out.println("<h2>Grand total: " + total + "$</h2>");
    out.println("</body></html>");
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
