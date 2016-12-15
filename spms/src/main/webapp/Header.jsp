<%@page import="spms.vo.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="member" scope="session" class="spms.vo.Member" />
	SPMS(Simple Project Management System)
<div>
<%
	if (member.getEmail() != null) {
%>
	<span>
		<%=member.getName()%> <a href="<%=request.getContextPath()%>/auth/logout">로그아웃</a>
	</span>
<%
	}else{
%>
		<a href="<%=request.getContextPath()%>/auth/login">로그인</a>
<%
	}

%>
</div>