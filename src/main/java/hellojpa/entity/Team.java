package hellojpa.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Team {
	@Id @GeneratedValue
	private Long id;
	private String name;
	
	@OneToMany(mappedBy = "team")	//양방향 매핑을 해주고 싶다면 반대의 경우로도 매핑해주면 된다. Team은 Member와 1..* 관계
	List<Member> members = new ArrayList<Member>();
}
