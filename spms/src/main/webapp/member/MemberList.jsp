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
		
		int totalCount = Integer.parseInt(request.getAttribute("totalCount").toString());
		int pagePerRow = Integer.parseInt(request.getAttribute("pagePerRow").toString());
		int groupPagePer = Integer.parseInt(request.getAttribute("groupPagePer").toString());
		
		int currentPage = Integer.parseInt(request.getAttribute("currentPage").toString());
		System.out.println("currentPage:"+currentPage);
		
		int groupPage = (currentPage-1)/groupPagePer;	//현재페이지에 -1을(10때문에) 하고 보여줄 페지이당 개수로 나눠준다. 그럼 1~10페이지는 0을 11~20은 1이나옴
		groupPage = groupPage*groupPagePer;	//0, 1 값을 다시 보여줄 페이지당 개수로 곱해줌 (나누고 다시 곱하는데 뒤에 일의자리 떨어내기위함임)
		System.out.println("groupPage:"+groupPage);
		
		int lastPage = (totalCount/pagePerRow)+1;
		System.out.println("lastPage:"+lastPage);
		
		int lastGroupPage = lastPage/groupPagePer;
		lastGroupPage *= groupPagePer;
		System.out.println("lastGroupPage:"+lastGroupPage);
		
		if(groupPage!=0){
%>
		<a href="<%=request.getContextPath()%>/member/list?currentPage=<%=groupPage-groupPagePer+1%>">이전</a>
<%
		}
		if(lastGroupPage == groupPage){
			for(int i=0; i< lastPage-lastGroupPage; i++){
				%>
					<a href="<%=request.getContextPath()%>/member/list?currentPage=<%=groupPage+i+1%>"><%= groupPage + i+1 %></a>
				<%
			}
		}else{
		
			for(int i=0; i< groupPagePer; i++){
%>
				<a href="<%=request.getContextPath()%>/member/list?currentPage=<%=groupPage+i+1%>"><%= groupPage + i+1 %></a>
<%
			}
		}
		if(groupPage!=lastGroupPage){
%>
			<a href="<%=request.getContextPath()%>/member/list?currentPage=<%=groupPage+groupPagePer+1%>">다음</a>

<%
		}
%>
	<jsp:include page="/Tail.jsp" />
</body>
</html>