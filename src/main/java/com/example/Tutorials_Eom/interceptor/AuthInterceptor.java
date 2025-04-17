package com.example.Tutorials_Eom.interceptor;

import com.example.Tutorials_Eom.dto.UserLoginHomeDto;
import com.example.Tutorials_Eom.entity.Users;
import com.example.Tutorials_Eom.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    public AuthInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Users user = (Users) request.getSession().getAttribute("user");

        // เช็คว่า `/admin/` ให้เฉพาะ `admin01` เท่านั้น
        if (request.getRequestURI().startsWith("/admin/")) {
            if (user == null || !user.getUsername().equals("admin01")) {
                response.sendRedirect("/404"); // Redirect ไปหน้า 404 ถ้าไม่ใช่ admin01
                return false;
            }
        }

        if (user != null) {
            // ดึงข้อมูลจากฐานข้อมูล
            UserLoginHomeDto userInfo = userRepository.findUserDTOByUserId(user.getUserId());
            request.setAttribute("userInfo", userInfo); // ส่งข้อมูลไปยัง Model
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && request.getAttribute("userInfo") != null) {
            modelAndView.addObject("userInfo", request.getAttribute("userInfo"));
        }
    }
}
