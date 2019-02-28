function cancelCheck(id, lastUpdate){
    var result = window.confirm('取り消してよろしいですか？')

	if(result){
		//フォーム作成
        var form = document.createElement("form");
        form.setAttribute("action", "./cancelComplete");
        form.setAttribute("method", "post");
        form.style.display = "none";
        document.body.appendChild(form);

        //送信値
        var input = document.createElement('input');
        input.setAttribute('type',  'hidden');
        input.setAttribute('name', 'id');
        input.setAttribute('value', id);
        form.appendChild(input);

        input = document.createElement('input');
        input.setAttribute('type',  'hidden');
        input.setAttribute('name', 'lastUpdate');
        input.setAttribute('value', lastUpdate);
        form.appendChild(input);

        form.submit();
	}
}