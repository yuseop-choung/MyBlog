let index = {
	init: function() {
		$("#btn-save").on("click", ()=>{
			this.save();
		});
		$("#btn-delete").on("click", ()=>{
			this.deleteById();
		});
		$("#btn-edit").on("click", ()=>{
			this.edit();
		});
	},
	save: function() {
		let data = {
			title: $("#title").val(),
			content: $("#content").val(),
		};
		$.ajax({
			type: "POST",
			url: "/api/board",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json"
		}).done(function(response){
			alert("글쓰기가 완료되었습니다.");
			location.href = "/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	},
	deleteById: function() {
		let id = $("#id").text();
		console.log(id);
		$.ajax({
			type: "DELETE",
			url: "/api/board/"+id,
			dataType: "json",
		}).done(function(response){
			alert("삭제가 완료되었습니다.");
			location.href = "/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	},
	edit: function() {
		let id = $("#id").val();
		let data = {
			title: $("#title").val(),
			content: $("#content").val(),
		};
		$.ajax({
			type: "PUT",
			url: "/api/board/"+id,
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json"
		}).done(function(response){
			alert("글 수정이 완료되었습니다.");
			location.href = "/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	},
}

index.init();
