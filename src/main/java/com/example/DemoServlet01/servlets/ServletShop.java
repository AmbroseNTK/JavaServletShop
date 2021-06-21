package com.example.DemoServlet01.servlets;

import com.example.DemoServlet01.models.Book;
import com.example.DemoServlet01.services.Database;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletShop", value = "/shop", loadOnStartup = 1)
public class ServletShop extends HttpServlet {

    public void init() {
        try {
            Database.GetInstance().loadData("D:/books.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Database.GetInstance().restoreFromSession(request);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h1>Shop Frontpage</h1>");
        out.println("<a href='"+getServletContext().getContextPath()+"/checkout'>Checkout</a>");
        out.println("<h2>Shop items</h2>");
        for(Book book : Database.GetInstance().getBooks()){
            out.println("<br>"
                + "<p>"+book.getId()+": "+book.getName()+"</p>"
                + "<p>Price:"+book.getPrice()+" USD</p>"
                + "<form action='"+getServletContext().getContextPath()+"/shop?id="+book.getId()+"&type=add' method='POST'>"
                + "<input type='submit' value='Add to cart'></input>"
                + "</form>"
                + "<form action='"+getServletContext().getContextPath()+"/shop?id="+book.getId()+"&type=remove' method='POST'>"
                + "<input type='submit' value='Remove from cart'></input>"
                + "</form>");
            int qty = Database.GetInstance().getOrderQuantityById(book.getId());
            if(qty!=0) {
                out.println("<i>Qty: " + Database.GetInstance().getOrderQuantityById(book.getId())+"</i>");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        if (type.equals("add")) {
            Database.GetInstance().addOrder(Database.GetInstance().getBookById(id));
        } else if (type.equals("remove")) {
            Database.GetInstance().removeOrder(Database.GetInstance().getBookById(id));
        }
        Database.GetInstance().saveToSession(request);
        doGet(request, response);
    }
}
