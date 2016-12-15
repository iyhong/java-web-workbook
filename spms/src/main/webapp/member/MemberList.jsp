<%@page import="spms.vo.Member"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원 목록</title>
</head>
<body>
	<jsp:include page="/Header.jsp" />
	<h1>회원목록</h1>
	<p>
		<a href='add'>신규 회원</a>
	</p>
	총회원수 : <%= request.getAttribute("totalCount") %> <br>
	현재 페이지 : <%= request.getAttribute("currentPage") %><br>
	<jsp:useBean id="members" scope="request" class="java.util.ArrayList" type="java.util.ArrayList<spms.vo.Member>" />
<%
		for (Member member : members) {
%>
			<%=member.getNo()%>,
			<a href='update?no=<%=member.getNo()%>'><%=member.getName()%></a>,
			<%=member.getEmail()%>,
			<%=member.getCreatedDate()%>
			<a href='delete?no=<%=member.getNo()%>'>[삭제]</a>
			<br>
<%
		}
		
		//int totalCount = Integer.parseInt(request.getAttribute("totalCount").toString());
		//int pagePerRow = Integer.parseInt(request.getAttribute("pagePerRow").toString());
		int groupPagePer = Integer.parseInt(request.getAttribute("groupPagePer").toString());
		int startPage = Integer.parseInt(request.getAttribute("startPage").toString());
		int lastPage = Integer.parseInt(request.getAttribute("lastPage").toString());
		int endPage = Integer.parseInt(request.getAttribute("endPage").toString());
		
		if(startPage>1){
%>
		<a href="<%=request.getContextPath()%>/member/list?currentPage=<%=startPage-groupPagePer%>">이전</a>
<%
		}
		
		for(int i= startPage; i<endPage+1 ;i++){
%>
			<a href="<%=request.getContextPath()%>/member/list?currentPage=<%=i%>"><%=i %></a>
<%
		}
		
		if(endPage!=lastPage){
%>
			<a href="<%=request.getContextPath()%>/member/list?currentPage=<%=startPage+groupPagePer%>">다음</a>

<%
		}
%>
	<jsp:include page="/Tail.jsp" />
</body>
</html>