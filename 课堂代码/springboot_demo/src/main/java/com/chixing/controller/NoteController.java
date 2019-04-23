package com.chixing.controller;

import com.chixing.common.JsonResult;
import com.chixing.entity.Customer;
import com.chixing.entity.Note;
import com.chixing.service.CustomerService;
import com.chixing.service.NoteService;
import com.chixing.util.PageHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;
    @Autowired
    private CustomerService customerService;




    //分页查询游记
    @RequestMapping("getall/{pageNum}")
    @ResponseBody
    public  JsonResult getByPage(@PathVariable("pageNum")int pageNum){
        // 5条游记（cust_id）
           List<Note> noteList =  this.noteService.getAll(pageNum); // 当前页码的游记集合对象
            int totalCount = this.noteService.getCount();//游记总记录数
            int totalPage =  (int)  Math.ceil(totalCount% PageHelperUtil.PAGE_SIZE==0?totalCount/ PageHelperUtil.PAGE_SIZE:totalCount/ PageHelperUtil.PAGE_SIZE+1);//共几页
        //
              //游记作者的信息


            for(Note note:noteList){
                Customer customer =  this.customerService.getById(note.getCustId());
                note.setOthers1(customer.getCustName());
            }



            Map<String,Object> data = new HashMap<>();
            data.put("noteList",noteList);        // 当前页码的游记作者对象
            data.put("totalCount",totalCount);    //游记总记录数
            data.put("totalPage",totalPage);      //共几页
            return   JsonResult.createSuccessJsonResult(data);
    }







    @RequestMapping("goToDetail")
    public String goToDetail(){
        return "note/detail";
    }

    @RequestMapping("goToMyNoteList")
    public String goToMyNoteList(){
        return "note/mylist";
    }


    @RequestMapping("goToDescriptionById/{noteId}")
    public String goToDescription(@PathVariable("noteId") int noteId){
        System.out.println("noteId:"+noteId);
        return "note/description.html?noteid="+noteId;
    }




}
