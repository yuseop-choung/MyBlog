let index = {
	init: function() {
		//()=>{}사용 이유: this를 바인딩하기 위해서
		//()=>{}에서의 this: index 오브젝트
		//function(){}에서의 this: 윈도우 오브젝트
		$("#btn-save").on("click", ()=>{
			this.save();
		});	//클릭이 되었을 때 실행할 함수
	},
	save: function() {
		//alert('user의 save함수 호출됨');
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val(),
		};
		
		//console.log(data)
		
		//ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
		//ajax 호출 시 default가 비동기 호출
		//따라서 아래 코드가 있으면 ajax 내 코드와 아래 코드들과 병행 수행
		//병행 수행하다가 ajax내 코드 완료되면 아래 코드 중지 후 결과에 따라 done, fail 이동
		//ajax가 통신 성공 후, 서버가 json을 리턴해주면, 자동으로 자바 오브젝트로 변환해줌
		$.ajax({
			//회원가입 수행 요청
			type: "POST",
			url: "/blog/api/user",
			data: JSON.stringify(data),	//http의 body데이터 -> MIME 타입 필요
			contentType: "application/json; charset=utf-8",	//body데이터가 어떤 타입인지(MIME)
			dataType: "json"
			//요청에 대한 응답이 왔을 때, 응답의 타입 -> 기본적으로 문자열
			//생긴게 json이라면 => javascript 오브젝트로 변환
		}).done(function(response){
			//회원가입 수행 요청에 대한 응답 -> 정상 인 경우
			alert("회원가입이 완료되었습니다.");
			console.log(response);
			location.href = "/blog";
		}).fail(function(error){
			//회원가입 수행 요청에 대한 응답 -> 실패 인 경우
			alert(JSON.stringify(error));
		});
	}
}

index.init();
