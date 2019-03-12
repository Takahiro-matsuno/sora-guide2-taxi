function getDetail(id) {
    //フォーム作成
    var form = document.getElementById("listForm" + id);
    form.submit();
}

function searchAreaClicked(){
    obj=document.getElementById('search_area').style;
    obj.display=(obj.display=='flex')?'none':'flex';
}

function updateList(){
    document.searchForm.reset();
    document.searchForm.submit();
}