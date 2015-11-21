package com.ysq.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.alibaba.fastjson.JSON;
import com.ysq.beans.User;
import com.ysq.utils.LoginResult;
import com.ysq.utils.ValidVerifyUtil;

/**
 * Servlet implementation class MobileServlet
 */
@WebServlet("/MobileServlet")
public class MobileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private JdbcTemplate jdbcTemplate;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result = "";
		String accessType = request.getParameter("method");
		ServletContext servletContext = getServletContext();
		ApplicationContext ctx = (ApplicationContext) servletContext.getAttribute("ApplicationContext");
		jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
		if ("login".equals(accessType)) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String sql = "select * from user_info where name = ? and password = ?";
			RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
			List<User> users = jdbcTemplate.query(sql, rowMapper, username, password);
			LoginResult loginResult = new LoginResult();
			if(users.size() > 0) {
				loginResult.setResult(true);
				loginResult.setSessionID(ValidVerifyUtil.getSessionID(username));
				result = getJsonFromObject(loginResult);
			} else {
				loginResult.setResult(false);
				result = getJsonFromObject(loginResult);
			}
		}
		/*String sql = "select id, name username, password from user_info";
		RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
		List<User> users = jdbcTemplate.query(sql, rowMapper);*/
		response.getWriter().append(result);
	}

	/**
	 * 将对象转换json字符串
	 * 如果存在开头结束没有'[' ']'补充此符号
	 * @param obj
	 * @return
	 */
	public static String getJsonFromObject(Object obj) {
		if (null == obj)
			return "";
		
		String json = JSON.toJSONString(obj);
		StringBuffer result = new  StringBuffer();
		if(!(obj instanceof ArrayList)){
			result.append("[").append(json).append("]");
			json = result.toString();
		}

		return json;
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
