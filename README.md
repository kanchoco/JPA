# JPA
JPA 수업 내용 레파지토리입니다

### ORM(Object Relational Mapping)

  진영과 RDB 진영을 자동으로 매핑하여 구조의 불일치를 개발자 대신 해결해주는 기술의 총칭이다.   
  객체지향 구조에서 프로그래밍 언어를 사용하여 RDB의 데이터를 조작하는 방법이다.   
  ORM을 사용하면 개발자가 SQL문을 직접 작성하지 않아도 RDB와 상호작용할 수 있다.

  
### JPA(Java Persistence API)

  ORM을 사용하기 위한 설계도(틀)이다.
  Java Application용 RDB 매핑 관리를 위한 인터페이스이며,  DBMS 벤더사에 의존하지 않고 독립적으로 ORM을 사용할 수 있는 ORM 표준이다.   
  인터페이스이기 때문에 구현되어 있지 않은 틀만 제공하며, 자체적인 작업을 수행하지 않는다.   
  JPA에 설계된 구조에 맞춰서 각 메소드를 재정의하여 직접 ORM을 구현하여 사용해야한다.   
  JPA는 ORM을 사용할 수 있는 JAVA 인터페이스를 제공하는 ORM 접근 방식이며, 구현되지 않은 JPA를 RM이라고 말하기는 어렵다.


### Hibernate Framework

모든 Java Application에 대해 객체 관계를 그대로 유지한 채 쿼리 서비스를 제공하는 오픈 소스(비용 없이 공개적으로 사용가능)의 경향 ORM이다.
JPA를 구현한 구현체이며, 여러 구현체 중 가장 대표적인 구현체이다.
객체 간 관계 구성을 지원하며, 상속, 지연성, 페이징 처리, 예외 처리 불필요를 지원한다.
예외 처리 불필요란, JPA만의 독자적인 예외를 생성하여 try catch 및 throws를 강제시키지 않고
트랜잭션을 지원하는 Spring Framework가 추상화한 예외로 변환 시켜 커밋하지 않고 롤백시키도록 해준다.

```
                  Application ---------> JPA 표준 인터페이스
                                   ▲
                                   │
                                   | 
                    ┌──────────────┼────────────────┐
                    │              │                │
                Hibernate     EclipseLink      DataNucleus
```

### Spring Data JPA

  JPA를 추상화한 Repository 인터페이스를 제공하여 JPA를 쓰기 편하게 다양한 기능을 지원한다.   
  내부적으로는 JPA를 사용한다. 그래서 JPA를 모르면 내부 구조를 이해하기 힘들 수 있다.


### 객체와 관계형 데이터베이스의 차이
#### 1. 상속
```
  	[개발자]	      [기획자]

	번호		번호
	---------------	----------------
	이름		이름
	생년월일		생년월일
	경력		경력
	기술등급		OA등급
	프로젝트 수	클라이언트 수

	●●●●●●● 또는 ●●●●●●● 

	[사원]
	번호
	----------
	이름
	생년월일
	경력
	기술등급
	프로젝트 수
	OA등급
	클라이언트 수

	▷ 슈퍼 - 서브 타입 도출
	
	[사원] - 슈퍼

	번호(PK)
	----------
	이름
	생년월일
	경력

	[개발자] - 서브	[기획자] - 서브

	  번호(PK, FK)	  번호(PK, FK)
	-------------------   ------------------
	기술등급		OA등급
	프로젝트 수	클라이언트 수
```
  
1:1 관계에서 INSERT를 하기 위해서는 쿼리를 2번 작성해야하는 불편함이 생긴다.   
게다가 조회를 할 때에는 JOIN을 사용해야 하는데 쿼리가 굉장히 복잡해진다.   
만약에 이런 RDB의 테이블 관계를 자바 컬렉션으로 바꿀 수 있다면,    
  
===> ``` Developer developer = list.get(developerId);``` 이와 같이 간단하게 조회할 수 있다.

   
#### 2. 연관관계
 객체는 하위 연산자(.)를 사용하여 참조를 한다.    	
 >__"접근방향에 포커스한다."__

 ▶ 객체 연관 관계(상속이랑 헷갈리지 않도록 주의)
 - 단방향(Flower에서 Pot은 접근 가능하지만, Pot에서 Flower를 접근할 수 없다)
    
```
Flower	   →  	Pot	

id		id
flowerName	shape
Pot pot		color
```
    
 ▶ RDB 연관 관계
 - 양방향(FLOWER에서 POT을, POT에서 FLOWER를 접근할 수 있다.)
    
```
FLOWER	       ↔	  POT

FLOWER_ID(PK)       POT_ID(PK)
------------------     --------------
FLOWER_NAME       POT_SHAPE
POT_ID(FK)	  POT_COLOR
```
    
▶ 테이블을 객체에 맞추어 설계
```
Class Flower{
	String flowerId;
	String flowerName;
	Pot pot;
}
```
위와 같이 RDB를 객체방식으로 설계하면,   
조회 시 JOIN을 하여 FLOWER와 POT에서 각각 필요한 정보를 가져와   
각 객체에 담아준 뒤, flower.setPot(pot) 형태와 같이 복잡하게 작업해야 한다.   

하지만 만약 자바 컬렉션으로 관리가 가능하다면.   
---> 두 번 셀렉트할 필요가 없어진다.   
ex>
```java
list.add(flower);
Flower flower = list.get(flowerId);
Pot pot = flower.getPot();
```

#### 3. 그래프 탐색
```
			Market
			    ｜
			    ｜
			Flower - Pot
			    ｜
			    ｜
			Order - Client
			    ｜
			    ｜
			Delivery
```
__객체는 모든 객체 그래프를 탐색할 수 있어야 한다.__
```java
market.getFlower();
flower.getPot();
...
```
하지만 SQL 작성 시 이미 탐색 범위가 결정된다.
만약 Market과 Flower를 JOIN해서 조회를 한다면,

market.getFlower();는 가능 하지만,
market.getPot(); 등은 할 수 없음(Null)

따라서 엔티티(market, flower ..)에 대한 신뢰가 무너질 수 밖에 없다.
```java
Market market = marketDAO.find(marketId);
market.getFlower();		//null이 아니라고 확신할 수 없다.
market.getOrder().getClient();  //null이 아니라고 확신할 수 없다.
```
marketDAO에 있는 find()를 분석해보지 않는 이상 각 엔티티에 대해 신뢰할 수 없다.

따라서 상황에 따라 조회에 대한 메소드를 여러 개 선언해놓아야 한다.
```java
marketDAO.getFlower();
marketDAO.getMemberWithFlower();	<---조인
...
```
하지만 위와 같은 방법은 사실상 불가능에 가깝다.

#### 4. 값 비교
SQL 실행 결과를 담은 뒤 생성자를 호출하여 객체에 담으면 매번 new가 사용되기 때문에    
동일한 조회 결과의 객체일지라도 주소가 모두 다르다.

하지만 만약 자바 컬렉션에서 객체 조회가 가능하다면   
```java
list.get(memberId) == list.get(memberId)   
```
같은 객체를 가져오기 때문에 주소가 같다.   

즉, 객체지향으로 설계할 수록 작업이 오히려 복잡해지고 늘어나기 때문에 RDB 중심으로 설계할 수 밖에 없다.
RDB를 자바 컬렉션에 저장하듯 사용하면 굉장히 편해지고 많은 문제가 해결되는데, 
바로 이 기술을 JPA(Java Persistence API)라고 한다.

 
### JPA를 사용해야 하는 이유
1. SQL 중심 개발에서 객체 중심으로 개발하기 위해서 

2. 생산성
	저장 : jpa.persist(market);
	조회 : jpa.find(marketId);
	수정 : market.setMarketName("새로운 이름");
	삭제 : jpa.remove(market);

3. 유지보수
	클라이언트가 새로운 필드를 요청하여 새로운 필드 추가 시 
	크래스 안에 필드만 한 개 추가하면 끝. SQL문을 수정할 필요 없음.

4. 패러다임의 불일치 해결
```
		▶ JPA와 상속 
			
			Employee	      	→	Developer extends Employee

			employeeId		developerId
			employeeName		developerLevel
						projectCount

			- INSERT

				▷ 개발자
					jpa.persist(developer);

				▷ JPA
					JPA가 알아서 INSERT 두 번 해줌.

				자식 필드에 부모 필드가 포함되어 있기 때문에 필요한 데이터를 자식 객				체에 채우기만 하면 됨.

			- SELECT
				
				▷ 개발자
					//소속을 전달해줘야 한다.
					jpa.find(Developer.class, developerId);

				▷ JPA
					JPA가 알아서 부모 테이블(Employee)과 JOIN해서 데이터를 					가져옴.

		▶ JPA와 연관관계

			Flower		→ 	Pot

			flowerId			potId
			name			shape
			pot			color
			
			flower.setPot(pot);
			jpa.persist(flower);

			jpa.find(Flower.class, flowerId);		

		▶ JPA와 객체 그래프 탐색

			Market
			    ｜
			    ｜
			Flower - Pot
			    ｜
			    ｜
			Order - Client
			    ｜
			    ｜
			Delivery

			Flower flower = jpa.find(Flower.class, flowerId);
			Pot pot = flower.getPot();

			marketDAO.find(marketId);
			market.getFlower();
			market.getOrder().getClient();
			
			※ SELECT 결과가 없으면 문제가 생기기 때문에 NPE 체크는 반드시 해야 한다.					(optional)

		▶ JPA와 값 비교
			Market market1 = jpa.find(Market.class, marketId);
			Market market2 = jpa.find(Market.class, marketId);

			market1 == market2;		
		==> 무조건 TRUE
```
동일쿼리 사용 시, 쿼리를 실행하는게 아니라 그 값을 불러온다.(성능최적화)

동일한 트랜잭션에서 조회한 엔티티는 무조건 같다.


### JPQL
- JPA만으로는 검색 조건의 한계가 발생한다.(전체 조회, 선택 조회)

- 그래서 JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어를 제공한다.

- SQL과 문법이 유사하고, SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN등을 지원한다.

- JPQL은 엔티티 객체를 대상으로 쿼리를 질의하고

- SQL은 데이터베이스 테이블을 대상으로 쿼리를 질의한다.

### 벌크 연산
	- 여러 건(대량의 데이터)을 한 번에 수정하거나 삭제하는 방법
	- executeUpdate()
	- ※영속성 컨텍스트를 무시하고 SQL을 반영한다. -> DB에서는 업데이트가 되지만, 영속성 컨텍스트는 변하지 않는다
	 --->동일한 트랜잭션내에서 연산시 영속성 컨텍스트를 비우고 실행한다.(entityManager.clear())

### @Query 어노테이션
- JPA에서 제공하는 쿼리 메소드 기능의 하나로, 직접 쿼리를 작성하는 것
- default 가 executeQuery()이기 때문에 update문, delete문은 @Modifying(clearAutomatically = true)를 정의해서 사용



### 임베디드
1. 임베디드(@Embeddable(선언)   @Embedded(사용))   ::: "모듈화(재사용할 수 있도록 정의)"
    - 기존에 있는 것에서 새로운 것을 추가하는것

2. 여러 값을 하나의 값으로 묶음
    - 클래스를 분리해서 하나로 쓸 수 있다.

Ex) @Embedded
      private Hardware hardware;

※상속이 아니라, 모듈을 사용할 때 작성한다.


### @MappedSuperClass
조회시 조인이 되지 않는다(@Entity를 사용하는 이유)
--> Entity가 되어있는 곳에서 조회를 할 때 사용
상속을 하지만, 상속이 필요없을 때 사용한다!
객체간의 상속은 맞지만 RDB로는 상속이지 않는 모호한 상속..
```
사용이유-------
임베디드, 임베더블과 흡사해 보인다.
-> 임베디드를 사용하지 않기 위해 사용하는 문법이다. 
-- > 임베디드를 사용할 때는 2번 접근해서 사용하게 된다, ex)Em.xx -> 복잡함
접근을 줄여서 복잡함을 해결하기 위해 @MappedSuperClass를 사용한다.
------------------
```


### public abstract class
만약 상속관계에서 부모 클래스를 직접 사용할 일이 없으면 abstract 키워드를 붙여준다.
--> 상속이 필요없어진다. --> @MappedSuperclass 랑 함께 사용한다

* * *
### 연관관계
즉시 로딩    
	- 연관관계를 맺고있는 객체를 모두 조인해서 가져와줌, - 실무에서 절대 사용하지 말것   
	- @ManyToOne에 fetch속성으로 부여할 수 있으며,(FetchType.EAGER)    
  	  One의 디폴트 타입이다   
	- 실행되면서 바로 값을 넣기때문에 실행이후 프록시를 날린다고해서 초기화되지 않는다.

지연 로딩★(신경쓸게 많으니 사용시 유의)   
	- 즉시 로딩과 반대   
	- @ManyToOne에 fetch속성으로 부여할 수 있으며,(FetchType.LAZY)     
  	 Many의 디폴트 타입이다   
	- Proxy를통해 가져옴으로 프록시를 초기화하면 값또한 날라간다, /id만 남음   
	

### 양방향

>__FK -> "다"__

연관관계의 주인 - 을 파악   
- 다대일 양방향 관계
- 양방향에서는 연관관계의 주인을 정해야 한다.
- DBMS에서는 FK로 양방향 조회가 가능하지만, 객체에서는 객체 간 참조 2개인 당방향 2개로 설정한다.
- 만약 주인을 정해주지 않으면, DBMS쪽에서는 2개의 연관관계인지, 하나의 연관관계를 단방향 2개로 설정한건지 알 수가 없다.
- 영속성 컨텍스트도 복잡해질 수 있으며, 불필요한 중간 테이블이 생성될 수도 있기 때문에
- 하나의 연관관계에서 양방향 설계 시 하나의 객체에서만 FK를 관리해야 한다.
- 이 때 FK를 관리하는 객체를 연관관계의 주인이라고 하며, Pet엔티티에서 FK(owner_Id)를 관리하는 객체는 owner이다.

팀 설계할때는 양방향 사용하지 말기.   
*문제가 많이 발생하기 때문에

N + 1 문제    
조회하는 개수만큼 쿼리가 실행   
	- 양방향에서 필드조회를 할 때 select문이 사용된다.   
--> 해결 방법 페치 조인을 사용한다.


### 페치조인(fetch join)
- 페치조인(fetch join)   --> 상위 문법이기 때문에 dialect를 12로 수정해야한다.
- 양방향과 단방향의 장점만을 모아둔 조인
```
@Query("select p from Pet p join fetch p.owner")
or
@EntityGraph(attributePaths="owner")
@Query("select p form Pet p ")
select를 할 때 알아서 fetch ~문과 관련된 정보를 조인해서 가져옴

기존 select메소드에
@Override
@EntityGraph(attributePaths = "owner")   --> 여러개 가능.{"owner", "..."}
하면 fetch조인으로 바뀜
```

* * *

### QueryDSL
- 빌드를 시켜야만 사용가능하다.(Q(entity))   
-> 수정 시 반드시 컴파일을 통해 빌드를 생성한다

- 쿼리팩토리를 이용해서 사용한다   
-> 빈으로 등록해서 주입하는 방법으로 간소화할 수 있다.

- entilty는 화면에서 사용하지 않는다. --> entity는 서비스까지 사용하고 서비스에서 리턴타입을 DTO로   
-> @Query(select new com.example.advanced.entity.OwnerDTO(o.필드, o.필드) from Owner)

### @QueryProjection
빌드 시 DTO생성, |DTO로 조회시 생성자에 붙이는 어노테이션   
-그러나 DTO에 QueryDSL 어노테이션을 유지해야 하는 점과(의존성의 추가), DTO까지 Q파일을 생성해야 한다.   
-> 컴파일 시에 타입 체크 가능

직접 생성자를 호출하는 경우   
 - 굳이 컴파일로 확인하지 않아도 된다.   
 - 허나 코드가 복잡하다.
 
>**selectFrom([table])
>from과 select를 나누기 보단 selectFrom 절을 쓰는것이 더 낫다.

* * *
### QDSL 동적쿼리

where()안에서 메소드로 조건을 검색한다.(null인지 아닌지 검사하는 메소드를 생성)

1. booleanBuilder를 통해 조건 나열
```java
    private List<User> findByClassNameAndLevelUnder(String className, Integer level){
        BooleanBuilder builder = new BooleanBuilder();

        if(!Objects.isNull(className)){  	//조건1
            builder.and(user.battleClass.className.eq(className));
        }

        if(level > 0){ 			// 조건2|and로 묶음
            builder.and(user.level.lt(level));
        }

        List<User> results = query
                .selectFrom(user)
                .where(builder)		//사용할 때는 builder만 넣음
                .fetch();

        return results;
    }
```

2. BooleanExpression을 통해 특정 조건을 메소드화
- 이렇게 하면 이 조건을 다양한 쿼리에서 사용할 수 있으므로 확장성에 있어서 유리하다   >> 실무에서 많이 사용한다.

*booleanExpression 생성*
```java
	//조건 1
    private BooleanExpression findByClassName(String className){
        return Objects.isNull(className) ? null : user.battleClass.className.eq(className);
    }
	//조건 2
    private BooleanExpression findByLevelUnder(Integer level){
        return level < 0 ? null : user.level.lt(level);
    }
```
*booleanExpression 사용*
```java
 private List<User> findByClassNameAndLevelUnderWithBooleanExpression(String className, Integer  level){	
        return query
                .selectFrom(user)   
	//만든 booleanExpression 메소드를 where절에 넣어서 사용
                .where(findByClassName(className), findByLevelUnder(level))
                .fetch();
    }
```

*builder vs Expression*
- 성능에 차이는 없으나, 가독성, 재사용 면에서 차이가 있다
- 재사용이 자바라는 언어에서의 큰 장점이므로, expression을 더 많이 쓰는것같다
