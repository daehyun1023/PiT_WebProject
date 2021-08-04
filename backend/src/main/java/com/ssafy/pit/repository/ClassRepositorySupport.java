package com.ssafy.pit.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.pit.entity.Classes;
import com.ssafy.pit.entity.QClasses;
import com.ssafy.pit.entity.QUserLikes;
import com.ssafy.pit.entity.User;

@Repository
public class ClassRepositorySupport {
	
	@Autowired
	private JPAQueryFactory query;
	
	@Autowired
	private CommentRepositorySupport commentRepositorySupport;
	
	@Autowired
	private ClassPhotoRepositorySupport classPhotoRepositorySupprot;
	
	@Autowired
	private CodeRepositorySupport codeRepositorySupport;
	
	QClasses qClass = QClasses.classes;
	QUserLikes qUserLikes = QUserLikes.userLikes;
	
	public List<Classes> getClassList() {
		return query.selectFrom(qClass).fetch();
		
	}
	
	public Classes getClassDetail(int classNo) {
		Classes classes = query.selectFrom(qClass).where(qClass.classNo.eq(classNo)).fetchOne();
		return classes;
	}

	public List<Classes> getClassLikesList(int userNo) {
		List<Classes> classesList = query.selectFrom(qClass).where(qClass.classNo.in(
				JPAExpressions.select(qUserLikes.classes.classNo).from(qUserLikes).where(qUserLikes.user.userNo.eq(userNo))
				)).fetch();
		
		return classesList;
	}

	public int getUserLikesNo(int userNo, int classNo) {
		int userLikesNo = query.select(qUserLikes.userLikesNo).from(qUserLikes).where(qUserLikes.user.userNo.eq(userNo).
				and(qUserLikes.classes.classNo.eq(classNo))).fetchOne();
		return userLikesNo;
	}
	
	
}
