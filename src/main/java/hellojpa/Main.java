package hellojpa;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import hellojpa.entity.Member;
import hellojpa.entity.MemberType;

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
			Member member = new Member();
			member.setId(200L);
			member.setName("안녕하세용");
			member.setMemberType(MemberType.ADMIN);
			member.setAge(100);
			member.setDate(new Date());
			member.setTime(new Date());
			member.setTs(new Date());
			em.persist(member);

			tx.commit();
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
