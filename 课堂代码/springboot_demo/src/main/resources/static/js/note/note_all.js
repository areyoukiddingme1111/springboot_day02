var currentPage;//当前页码
function loadNote(pageNum){
    currentPage = pageNum;



    $.get("note/getall/"+pageNum,{},function (result) {
        var noteList = result.data.noteList;
        var totalCount = result.data.totalCount;
        var totalPage = result.data.totalPage;


        //上下页码显示
        if(pageNum==1)
            $(".provious").hide();
        else
            $(".provious").show();


        if(pageNum==totalPage)
            $(".next").hide();
        else
            $(".next").show();


        //设置页码信息
        $(".totalPage").text(totalPage);
        $(".totalCount").text(totalCount);
        $(".currentPage").text(pageNum);


        //设置游记信息
        $("#hot_note ul li").remove();
        var ul = $("#hot_note ul");

        var ele = " <li>\n" +
            "                <div class=\"note_img\">\n" +
            "                    <a href=\"\" ><img src=\"/images/a.jpeg\"></a>\n" +
            "                    </div>\n" +
            "                <div class=\"note_detail\">\n" +
            "                    <a href=\"\" class=\"note_title\"></a>\n" +
            "                    <span>坐标：</span><span class='note_city'></span>\n" +
            "                    <span>作者：</span><a href=\"\" class=\"note_author\"><span></span></a>\n" +
            "                 </div>\n" +
            "            </li>";

        $.each(noteList,function (index,item) {
            ul.append(ele);
            var li =  $("#hot_note ul").children().eq(index);
            console.log(li.html());

            var note_title = li.children().find(".note_title");
            var note_city= li.children().find(".note_city");
            var note_author= li.children().find(".note_author");
            note_title.text(item.noteTitle);
            note_city.text(item.noteCity);
            note_author.text(item.others1);
        })
    });
}

loadNote(1);
