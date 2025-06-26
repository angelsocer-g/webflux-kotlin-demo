# Elasticsearch to PostgreSQL Data Processor
# Elasticsearch에서 PostgreSQL로 데이터 처리기

## Project Overview
## 프로젝트 개요

This application periodically queries documents from Elasticsearch based on specified conditions, processes the data, and stores it in a PostgreSQL database. The entire process is implemented using reactive programming with Kotlin coroutines and Spring WebFlux.
이 애플리케이션은 지정된 조건에 따라 Elasticsearch에서 주기적으로 문서를 조회하고, 데이터를 가공한 후 PostgreSQL 데이터베이스에 저장합니다. 전체 프로세스는 Kotlin 코루틴과 Spring WebFlux를 사용한 리액티브 프로그래밍으로 구현되었습니다.

## Flow
## 처리 흐름

1. **Scheduler**: Periodically triggers the document processing flow
2. **Service Layer**: Initiates coroutine flow for processing
3. **Elasticsearch Client**: Queries documents using coroutines
4. **Document Parsing & Mapping**: Converts DTOs to entities
5. **R2DBC Repository**: Stores data in PostgreSQL using coroutines
6. **Result Logging / Error Handling**: Comprehensive logging and error handling


1. **스케줄러**: 주기적으로 문서 처리 흐름을 트리거
2. **서비스 계층**: 처리를 위한 코루틴 플로우 시작
3. **Elasticsearch 클라이언트**: 코루틴을 사용한 문서 쿼리
4. **문서 파싱 및 매핑**: DTO를 엔티티로 변환
5. **R2DBC 리포지토리**: 코루틴을 사용하여 PostgreSQL에 데이터 저장
6. **결과 로깅 / 오류 처리**: 포괄적인 로깅 및 오류 처리

## Technology Stack
## 기술 스택

### Core Platform & Languages
### 핵심 플랫폼 및 언어
- Java 21
- Kotlin 1.9.25
- Spring Boot 3.5.3

### Key Dependencies
- **Spring WebFlux**: Reactive web application development
- **Spring Data R2DBC**: Reactive database access
- **Spring Boot Validation**: Data validation
- **PostgreSQL**: Database with R2DBC driver
- **Elasticsearch Java Client**: Official Elasticsearch client
- **Spring Quartz**: Scheduler for periodic tasks
### 주요 의존성
- **Spring WebFlux**: 리액티브 웹 애플리케이션 개발
- **Spring Data R2DBC**: 리액티브 데이터베이스 액세스
- **Spring Boot Validation**: 데이터 유효성 검증
- **PostgreSQL**: R2DBC 드라이버가 포함된 데이터베이스
- **Elasticsearch Java Client**: 공식 Elasticsearch 클라이언트
- **Spring Quartz**: 주기적 작업을 위한 스케줄러

### Kotlin-specific Dependencies
- **kotlin-reflect**: Kotlin reflection library
- **kotlinx-coroutines-reactor**: Coroutines integration with Project Reactor
- **jackson-module-kotlin**: Jackson support for Kotlin
- **reactor-kotlin-extensions**: Project Reactor Kotlin extensions
### Kotlin 관련 의존성
- **kotlin-reflect**: Kotlin 리플렉션 라이브러리
- **kotlinx-coroutines-reactor**: Project Reactor와 코루틴 통합
- **jackson-module-kotlin**: Kotlin을 위한 Jackson 지원
- **reactor-kotlin-extensions**: Project Reactor의 Kotlin 확장

## Setup Instructions
## 설정 지침

### Prerequisites
- Java 21 or higher
- PostgreSQL database
- Elasticsearch instance
### 사전 요구사항
- Java 21 이상
- PostgreSQL 데이터베이스
- Elasticsearch 인스턴스

### Database Setup
1. Create a PostgreSQL database named `esdata`
2. Run the SQL script in `src/main/resources/schema.sql` to create the necessary tables
### 데이터베이스 설정
1. `esdata`라는 이름의 PostgreSQL 데이터베이스 생성
2. 필요한 테이블을 생성하기 위해 `src/main/resources/schema.sql` SQL 스크립트 실행

### Configuration
Configure the application in `application.properties`:
### 설정
`application.properties`에서 애플리케이션을 구성하세요:

```properties
# R2DBC PostgreSQL Configuration
spring.r2dbc.url=r2dbc:postgresql://localhost:5432/esdata
spring.r2dbc.username=your_username
spring.r2dbc.password=your_password

# Elasticsearch Configuration
spring.elasticsearch.uris=http://localhost:9200
spring.elasticsearch.username=elastic
spring.elasticsearch.password=your_password

# Scheduler Configuration
es.document.scheduler.cron=0 */5 * * * ?  # Every 5 minutes
es.document.query.index=your_index_name
es.document.query.size=100
```

### Building and Running
### 빌드 및 실행
```bash
./gradlew build
java -jar build/libs/es-r2dbc-webflux-kotlin-demo-0.0.1-SNAPSHOT.jar
```

## Key Components
## 주요 구성 요소

### 엔티티 및 리포지토리
- `Document`: PostgreSQL에 처리된 문서를 나타내는 엔티티
- `DocumentRepository`: 코루틴을 지원하는 R2DBC 리포지토리

### DTO
- `ElasticsearchDocument`: Elasticsearch 문서를 위한 DTO
- `DocumentSource`: 문서 내용을 위한 DTO

### 서비스
- `ElasticsearchClientService`: Elasticsearch 쿼리를 위한 서비스
- `DocumentProcessingService`: 문서 처리를 위한 서비스

### 스케줄러
- `DocumentProcessingScheduler`: 주기적인 문서 처리를 위한 스케줄러

## 빌드 정보
- 빌드 도구: Gradle (Kotlin DSL)
- 의존성 관리: Spring Dependency Management Plugin 1.1.7

## 자주 사용하는 명령어

### Gradle 빌드 명령어
- `./gradlew build`: 프로젝트 빌드
- `./gradlew test`: 테스트 실행
- `./gradlew bootRun`: 애플리케이션 실행
- `./gradlew dependencies`: 의존성 확인
- `./gradlew dependencyUpdates`: 의존성 업데이트 확인
- `./gradlew clean`: 프로젝트 클린
- `./gradlew clean build`: 클린 후 빌드
- `./gradlew build -x test`: 테스트 없이 빌드
- `./gradlew help --task <태스크명>`: 특정 태스크 정보 확인
- `./gradlew --status`: Gradle 데몬 상태 확인
- `./gradlew --stop`: Gradle 데몬 중지

### Entity and Repository
- `Document`: Entity representing processed documents in PostgreSQL
- `DocumentRepository`: R2DBC repository with coroutine support
### 애플리케이션 실행 명령어
- `java -jar build/libs/es-r2dbc-webflux-kotlin-demo-0.0.1-SNAPSHOT.jar`: 애플리케이션 JAR 실행
- `java -jar -Dspring.profiles.active=prod build/libs/es-r2dbc-webflux-kotlin-demo-0.0.1-SNAPSHOT.jar`: 특정 프로필로 실행
- `java -Xms256m -Xmx1g -jar build/libs/es-r2dbc-webflux-kotlin-demo-0.0.1-SNAPSHOT.jar`: JVM 메모리 옵션 설정
- `java -jar build/libs/es-r2dbc-webflux-kotlin-demo-0.0.1-SNAPSHOT.jar --server.port=9090`: 서버 포트 변경
- `java -jar build/libs/es-r2dbc-webflux-kotlin-demo-0.0.1-SNAPSHOT.jar --spring.config.location=file:./custom-config.yml`: 외부 설정 파일 지정

### DTOs
- `ElasticsearchDocument`: DTO for Elasticsearch documents
- `DocumentSource`: DTO for document content
### 데이터베이스 관련 명령어
- `psql -U username -d esdata`: PostgreSQL 접속
- `psql -U username -d esdata -f schema.sql`: SQL 파일 실행
- `pg_dump -U username -d esdata > backup.sql`: 데이터베이스 덤프 생성
- `psql -U username -d esdata < backup.sql`: 데이터베이스 복원
- `docker run --name postgres -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres`: Docker에서 PostgreSQL 실행

### Services
- `ElasticsearchClientService`: Service for querying Elasticsearch
- `DocumentProcessingService`: Service for processing documents
### 모니터링 및 관리 명령어
- `curl localhost:8080/actuator/health`: 액추에이터 엔드포인트 접근
- `tail -f logs/application.log`: 애플리케이션 로그 확인 (Linux/Mac)
- `jconsole`: 성능 모니터링 (JMX 활성화 필요)

### Scheduler
- `DocumentProcessingScheduler`: Scheduler for periodic document processing
## 패키지 구조 및 기능

## Build Information
- Build Tool: Gradle (Kotlin DSL)
- Dependency Management: Spring Dependency Management Plugin 1.1.7
### 구조 개요
- `com.angelsocer.es_r2dbc_webflux_kotlin_demo`: 루트 패키지
    - `config`: 애플리케이션 설정 클래스
        - Elasticsearch 클라이언트 설정
        - R2DBC 설정
        - 스케줄러 설정
    - `model`: 데이터 모델 클래스
        - `dto`: Elasticsearch 문서 관련 DTO
        - `entity`: PostgreSQL 엔티티 클래스
    - `repository`: R2DBC 리포지토리 인터페이스
    - `service`: 비즈니스 로직 구현 서비스
        - Elasticsearch 쿼리 서비스
        - 문서 처리 서비스
    - `scheduler`: 주기적 작업 실행 스케줄러
    - `util`: 유틸리티 클래스

### 주요 기능 설명
- **스케줄러**: Quartz를 사용한 주기적 문서 처리 트리거
- **Elasticsearch 클라이언트**: 코루틴 기반 비동기 ES 쿼리 실행
- **문서 처리 서비스**: Flow를 사용한 리액티브 데이터 처리
- **R2DBC 리포지토리**: 코루틴 기반 비동기 데이터베이스 액세스
- **오류 처리**: 전체 프로세스에 대한 포괄적인 오류 처리 및 로깅

