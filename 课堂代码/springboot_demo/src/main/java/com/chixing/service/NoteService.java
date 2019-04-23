package com.chixing.service;

import com.chixing.entity.Note;

import java.util.List;

public interface NoteService {


    /**
     * 根据游记Id查询游记
     * @param noteId 当前游记id
     * @return 游记对象
     */
    public Note getById(int noteId);

    /**
     * 分页查询游记
     * @param pageNum 页码
     * @return  返回当前页面的游记信息
     */
    public List<Note> getAll(int pageNum);

    /**
     * 获得游记总记录数
     * @return 返回游记总记录数
     */
    public int getCount();
}
