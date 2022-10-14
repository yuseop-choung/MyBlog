<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">

	<button id="btn-back" class="btn btn-primary" onclick="history.back()" style="background-color: grey;">Back</button>
	<c:if test="${board.user.id == principal.user.id }">
		<a href="/board/${board.id}/editForm">Edit</a>
		<button id="btn-delete" class="btn btn-primary" style="background-color: red;">Delete</button>
	</c:if>
	<br />
	<br />

	<div>
		글 번호: <span id="id"><i>${board.id} </i></span>
		작성자: <span><i>${board.user.username} </i> </span>
	</div>
	<div>
		<h3>${board.title}</h3>
	</div>
	<hr />
	<div>
		<div>${board.content}</div>
	</div>
	<hr />

</div>

<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp"%>