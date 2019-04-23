package com.chixing.service.impl;

import com.chixing.dao.NoteDao;
import com.chixing.entity.Note;
import com.chixing.service.NoteService;
import com.chixing.util.PageHelperUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteDao noteDao;






    @Override
    public Note getById(int noteId) {
        return noteDao.selectByPrimaryKey(noteId);
    }

    @Override
    public List<Note> getAll(int pageNum) {
        PageHelper.startPage(pageNum, PageHelperUtil.PAGE_SIZE);
        return this.noteDao.selectByExample(null);

    }

    @Override
    public int getCount() {
        return noteDao.selectCount();
    }


}
