package com.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by R500 on 16.7.2014 г..
 */
@WebFilter("/AuthenticationFilter")
public class AuthenticationFilter implements Filter {
    private ServletContext context;

    public void init(FilterConfig filterConfig){
        this.context = filterConfig.getServletContext();
        this.context.log("AuthenticationFilter initialized");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain){
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;

        String uri = req.getRequestURI();
        this.context.log("Requested Resource::" + uri);

        HttpSession session = req.getSession(false);
        int access = session!=null&&session.getAttribute("access")!=null?(Integer)session.getAttribute("access"):0;

        if((session == null || session.getAttribute("email") == null) &&
                (!(uri.endsWith("index.jsp") || uri.endsWith("signUp.html") || uri.endsWith(".css") || uri.endsWith("/LoginServlet") || uri.endsWith("/SignUpServlet")))){
            this.context.log("Unauthorized access request");
            try {
                res.sendRedirect("index.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (session != null && access != 1 && (uri.endsWith("Admin.html") || uri.endsWith("controlPanel.html"))){
            this.context.log("Unauthorized access request");
            try {
                res.sendRedirect("NormalUser.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (session != null && access != 2 && uri.endsWith("NormalUser.html")){
            this.context.log("Unauthorized access request");
            try {
                res.sendRedirect("Admin.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                chain.doFilter(request, response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }

    public void destroy(){}
}
