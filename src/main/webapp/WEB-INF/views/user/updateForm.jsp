<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">

	<form>
		<input type="hidden" id="id" value="${principal.user.id}"/>
		<div class="form-group">
			<label for="email">Email address:</label> 
			<input type="email" value="${principal.user.email}" class="form-control" placeholder="Enter email" id="email">
		</div>
		<div class="form-group">
			<label for="username">User Name:</label> 
			<input type="text" value="${principal.user.username}" class="form-control" placeholder="Enter user name" id="username" readonly>
		</div>
		<div class="form-group">
			<label for="pwd">Password:</label> 
			<input type="password"  class="form-control" placeholder="Enter password" id="password">
		</div>
		
	</form>
	<button id="btn-update" class="btn btn-primary">Update</button>
</div>

<!-- '/'라고 하면 바로 static으로 찾아간다 -->
<script src="/js/user.js"></script>
<%@ include file="../layout/footer.jsp"%>