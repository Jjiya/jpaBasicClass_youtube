package hellojpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="USERNAME") 
	private String name;
	private int age;

	/*
	 * 데이터 위주의 관계 매핑 방법. 그냥 Team객체의 pk값과 일치시키는 변수 생성
		 @Column(name = "TEAM_ID")
		 private Long teamId;
	 * */
	
	@ManyToOne (fetch = FetchType.LAZY) //연관관계 *..1, Member class입장에서의 Team class과의 관계
	@JoinColumn(name = "TEAM_ID")
	private Team team;
	
}

/**
 * 강의 3강 학습 위함
 * @Id
	private Long id;
	
	@Column(length = 20, name="USERNAME",nullable=true)
	private String name;
	private int age;
	
	@Temporal(TemporalType.TIME)
	private Date time;
	@Temporal(TemporalType.DATE)
	private Date date;
	@Temporal(TemporalType.TIMESTAMP)
	private Date ts;
	
	@Enumerated(EnumType.STRING)	//꼭 StringType으로 저장. default 값이 index처럼 들어감
	private MemberType memberType;
 */
