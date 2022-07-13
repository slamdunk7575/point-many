## Point-Many
포인트 마니: 마일리지 서비스


## Dev Env
- JAVA 11
- Spring Boot 2.7.1
- Spring Data JPA
- Redis(Redisson 3.17.3)
- MySQL 8.0
- JUnit 5


## How To Run
~~~  
Application 실행 전 로컬에 Redis, MySQL 인스턴스가 실행중이여야 합니다.

Docker를 설치하고 아래 명령어로 띄울 수 있습니다.
$ docker run -p 6379:6379 --name boot-redis -d redis
$ docker run -d 3306:3306 -e MYSQL_ROOT_PASSWORD=root --name mysql mysql
~~~

프로젝트를 Clone or Download 합니다.
<br>터미널을 실행시켜 해당 폴더로 이동합니다.
~~~
$ gradle bootjar
~~~

build/libs 경로에 보면 모든 의존성 라이브러리가 포함된 jar 파일을 확인 할 수 있습니다.
<br>아래 명령어로 Application 을 실행할 수 있습니다.
~~~
nohup java -jar ~~~.jar &
tail -f nohup.out
~~~


## DB Schema
~~~  
create table place (
        id bigint not null auto_increment,
        location varchar(255) comment '위치',
        name varchar(255) not null comment '장소명',
        place_id varchar(255),
        primary key (id)
)

create table point (
        id bigint not null auto_increment,
        bonus_score integer comment '보너스점수',
        contents_score integer comment '내용점수',
        point_id varchar(255),
        primary key (id)
) 

create table review (
        id bigint not null auto_increment,
        contents longtext comment '내용',
        image varchar(255) comment '이미지 주소',
        review_id varchar(255),
        name_id bigint,
        user_id bigint,
        primary key (id)
)

create table user (
        id bigint not null auto_increment,
        email varchar(255) comment '이메일',
        user_id varchar(255),
        point_id bigint,
        primary key (id)
)

alter table review 
       add constraint FK6cgvwabh9te9mdmpbuxup46p1 
       foreign key (name_id) 
       references place (id)
       
alter table review 
       add constraint FKiyf57dy48lyiftdrf7y87rnxi 
       foreign key (user_id) 
       references user (id)

alter table user 
       add constraint FKpcidb4yliq94jogckhjhoiqrw 
       foreign key (point_id) 
       references point (id)
~~~

* 인덱스 추가
~~~ 
create index idx_place_id on place (place_id)
create index idx_review_id on place (review_id)
create index idx_user_id on place (user_id)
~~~


## Solve
사용자가 Review 를 추가, 삭제하는 과정에서 동시성 문제가 발생할 것으로 생각하였습니다.
<br>여러 서버를 운영하는 분산환경에서 동기화 처리를 위해 Redis 분산락을 이용하여 서버에 공통된 락을 적용해봤습니다.


## ToDo
테스트 코드 추가
