package com.chixing.dao;

import com.chixing.entity.Note;
import com.chixing.dao.example.NoteExample;
import org.springframework.stereotype.Repository;

/**
 * NoteDao继承基类
 */
@Repository
public interface NoteDao extends MyBatisBaseDao<Note, Integer, NoteExample> {

    public int selectCount();

}