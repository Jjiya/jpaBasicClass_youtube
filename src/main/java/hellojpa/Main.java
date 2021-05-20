package hellojpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import hellojpa.entity.Member;
import hellojpa.entity.MemberType;
import hellojpa.entity.Team;

public class Main {
	public static void main(String args[]) {

//		엔티티매니저팩토리는 하나만 생성 후 애플리케이션 전체에서 공유해야한다. 여러개 생성하면 안됨!! 성능이 몹시 낮아짐
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
//		엔티티매니저는 thread 간에 공유하면 안됨 사용하고 버려야 한다.
//		얘는 database connection당 묶인다고 봐야한다.
//		그래서 entityManager를 공유하게 되면 다른 사람의 db connection이랑 같이 공유하게 된다. rollabck이나 commit이런게 잘못 같이 될 수도 있으므로 꼭 한 쓰레드에서 사용하고 close...
		EntityManager em = emf.createEntityManager();
//		JPA의 모든 데이터 변경은 트랜잭션 안에서 실행한다. begin 후 commit까지 해야됨
		EntityTransaction tx = em.getTransaction();
		tx.begin();
//		정석 로직 try catch		각 코드에 대한 설명은 아래 단위 주석 내용을 참고
		try {
//			1. 팀을 저장
			Team team = new Team();
			team.setName("Team A");
			em.persist(team);
//			팀 추가하려면 그냥 새로 선언해주면 되네..
			Team team2 = new Team();
			team2.setName("Team B");
			em.persist(team2);
//			2. 회원을 저장
			Member member = new Member();
			member.setName("hello");
			member.setTeam(team);	//단방향 연관관계 설정, 참조 저장 / ID를 꺼내올 필요 없이 객체를 딱 넣어주면 된다.
			em.persist(member);
//			양방향 매핑 시 주의할 점 위의 member.setTeam()을 실행하지 않고 연관관계의 역방향에 값을 주입할 경우
			team.getMembers().add(member);
			
//			팀 추가한거 새로운 멤버 만들어서 저장해보기 => ?? 뭐여... row가 왜 2줄?
//			Member member2 = new Member();
//			member.setName("hello2");
//			member.setTeam(team2);
//			em.persist(member2);
			
//			em.flush();	//출력 되듯이...
//			em.clear();	// 캐시 삭제
//			3. 회원 조회
			Member findMember = em.find(Member.class, member.getId());
			Team findTeam = findMember.getTeam();

//			양방향 매핑. Team에 속한 Member 조회하기!
			List<Member> members = findTeam.getMembers();
			for(Member mem : members) {
				int i = 0;
				System.out.println(i + "번째 member: " + mem);
				i++;
			}
			
			tx.commit();
			/* 데이터 지향적인 방법... 연관 관계가 없음
			 * 아래 코드처럼 진행하면 이루어지는 순서...
			 * 팀을 저장한다 -> 회원을 저장한다 -> 저장된 회원을 id를 통해 찾아온다. -> id값을 조회해 온 결과에서 가져온다. -> 가져온 id값을 이용해 Team테이블에서 조회해온다.
			 * 테이블의 경우 외래키로 조인을 하면 되지만, 객체는 참조를 해서 연관된 객체를 찾기때문에 bad method이다... 거의 하드코딩 수준?
			 * 1. 팀을 저장
				Team team = new Team();
				team.setName("TeamA");
				em.persist(team);
				
			 * 2. 회원을 저장
				Member member = new Member();
				member.setName("member1");
				member.setTeamId(team.getId());
				em.persist(member);

			 * 3. 저장된 회원을 id를 통해 찾아온다.
				Member findMember = em.find(Member.class, member.getId());
			 * 4. id값을 조회해 온 결과에서 가져온다.
				Long teamId = findMember.getTeamId();
			 * 5. 가져온 id값을 이용해 Team테이블에서 조회해온다.
				Team findTeam = em.find(Team.class, teamId);
			*/

		}catch (Exception e) {
			tx.rollback();
		}finally {
			em.close();
		}
		emf.close();

		/*	em을 통해서 해야할 것 및 설명
			//1. 트랜잭션을 얻어오기 → jpa의 모든 활동은 트랜잭션 안에서 이루어져야한다.
			EntityTransaction tx = em.getTransaction();
			tx.begin();	//트랜잭션 시작_ db에 접근해서 connection을 가져온 후 트랜잭션을 시작 함
			//2. 멤버객체 저장
			Member member = new Member();
			member.setId(100L);
			member.setName("안녕하세용");
			//3. jpa에 저장(INSERT)
			em.persist(member);
			//4. 꼭 트랜잭션을 commit을 해줘야 위의 쿼리문들이 일괄 실행됨
			tx.commit();
			//5. EntityManager 닫아주기
			em.close();
			//6. EntityMangerFactory 닫아주기		
			emf.close();
		 */
		
	}
}
