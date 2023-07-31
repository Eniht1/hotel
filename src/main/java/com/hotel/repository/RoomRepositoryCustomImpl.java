package com.hotel.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.hotel.constant.RoomSellStatus;
import com.hotel.dto.RoomSearchDto;
import com.hotel.dto.MainRoomDto;
import com.hotel.dto.QMainRoomDto;
import com.hotel.entity.Room;
import com.hotel.entity.QRoom;
import com.hotel.entity.QRoomImg;

import jakarta.persistence.EntityManager;

public class RoomRepositoryCustomImpl implements RoomRepositoryCustom {

	private JPAQueryFactory queryFactory;
	
	private RoomRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	//현재 날짜로부터 이전날짜를 구해주는 메소드
	private BooleanExpression regDtsAfter(String searchDateType) {
		LocalDateTime dateTime = LocalDateTime.now(); //현재 날짜, 시간
		
		if(StringUtils.equals("all", searchDateType) || searchDateType == null) 
			return null;
		else if(StringUtils.equals("1d", searchDateType))
			dateTime = dateTime.minusDays(1); //현재 날짜로부터 1일전
		else if(StringUtils.equals("1w", searchDateType))
			dateTime = dateTime.minusWeeks(1); //1주일 전
		else if(StringUtils.equals("1m", searchDateType))
			dateTime = dateTime.minusMonths(1); //1달전
		else if(StringUtils.equals("6m", searchDateType))
			dateTime = dateTime.minusMonths(6); //6개월전
		
		return QRoom.room.regTime.after(dateTime); //Q객체 리턴
	}
	
	//상태를 전체로 했을때 null이 들어있으므로 처리를 한번 해준다
	private BooleanExpression searchSellStatusEq(RoomSellStatus searchSellStatus) {
		return searchSellStatus == null ? null : 
			QRoom.room.roomSellStatus.eq(searchSellStatus);
	}
	
	
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if(StringUtils.equals("roomNm", searchBy)) {
			//등록자로 검색시
			return QRoom.room.roomName.like("%"+ searchQuery +"%"); //room_name like %검색어%
		} else if(StringUtils.equals("createdBy", searchBy)) {
			return QRoom.room.createdBy.like("%"+ searchQuery +"%"); //create_by like %검색어%
		}
		
		return null; //쿼리문을 실행하지 않는다.
	}
	
	//검색어가 빈문자열 일때를 대비해
	private BooleanExpression roomNmLike(String searchQuery) {
		return StringUtils.isEmpty(searchQuery) ? 
				null : QRoom.room.roomName.like("%" + searchQuery + "%");
	}

	@Override
	public Page<MainRoomDto> getMainRoomPage(RoomSearchDto roomSearchDto, Pageable pageable) {
		
		QRoom room = QRoom.room;
		QRoomImg roomImg = QRoomImg.roomImg;
		
		//dto로 객체로 바로 받아올 때는 
		//1.컬럼과 dto객체의 필드가 일치해야 한다.
		//2.dto객체의 생성자에 @QueryProjection를 반드시 사용해야 한다.
		List<MainRoomDto> content = queryFactory
				.select(
						new QMainRoomDto(
								room.id,
								room.roomName,
								room.price,
								roomImg.imgUrl,
								room.roomType,
								room.maxPerson,
								room.description)
						)
				.from(roomImg)
				.join(roomImg.room, room)
				.where(roomImg.repimgYn.eq("Y"))
				.where(roomNmLike(roomSearchDto.getSearchQuery()))
				.orderBy(room.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = queryFactory
				.select(Wildcard.count)
				.from(roomImg)
				.join(roomImg.room, room)
				.where(roomImg.repimgYn.eq("Y"))
				.where(roomNmLike(roomSearchDto.getSearchQuery()))
				.fetchOne();
		
		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Page<Room> getAdminRoomPage(RoomSearchDto roomSearchDto, Pageable pageable) {
		
		List<Room> content = queryFactory
				.selectFrom(QRoom.room)
				.where(regDtsAfter(roomSearchDto.getSearchDateType()),
						searchSellStatusEq(roomSearchDto.getSearchSellStatus()),
						searchByLike(roomSearchDto.getSearchBy(), roomSearchDto.getSearchQuery()))
				.orderBy(QRoom.room.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = queryFactory.select(Wildcard.count).from(QRoom.room)
				.where(regDtsAfter(roomSearchDto.getSearchDateType()),
						searchSellStatusEq(roomSearchDto.getSearchSellStatus()),
						searchByLike(roomSearchDto.getSearchBy(), roomSearchDto.getSearchQuery()))
				.fetchOne();
		
		return new PageImpl<>(content, pageable, total);
	}
	
}
