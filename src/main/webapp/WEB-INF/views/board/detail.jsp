<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">

	<button id="btn-back" class="btn btn-primary" onclick="history.back()" style="background-color: grey;">Back</button>
	<c:if test="${board.user.id == principal.user.id }">
		<a href="/board/${board.id}/editForm">Edit</a>
		<button id="btn-delete" class="btn btn-primary" style="background-color: red;">Delete</button>
	</c:if>
	<br /> <br />

	<div>
		글 번호: <span id="id"><i>${board.id} </i></span> 작성자: <span><i>${board.user.username} </i> </span>
	</div>
	<div>
		<h3>${board.title}</h3>
	</div>
	<hr />
	<div>
		<div>${board.content}</div>
	</div>
	<hr />

	<div class="card">
		<form>
			<input type="hidden" id="userId" value="${principal.user.id}"/>
			<input type="hidden" id="boardId" value="${board.id}"/>
			<div class="card-body">
				<textarea id="reply-content" class="form-control" rows="1"></textarea>
			</div>
			<div class="card-footer">
				<button type="button"  id="btn-reply-save" class="btn btn-primary">Enter</button>
			</div>
		</form>
	</div>
	<br />
	<div class="card">
		<div class="card-header">Comments</div>
		<ul id="reply-box" class="list-group">
			<c:forEach var="reply" items="${board.replys}">
				<li id="reply-${reply.id}" class="list-group-item d-flex justify-content-between">
					<div>${reply.content}</div>
					<div class="d-flex ">
						<div class="font-italic">작성자 : ${reply.user.username}&nbsp;</div>
						<button onClick="index.replyDelete(${board.id}, ${reply.id})" class="badge">remove</button>
					</div>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>

<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp"%>